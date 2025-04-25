package com.biometria.controller;

import com.biometria.model.BiometriaDigital;
import com.biometria.service.BiometriaDigitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/biometria/digital")
@RequiredArgsConstructor
public class BiometriaDigitalController {

    private final BiometriaDigitalService biometriaDigitalService;

    @PostMapping("/receber")
    public ResponseEntity<BiometriaDigital> receberBiometriaDigital(@RequestBody BiometriaDigital biometriaDigital) {
        BiometriaDigital savedBiometria = biometriaDigitalService.salvarBiometriaDigital(biometriaDigital);
        return new ResponseEntity<>(savedBiometria, HttpStatus.CREATED);
    }
}