package com.biometria.service;

import com.biometria.model.BiometriaFacial;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class BiometriaFacialAuxiliaryService {

    public BiometriaFacial processarBiometriaFacial(BiometriaFacial biometriaFacial) {
        if (StringUtils.hasText(biometriaFacial.getImagemBase64())) {
            try {
                byte[] decodedBytes = Base64.decodeBase64(biometriaFacial.getImagemBase64());
                ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);

                BufferedImage bufferedImage = ImageIO.read(bis);

                if (bufferedImage != null) {
                    ByteArrayInputStream bisForMetadata = new ByteArrayInputStream(decodedBytes);
                    Metadata metadata = ImageMetadataReader.readMetadata(bisForMetadata);

                    Map<String, String> metadados = new HashMap<>();
                    for (Directory directory : metadata.getDirectories()) {
                        for (Tag tag : directory.getTags()) {
                            metadados.put(directory.getName() + " - " + tag.getTagName(), tag.getDescription());
                        }
                    }
                    biometriaFacial.setMetadados(new com.biometria.model.Metadados()); // Garante que o objeto metadados não seja nulo
                    // Copia os metadados extraídos para o objeto Metadados
                    if (metadados.containsKey("Exif SubIFD - GPS Latitude")) {
                        biometriaFacial.getMetadados().setLatitude(converterCoordenada(metadados.get("Exif SubIFD - GPS Latitude")));
                    }
                    if (metadados.containsKey("Exif SubIFD - GPS Longitude")) {
                        biometriaFacial.getMetadados().setLongitude(converterCoordenada(metadados.get("Exif SubIFD - GPS Longitude")));
                    }
                    // O IP de origem virá da requisição, não dos metadados da imagem

                } else {
                    log.warn("Não foi possível ler a imagem facial para extrair metadados.");
                }

            } catch (IOException e) {
                log.error("Erro ao decodificar a imagem Base64 ou ler metadados: {}", e.getMessage());
            } catch (com.drew.imaging.ImageProcessingException e) {
                log.error("Erro ao processar metadados da imagem facial: {}", e.getMessage());
            }
        }
        return biometriaFacial;
    }

    // Método auxiliar para converter coordenadas GPS (se necessário)
    private Double converterCoordenada(String coordenadaDMS) {
        // Lógica para converter graus, minutos e segundos para decimal
        // Exemplo simplificado (precisa de implementação robusta)
        if (coordenadaDMS != null) {
            String[] parts = coordenadaDMS.split(",");
            if (parts.length == 3) {
                double graus = Double.parseDouble(parts[0].trim().split(" ")[0]);
                double minutos = Double.parseDouble(parts[1].trim().split(" ")[0]);
                double segundos = Double.parseDouble(parts[2].trim().split(" ")[0].replace("\"", ""));
                return graus + (minutos / 60.0) + (segundos / 3600.0);
            }
        }
        return null;
    }
}