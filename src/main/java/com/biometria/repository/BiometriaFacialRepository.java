package com.biometria.repository;

import com.biometria.model.BiometriaFacial;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BiometriaFacialRepository extends MongoRepository<BiometriaFacial, String> {
}