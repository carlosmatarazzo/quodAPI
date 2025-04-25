package com.biometria.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
public class Dispositivo {
    private String fabricante;
    private String modelo;
    private String sistemaOperacional;
}