package com.biometria.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "notificacao")
public class Notificacao {

    @Id
    private String id;
    private String transacaoId;
    private String tipoBiometria;
    private String tipoFraude;
    private LocalDateTime dataCaptura;
    private Map<String, String> dispositivo;
    private List<String> canalNotificacao;
    private String notificadoPor;
    private Map<String, Object> metadados;
    private LocalDateTime dataEnvio;
    private String statusEnvio;
    private String respostaEnvio;
}