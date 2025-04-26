package com.biometria.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BiometriaFacial extends Biometria {
    private String imagemBase64;
    private String nomeImagem;
    private String fabricanteImagem;
    private String modeloImagem;
    private Double latitudeImagem;
    private Double longitudeImagem;
    private String dataImagem;
    private String sistemaImagem;
    private String filenameImagem;
}