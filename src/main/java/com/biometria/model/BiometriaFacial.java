package com.biometria.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BiometriaFacial extends Biometria {
    private String imagemBase64;
    private String nomeImagem;
}
