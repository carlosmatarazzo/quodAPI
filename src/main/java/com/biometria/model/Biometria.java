package com.biometria.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipoBiometria",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BiometriaFacial.class, name = "facial"),
        @JsonSubTypes.Type(value = BiometriaDigital.class, name = "digital")
})
public abstract class Biometria {
    @Id
    private String id;
    private String tipoBiometria;
    private String dataCaptura;
    private Dispositivo dispositivo;
    private Metadados metadados;
}
