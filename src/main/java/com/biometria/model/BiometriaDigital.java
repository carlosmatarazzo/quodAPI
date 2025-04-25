package com.biometria.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BiometriaDigital extends Biometria {
    private String templateBase64;
}
