package com.biometria.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
public class Dispositivo {
    private String fabricante;
    private String modelo;
    private String sistemaOperacional;
    private String dataDispositivo;
    private Double latitude;
    private Double longitude;
    private String ipOrigem;
}