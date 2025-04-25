package com.biometria.service;

import com.biometria.model.BiometriaFacial;
import com.biometria.repository.BiometriaFacialRepository;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BiometriaFacialService {

    private final BiometriaFacialRepository biometriaFacialRepository;

    public BiometriaFacial salvarBiometriaFacial(BiometriaFacial biometriaFacial) {
        if (StringUtils.hasText(biometriaFacial.getImagemBase64())) {
            try {
                byte[] decodedBytes = Base64.decodeBase64(biometriaFacial.getImagemBase64());
                ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);

                // Primeira leitura para extrair imagem
                BufferedImage bufferedImage = ImageIO.read(bis);

                if (bufferedImage != null) {
                    // Precisa criar um novo ByteArrayInputStream pois o anterior foi lido
                    ByteArrayInputStream bisForMetadata = new ByteArrayInputStream(decodedBytes);
                    Metadata metadata = ImageMetadataReader.readMetadata(bisForMetadata);

                    Map<String, String> metadados = new HashMap<>();
                    for (Directory directory : metadata.getDirectories()) {
                        for (Tag tag : directory.getTags()) {
                            metadados.put(directory.getName() + " - " + tag.getTagName(), tag.getDescription());
                        }
                    }
                    biometriaFacial.setMetadadosImagem(metadados);
                } else {
                    System.err.println("Não foi possível ler a imagem.");
                }

            } catch (IOException e) {
                System.err.println("Erro ao decodificar a imagem Base64 ou ler metadados: " + e.getMessage());
            } catch (com.drew.imaging.ImageProcessingException e) {
                System.err.println("Erro ao processar metadados da imagem: " + e.getMessage());
            }
        }
        return biometriaFacialRepository.save(biometriaFacial);
    }
}
