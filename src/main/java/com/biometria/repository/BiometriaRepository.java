package com.biometria.repository;

import com.biometria.model.Biometria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiometriaRepository extends MongoRepository<Biometria, String> {
    // Você pode adicionar métodos de consulta específicos aqui, se necessário
}