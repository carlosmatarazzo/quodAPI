package com.biometria.repository;

import com.biometria.model.BiometriaDocumento;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BiometriaDocumentoRepository extends MongoRepository<BiometriaDocumento, String> {
}