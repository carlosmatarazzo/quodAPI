package com.biometria.repository;

import com.biometria.model.Notificacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacaoRepository extends MongoRepository<Notificacao, String> {
}