package com.biometria.model;

import lombok.Data;

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