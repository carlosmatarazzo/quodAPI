package com.biometria.controller;

import com.biometria.model.Biometria;
import com.biometria.service.BiometriaService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/biometria")
@RequiredArgsConstructor
public class BiometriaController {

    private static final Logger logger = LoggerFactory.getLogger(BiometriaController.class);
    private final BiometriaService biometriaService;

    @PostMapping("/receber")
    public ResponseEntity<Biometria> receberBiometria(@RequestBody Biometria biometria) {
        logger.info("Recebendo requisição POST para /api/biometria/receber");
        logger.info("Objeto Biometria recebido (antes do service): {}", biometria);
        if (biometria == null) {
            logger.error("Objeto Biometria chegou como null!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Biometria savedBiometria = biometriaService.salvarBiometria(biometria);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBiometria);
    }

    @GetMapping
    public String obterStatusBiometria() {
        return "Endpoint de biometria funcionando!";
    }
}