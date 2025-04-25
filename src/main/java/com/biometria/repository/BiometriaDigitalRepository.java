package com.biometria.repository;

import com.biometria.model.BiometriaDigital;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BiometriaDigitalRepository extends MongoRepository<BiometriaDigital, String> {
}