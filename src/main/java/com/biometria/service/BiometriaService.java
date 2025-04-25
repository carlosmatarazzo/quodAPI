package com.biometria.service;

import com.biometria.model.Biometria;
import com.biometria.model.BiometriaDigital;
import com.biometria.model.BiometriaFacial;
import com.biometria.repository.BiometriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BiometriaService {

    private final BiometriaRepository biometriaRepository;
    private final BiometriaFacialAuxiliaryService biometriaFacialAuxiliaryService; // Injetando a classe renomeada

    public Biometria salvarBiometria(Biometria biometria) {
        if ("facial".equalsIgnoreCase(biometria.getTipoBiometria())) {
            if (biometria instanceof BiometriaFacial) {
                BiometriaFacial biometriaFacial = (BiometriaFacial) biometria;
                // Processamento específico para biometria facial (extração de metadados, etc.)
                biometriaFacial = biometriaFacialAuxiliaryService.processarBiometriaFacial(biometriaFacial);
                return biometriaRepository.save(biometriaFacial);
            } else {
                throw new IllegalArgumentException("Tipo de biometria 'facial' recebido, mas o objeto não é BiometriaFacial.");
            }
        } else if ("digital".equalsIgnoreCase(biometria.getTipoBiometria())) {
            if (biometria instanceof BiometriaDigital) {
                // Nenhuma lógica de processamento específica para digital no momento
                return biometriaRepository.save((BiometriaDigital) biometria);
            } else {
                throw new IllegalArgumentException("Tipo de biometria 'digital' recebido, mas o objeto não é BiometriaDigital.");
            }
        } else {
            throw new IllegalArgumentException("Tipo de biometria desconhecido: " + biometria.getTipoBiometria());
        }
    }
}