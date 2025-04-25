package com.biometria.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "biometrias_faciais")
@Data
public class BiometriaFacial {

    @Id
    private String id;
    private String campoGenerico1;
    private String campoGenerico2;
    // Adicione outros campos genéricos conforme necessário
    private String imagemBase64;
    private Map<String, String> metadadosImagem;
}