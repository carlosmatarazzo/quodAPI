package com.biometria.service;

import com.biometria.model.BiometriaImagem;
import com.biometria.model.Dispositivo;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public abstract class BiometriaImagemAuxiliaryService<T extends BiometriaImagem> {

    public T processarBiometriaImagem(T biometriaImagem) {
        if (StringUtils.hasText(biometriaImagem.getImagemBase64())) {
            try {
                byte[] decodedBytes = Base64.decodeBase64(biometriaImagem.getImagemBase64());
                ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
                BufferedImage bufferedImage = ImageIO.read(bis);

                if (bufferedImage != null) {
                    ByteArrayInputStream bisForMetadata = new ByteArrayInputStream(decodedBytes);
                    Metadata metadata = ImageMetadataReader.readMetadata(bisForMetadata);

                    extrairMetadados(biometriaImagem, metadata);

                } else {
                    log.warn("Não foi possível ler a imagem para extrair metadados.");
                    biometriaImagem.setStatus("erro_leitura_imagem");
                }

            } catch (IOException | com.drew.imaging.ImageProcessingException e) {
                log.error("Erro ao processar imagem: {}", e.getMessage());
                biometriaImagem.setStatus("erro_processamento");
            }
        } else {
            biometriaImagem.setStatus("sem_imagem");
        }
        return biometriaImagem;
    }

    private void extrairMetadados(T biometriaImagem, Metadata metadata) {
        String fabricanteImagem = null;
        String modeloImagem = null;
        String dataHoraOriginal = null;
        Double latitudeImagem = null;
        Double longitudeImagem = null;
        String filenameImagem = null;
        String sistemaImagem = null;

        for (Directory directory : metadata.getDirectories()) {
            if (directory instanceof ExifIFD0Directory) {
                ExifIFD0Directory exifIFD0Directory = (ExifIFD0Directory) directory;
                fabricanteImagem = exifIFD0Directory.getString(ExifIFD0Directory.TAG_MAKE);
                modeloImagem = exifIFD0Directory.getString(ExifIFD0Directory.TAG_MODEL);
                sistemaImagem = exifIFD0Directory.getString(ExifIFD0Directory.TAG_SOFTWARE);
            } else if (directory instanceof ExifSubIFDDirectory) {
                ExifSubIFDDirectory exifSubIFDDirectory = (ExifSubIFDDirectory) directory;
                dataHoraOriginal = exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            } else if (directory instanceof GpsDirectory) {
                GpsDirectory gpsDirectory = (GpsDirectory) directory;
                latitudeImagem = gpsDirectory.getGeoLocation() != null ? gpsDirectory.getGeoLocation().getLatitude() : null;
                longitudeImagem = gpsDirectory.getGeoLocation() != null ? gpsDirectory.getGeoLocation().getLongitude() : null;
            } else if (directory.getName().equalsIgnoreCase("File")) {
                for (Tag tag : directory.getTags()) {
                    if (tag.getTagName().equalsIgnoreCase("File Name")) {
                        filenameImagem = tag.getDescription();
                    }
                }
            }
        }

        preencherCampos(biometriaImagem, fabricanteImagem, modeloImagem, dataHoraOriginal, latitudeImagem, longitudeImagem, filenameImagem, sistemaImagem);
    }

    private void preencherCampos(T biometriaImagem, String fabricanteImagem, String modeloImagem, String dataHoraOriginal, Double latitudeImagem, Double longitudeImagem, String filenameImagem, String sistemaImagem) {
        Dispositivo dispositivoApi = biometriaImagem.getDispositivo();

        biometriaImagem.setFabricanteImagem(fabricanteImagem);
        biometriaImagem.setModeloImagem(modeloImagem);
        biometriaImagem.setDataImagem(parseDataHoraOriginal(dataHoraOriginal));
        biometriaImagem.setLatitudeImagem(latitudeImagem);
        biometriaImagem.setLongitudeImagem(longitudeImagem);
        biometriaImagem.setFilenameImagem(filenameImagem);
        biometriaImagem.setSistemaImagem(sistemaImagem);

        boolean dispositivoValido = false;
        boolean dataHoraValida = false;
        List<String> motivosInvalidade = new ArrayList<>();
        boolean dispositivoInformado = dispositivoApi != null &&
                StringUtils.hasText(dispositivoApi.getFabricante()) &&
                StringUtils.hasText(dispositivoApi.getModelo());
        boolean metadadosDispositivoPresentes = StringUtils.hasText(fabricanteImagem) &&
                StringUtils.hasText(modeloImagem);
        boolean dataHoraApiInformada = dispositivoApi != null &&
                StringUtils.hasText(dispositivoApi.getDataDispositivo());
        boolean dataHoraMetadadosPresente = StringUtils.hasText(dataHoraOriginal);
        boolean sistemaOperacionalInformado = dispositivoApi != null && StringUtils.hasText(dispositivoApi.getSistemaOperacional());
        boolean sistemaOperacionalMetadadosPresente = StringUtils.hasText(sistemaImagem);
        boolean sistemaOperacionalDivergente = sistemaOperacionalInformado && sistemaOperacionalMetadadosPresente &&
                !dispositivoApi.getSistemaOperacional().trim().equalsIgnoreCase(sistemaImagem.trim());

        if (dispositivoInformado && metadadosDispositivoPresentes) {
            if (dispositivoApi.getFabricante().trim().equalsIgnoreCase(fabricanteImagem.trim()) &&
                    dispositivoApi.getModelo().trim().equalsIgnoreCase(modeloImagem.trim())) {
                dispositivoValido = true;
            } else {
                motivosInvalidade.add("dispositivo_divergente");
            }
        } else if (dispositivoInformado && !metadadosDispositivoPresentes) {
            motivosInvalidade.add("metadados_dispositivo_ausentes");
        } else if (!dispositivoInformado && metadadosDispositivoPresentes) {
            motivosInvalidade.add("dados_dispositivo_api_ausentes");
        }

        if (dataHoraApiInformada && dataHoraMetadadosPresente) {
            String dataHoraApiFormatada = formatarDataHoraParaComparacao(dispositivoApi.getDataDispositivo());
            String dataHoraMetadadosFormatada = formatarDataHoraMetadadosParaComparacao(dataHoraOriginal);
            if (dataHoraApiFormatada != null && dataHoraMetadadosFormatada != null &&
                    dataHoraApiFormatada.substring(0, 12).equals(dataHoraMetadadosFormatada.substring(0, 12))) {
                dataHoraValida = true;
            } else {
                motivosInvalidade.add("datahora_divergente");
            }
        } else if (dataHoraApiInformada && !dataHoraMetadadosPresente) {
            motivosInvalidade.add("metadados_datahora_ausentes");
        } else if (!dataHoraApiInformada && dataHoraMetadadosPresente) {
            motivosInvalidade.add("dados_datahora_api_ausentes");
        }

        if (sistemaOperacionalDivergente) {
            motivosInvalidade.add("sistema_operacional_divergente");
        } else if (sistemaOperacionalInformado && !sistemaOperacionalMetadadosPresente) {
            motivosInvalidade.add("metadados_sistema_operacional_ausente");
        } else if (!sistemaOperacionalInformado && sistemaOperacionalMetadadosPresente) {
            motivosInvalidade.add("dados_sistema_operacional_api_ausentes");
        }

        if (dispositivoValido && dataHoraValida && motivosInvalidade.isEmpty()) {
            biometriaImagem.setStatus("válido");
        } else {
            if (!motivosInvalidade.isEmpty()) {
                biometriaImagem.setStatus("inválido_" + String.join("_", motivosInvalidade));
            } else {
                biometriaImagem.setStatus("indeterminado");
            }
        }
    }

    private String formatarDataHoraParaComparacao(String dataHoraApi) {
        try {
            SimpleDateFormat sdfApi = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date dateApi = sdfApi.parse(dataHoraApi.replaceAll("Z$", "+0000"));
            SimpleDateFormat sdfComparacao = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault());
            return sdfComparacao.format(dateApi);
        } catch (ParseException e) {
            log.warn("Erro ao formatar data/hora da API para comparação: {}", dataHoraApi);
            return null;
        }
    }

    private String formatarDataHoraMetadadosParaComparacao(String dataHoraOriginal) {
        try {
            DateFormat exifFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.getDefault());
            Date dateMetadados = exifFormat.parse(dataHoraOriginal);
            SimpleDateFormat sdfComparacao = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault());
            return sdfComparacao.format(dateMetadados);
        } catch (ParseException e) {
            log.warn("Erro ao formatar data/hora dos metadados para comparação: {}", dataHoraOriginal);
            return null;
        }
    }

    private String parseDataHoraOriginal(String dataHoraOriginal) {
        if (dataHoraOriginal != null) {
            try {
                DateFormat exifFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.getDefault());
                Date date = exifFormat.parse(dataHoraOriginal);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault());
                return sdf.format(date);
            } catch (ParseException e) {
                log.warn("Não foi possível parsear a data/hora original da imagem: {}", dataHoraOriginal);
                return null;
            }
        }
        return null;
    }
}