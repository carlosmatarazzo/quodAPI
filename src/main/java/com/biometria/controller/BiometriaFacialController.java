package com.biometria.controller;

import com.biometria.model.BiometriaFacial;
import com.biometria.service.BiometriaFacialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/biometria/facial")
@RequiredArgsConstructor
public class BiometriaFacialController {

    private final BiometriaFacialService biometriaFacialService;

    @PostMapping("/receber")
    public ResponseEntity<BiometriaFacial> receberBiometriaFacial(@RequestBody BiometriaFacial biometriaFacial) {
        BiometriaFacial savedBiometria = biometriaFacialService.salvarBiometriaFacial(biometriaFacial);
        return new ResponseEntity<>(savedBiometria, HttpStatus.CREATED);
    }
}