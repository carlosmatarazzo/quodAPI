package com.biometria.service;

import com.biometria.model.BiometriaDigital;
import com.biometria.repository.BiometriaDigitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BiometriaDigitalService {

    private final BiometriaDigitalRepository biometriaDigitalRepository;

    public BiometriaDigital salvarBiometriaDigital(BiometriaDigital biometriaDigital) {
        // Aqui você pode adicionar lógica de validação ou processamento adicional
        return biometriaDigitalRepository.save(biometriaDigital);
    }
}