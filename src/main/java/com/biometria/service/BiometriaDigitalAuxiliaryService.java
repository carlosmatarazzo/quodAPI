package com.biometria.service;

import com.biometria.model.BiometriaDigital;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class BiometriaDigitalAuxiliaryService {

    public BiometriaDigital processarBiometriaDigital(BiometriaDigital biometriaDigital) {
        if (biometriaDigital == null) {
            throw new IllegalArgumentException("Objeto BiometriaDigital não pode ser nulo.");
        }

        if (!StringUtils.hasText(biometriaDigital.getStatus())) {
            log.warn("Status da biometria digital não informado.");
            biometriaDigital.setStatus("indeterminado");
        } else if (biometriaDigital.getStatus().equalsIgnoreCase("autenticado")) {
            log.info("Biometria digital autenticada localmente no dispositivo.");
            biometriaDigital.setStatus("autenticado");
        } else if (biometriaDigital.getStatus().equalsIgnoreCase("falha_autenticacao")) {
            log.info("Falha na autenticação biométrica digital.");
            biometriaDigital.setStatus("falha_autenticacao");
        } else {
            log.warn("Status desconhecido recebido para biometria digital: {}", biometriaDigital.getStatus());
            biometriaDigital.setStatus("indeterminado");
        }

        return biometriaDigital;
    }
}
