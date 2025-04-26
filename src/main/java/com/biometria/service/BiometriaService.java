package com.biometria.service;

import com.biometria.model.Biometria;
import com.biometria.model.BiometriaDigital;
import com.biometria.model.BiometriaFacial;
import com.biometria.repository.BiometriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BiometriaService {

    private final BiometriaRepository biometriaRepository;
    private final BiometriaFacialAuxiliaryService biometriaFacialAuxiliaryService;

    public Biometria salvarBiometria(Biometria biometria) {
        if ("facial".equalsIgnoreCase(biometria.getTipoBiometria())) {
            if (biometria instanceof BiometriaFacial) {
                BiometriaFacial biometriaFacial = (BiometriaFacial) biometria;
                biometriaFacial = biometriaFacialAuxiliaryService.processarBiometriaFacial(biometriaFacial);
                // Exemplo de acesso a latitude agora no dispositivo (se você usava antes)
                if (biometriaFacial.getDispositivo() != null && biometriaFacial.getDispositivo().getLatitude() != null) {
                    log.info("Latitude do dispositivo: {}", biometriaFacial.getDispositivo().getLatitude());
                }
                return biometriaRepository.save(biometriaFacial);
            } else {
                throw new IllegalArgumentException("Tipo de biometria 'facial' recebido, mas o objeto não é BiometriaFacial.");
            }
        } else if ("digital".equalsIgnoreCase(biometria.getTipoBiometria())) {
            if (biometria instanceof BiometriaDigital) {
                biometria.setDataCaptura(biometria.getDispositivo().getDataDispositivo());
                return biometriaRepository.save((BiometriaDigital) biometria);
            } else {
                throw new IllegalArgumentException("Tipo de biometria 'digital' recebido, mas o objeto não é BiometriaDigital.");
            }
        } else {
            throw new IllegalArgumentException("Tipo de biometria desconhecido: " + biometria.getTipoBiometria());
        }
    }
}