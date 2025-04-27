package com.biometria.service;

import com.biometria.model.Biometria;
import com.biometria.model.BiometriaFacial;
import com.biometria.model.BiometriaDocumento;
import com.biometria.model.BiometriaDigital;
import com.biometria.repository.BiometriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BiometriaService {

    private final BiometriaRepository biometriaRepository;
    private final BiometriaFacialAuxiliaryService biometriaFacialAuxiliaryService;
    private final BiometriaDocumentoAuxiliaryService biometriaDocumentoAuxiliaryService;
    private final BiometriaDigitalAuxiliaryService biometriaDigitalAuxiliaryService;
    private final NotificacaoFraudeService notificacaoFraudeService;

    public Biometria processarBiometria(Biometria biometria, String ipOrigem) {
        if (biometria == null) {
            throw new IllegalArgumentException("Objeto biometria não pode ser nulo.");
        }

        Biometria biometriaProcessada;

        if (biometria instanceof BiometriaFacial) {
            biometriaProcessada = biometriaFacialAuxiliaryService.processarBiometriaImagem((BiometriaFacial) biometria);
        } else if (biometria instanceof BiometriaDocumento) {
            biometriaProcessada = biometriaDocumentoAuxiliaryService.processarBiometriaImagem((BiometriaDocumento) biometria);
        } else if (biometria instanceof BiometriaDigital) {
            biometriaProcessada = biometriaDigitalAuxiliaryService.processarBiometriaDigital((BiometriaDigital) biometria);
        } else {
            throw new IllegalArgumentException("Tipo de biometria não suportado: " + biometria.getClass().getSimpleName());
        }

        biometriaRepository.save(biometriaProcessada);
        notificacaoFraudeService.notificarFraude(biometriaProcessada, ipOrigem);

        return biometriaProcessada;
    }
}
