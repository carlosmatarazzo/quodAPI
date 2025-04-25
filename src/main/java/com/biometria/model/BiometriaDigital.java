package com.biometria.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "biometrias_digitais")
@Data
public class BiometriaDigital {

    @Id
    private String id;
    private String campoGenerico1;
    private String campoGenerico2;
    // Adicione outros campos genéricos conforme necessário
    private String dadosBiometricosDigitais; // Este campo receberá os dados da digital
}