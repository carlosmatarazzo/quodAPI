package com.biometria.service;

import com.biometria.model.BiometriaDigital;
import com.biometria.repository.BiometriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BiometriaDigitalService {

    private final BiometriaRepository biometriaRepository;

    public BiometriaDigital salvarBiometriaDigital(BiometriaDigital biometriaDigital) {
        // Nenhuma lógica de processamento específica aqui agora
        return biometriaRepository.save(biometriaDigital);
    }
}