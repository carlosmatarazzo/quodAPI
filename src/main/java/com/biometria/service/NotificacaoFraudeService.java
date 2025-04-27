package com.biometria.service;

import com.biometria.model.Biometria;
import com.biometria.model.BiometriaDigital;
import com.biometria.model.Dispositivo;
import com.biometria.model.Notificacao;
import com.biometria.repository.NotificacaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class NotificacaoFraudeService {

    private final RestTemplate restTemplate;
    private final NotificacaoRepository notificacaoRepository;

    @Value("${api.notificacao.url}")
    private String apiUrl;

    public NotificacaoFraudeService(RestTemplateBuilder restTemplateBuilder, NotificacaoRepository notificacaoRepository) {
        this.restTemplate = restTemplateBuilder.build();
        this.notificacaoRepository = notificacaoRepository;
    }

    public void notificarFraude(Biometria biometria, String ipOrigem) {
        if (biometria == null) {
            log.warn("Biometria nula recebida. Nenhuma notificação será enviada.");
            return;
        }

        boolean deveNotificar = avaliarSeDeveNotificar(biometria);

        if (!deveNotificar) {
            log.info("Nenhuma notificação de fraude será enviada para esta biometria. Tipo: {}, Status: {}",
                    biometria.getTipoBiometria(), biometria.getStatus());
            return;
        }

        String transacaoId = UUID.randomUUID().toString();
        Map<String, Object> payload = montarPayload(biometria, ipOrigem, transacaoId);

        Notificacao notificacao = montarNotificacao(biometria, transacaoId, payload);
        notificacaoRepository.save(notificacao);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
            log.info("Chamada para API de notificação de fraude concluída. Status: {}, Corpo: {}", response.getStatusCode(), response.getBody());
            notificacao.setStatusEnvio("sucesso");
            notificacao.setRespostaEnvio(response.getBody());
        } catch (Exception e) {
            log.error("Erro ao chamar API de notificação de fraude: {}", e.getMessage(), e);
            notificacao.setStatusEnvio("falha");
            notificacao.setRespostaEnvio(e.getMessage());
        }

        notificacaoRepository.save(notificacao);
    }

    private boolean avaliarSeDeveNotificar(Biometria biometria) {
        if (biometria instanceof BiometriaDigital) {
            // Para biometria digital, só notifica se falhou a autenticação
            return "falha_autenticacao".equalsIgnoreCase(biometria.getStatus());
        } else {
            // Para facial e documento, notifica se não for válido
            return biometria.getStatus() != null && !biometria.getStatus().equalsIgnoreCase("válido");
        }
    }

    private Map<String, Object> montarPayload(Biometria biometria, String ipOrigem, String transacaoId) {
        Map<String, Object> payload = new HashMap<>();

        payload.put("transacaoId", transacaoId);
        payload.put("tipoBiometria", biometria.getTipoBiometria());
        payload.put("tipoFraude", biometria.getStatus());
        payload.put("dataCaptura", biometria.getDataCaptura());
        payload.put("canalNotificacao", Arrays.asList("sms", "email"));
        payload.put("notificadoPor", "sistema-de-monitoramento");

        Dispositivo dispositivo = biometria.getDispositivo();
        if (dispositivo != null) {
            Map<String, String> dispositivoPayload = new HashMap<>();
            dispositivoPayload.put("fabricante", dispositivo.getFabricante());
            dispositivoPayload.put("modelo", dispositivo.getModelo());
            dispositivoPayload.put("sistemaOperacional", dispositivo.getSistemaOperacional());
            payload.put("dispositivo", dispositivoPayload);
        }

        Map<String, Object> metadadosPayload = new HashMap<>();
        if (dispositivo != null) {
            metadadosPayload.put("latitude", dispositivo.getLatitude());
            metadadosPayload.put("longitude", dispositivo.getLongitude());
            metadadosPayload.put("ipOrigem", dispositivo.getIpOrigem() != null ? dispositivo.getIpOrigem() : ipOrigem);
        } else {
            metadadosPayload.put("latitude", null);
            metadadosPayload.put("longitude", null);
            metadadosPayload.put("ipOrigem", ipOrigem);
        }
        payload.put("metadados", metadadosPayload);

        return payload;
    }

    private Notificacao montarNotificacao(Biometria biometria, String transacaoId, Map<String, Object> payload) {
        Notificacao notificacao = new Notificacao();
        notificacao.setTransacaoId(transacaoId);
        notificacao.setTipoBiometria(biometria.getTipoBiometria());
        notificacao.setTipoFraude(biometria.getStatus());
        notificacao.setDataCaptura(parseDataCaptura(biometria.getDataCaptura()));
        notificacao.setDispositivo((Map<String, String>) payload.get("dispositivo"));
        notificacao.setCanalNotificacao(Arrays.asList("sms", "email"));
        notificacao.setNotificadoPor("sistema-de-monitoramento");
        notificacao.setMetadados((Map<String, Object>) payload.get("metadados"));
        notificacao.setDataEnvio(LocalDateTime.now(java.time.ZoneId.of("America/Sao_Paulo")));
        notificacao.setStatusEnvio("pendente");

        return notificacao;
    }

    private LocalDateTime parseDataCaptura(String dataCapturaStr) {
        if (dataCapturaStr == null) {
            return null;
        }

        try {
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(dataCapturaStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            return offsetDateTime.toLocalDateTime();
        } catch (Exception e) {
            try {
                LocalDateTime localDateTime = LocalDateTime.parse(dataCapturaStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                return localDateTime;
            } catch (Exception ex) {
                log.warn("Não foi possível parsear a dataCaptura: {}", dataCapturaStr);
                return null;
            }
        }
    }
}