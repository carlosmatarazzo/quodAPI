
# Sumário

- Introdução
- Tecnologias
- Configuração para Execução Local ou Docker
- Execução no Docker
- Fluxo de Negócio
  - tipoDocumento: "facial"
  - tipoDocumento: "documento"
  - tipoDocumento: "digital"
- Exemplos de Chamadas (JSON)
  - Biometria Facial Válida
  - Biometria Facial Inválida
  - Biometria Digital Válida
  - Biometria Digital Inválida
  - Biometria Documento Válida
  - Biometria Documento Inválida
- Swagger

# Introdução

API para receber chamadas para validação de biometria facial, digital e documental.

# Tecnologias
- Java Spring Boot
- Docker Compose
- MongoDB (local ou incluído no Docker)
- Beeceptor (fake, apenas para simular o sucesso de envio da notificação)

# Configuração para Execução Local ou Docker

Configurar a variável no application.properties:

Para Run pela IDE e MongoDB local:
```
spring.profiles.active=local
```
Para Run 100% Docker (Aplicação e MongoDB):
```
spring.profiles.active=docker
```
Para MongoDB Atlas (ainda não homologado - roadmap): 
```
spring.profiles.active=remoto
```

# Execução com Docker

Certifique-se de não estar executando servidor MongoDB local para evitar conflito.

Parar MongoDB local:
```
net stop MongoDB
```
Iniciar docker:
```
docker compose up --build
```
Parar docker:
```
docker compose down -v
```
Reiniciar MongoDB local:
```
net start MongoDB
```

# Fluxo de Negócio

## tipoDocumento: "facial"
1. Receber a imagemBase64 e dados do dispositivo;
2. Extrair os metadados do Base64 da imagem;
3. Comparar os dados do dispositivo com os dados do metadados.
4. Se resultado válido, gravar informações na collection biometria;
5. Se resultado inválido, gravar informações na collection biometria e gerar notificação na collection notificacao.

## tipoDocumento: "documento"
Similar ao fluxo do tipoDocumento: "facial". Não foi criado um serviço específico, pois a Documentoscopia foi dita pelo professor como opcional. 

## tipoDocumento: "digital"
Diferente dos fluxos anteriores, o próprio dispositivo envia o status.
Por questão de segurança, o templateBase64 da digital nunca sai do celular.

# Swagger
```
/swagger-ui/index.html#/
```

# Exemplos de Chamadas (JSON)

Biometria Facial Válida
```
{
"tipoBiometria": "facial",
"dispositivo": {
    "fabricante": "samsung",
    "modelo": "Galaxy S23+",
    "sistemaOperacional": "S916BXXS8CYBE",
    "dataDispositivo": "2025-04-25T15:24:49Z",
    "latitude": -23.1000,
    "longitude": -46.7500,
    "ipOrigem": "192.168.1.10"			
},
    "nomeImagem": "teste.jpg",
    "imagemBase64": "/9j/4AAQSkZJRgABAQAAAQABAAD/4QEsRXhpZgAATU0AKgAAAAgABgEPAAIAAAAIAAAAVgEQAAIAAAAMAAAAXgExAAIAAAAOAAAAagEyAAIAAAAUAAAAeIdpAAQAAAABAAAAjIglAAQAAAABAAAAwgAAAABzYW1zdW5nAEdhbGF4eSBTMjMrAFM5MTZCWFhTOENZQkUAMjAyNTowNDoyNSAxNToyNDo0OQAAApADAAIAAAAUAAAApqQzAAIAAAAIAAAAujIwMjU6MDQ6MjUgMTU6MjQ6NDkAc2Ftc3VuZwAABAABAAIAAAACTgAAAAACAAUAAAADAAAA9AADAAIAAAACRQAAAAAEAAUAAAADAAABDAAAAAAAAAABAAAAAAAAAAEAAAAAAAAAAQAAAAAAAAABAAAAAAAAAAEAAAAAAAAAAf/bAEMACAYGBwYFCAcHBwkJCAoMFA0MCwsMGRITDxQdGh8eHRocHCAkLicgIiwjHBwoNyksMDE0NDQfJzk9ODI8LjM0Mv/bAEMBCQkJDAsMGA0NGDIhHCEyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMv/AABEIAAoACgMBIgACEQEDEQH/xAAfAAABBQEBAQEBAQAAAAAAAAAAAQIDBAUGBwgJCgv/xAC1EAACAQMDAgQDBQUEBAAAAX0BAgMABBEFEiExQQYTUWEHInEUMoGRoQgjQrHBFVLR8CQzYnKCCQoWFxgZGiUmJygpKjQ1Njc4OTpDREVGR0hJSlNUVVZXWFlaY2RlZmdoaWpzdHV2d3h5eoOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4eLj5OXm5+jp6vHy8/T19vf4+fr/xAAfAQADAQEBAQEBAQEBAAAAAAAAAQIDBAUGBwgJCgv/xAC1EQACAQIEBAMEBwUEBAABAncAAQIDEQQFITEGEkFRB2FxEyIygQgUQpGhscEJIzNS8BVictEKFiQ04SXxFxgZGiYnKCkqNTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqCg4SFhoeIiYqSk5SVlpeYmZqio6Slpqeoqaqys7S1tre4ubrCw8TFxsfIycrS09TV1tfY2dri4+Tl5ufo6ery8/T19vf4+fr/2gAMAwEAAhEDEQA/APf6KKKAP//Z"
}
```
Biometria Facial Inválida
```
{
    "tipoBiometria": "facial",
    "dispositivo": {
        "fabricante": "samsung",
        "modelo": "Galaxy S23+",
        "sistemaOperacional": "S916BXXS8CYBE",
				"dataDispositivo": "2025-04-25T15:24:49Z",
        "latitude": -23.1000,
        "longitude": -46.7500,
        "ipOrigem": "192.168.1.10"			
    },
	"nomeImagem": "teste.jpg",
	"imagemBase64": "/9j/4AAQSkZJRgABAQAAAQABAAD/4QEsRXhpZgAATU0AKgAAAAgABgEPAAIAAAAIAAAAVgEQAAIAAAAMAAAAXgExAAIAAAAOAAAAagEyAAIAAAAUAAAAeIdpAAQAAAABAAAAjIglAAQAAAABAAAAwgAAAABzYW1zdW5nAEdhbGF4eSBTMjErAFM5MTZCWFhTOENZQkUAMjAyNTowNDoyNSAxNToyNDo0OQAAApADAAIAAAAUAAAApqQzAAIAAAAIAAAAujIwMjU6MDQ6MjUgMTU6MjQ6NDkAc2Ftc3VuZwAABAABAAIAAAACTgAAAAACAAUAAAADAAAA9AADAAIAAAACRQAAAAAEAAUAAAADAAABDAAAAAAAAAABAAAAAAAAAAEAAAAAAAAAAQAAAAAAAAABAAAAAAAAAAEAAAAAAAAAAf/bAEMACAYGBwYFCAcHBwkJCAoMFA0MCwsMGRITDxQdGh8eHRocHCAkLicgIiwjHBwoNyksMDE0NDQfJzk9ODI8LjM0Mv/bAEMBCQkJDAsMGA0NGDIhHCEyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMv/AABEIAAoACgMBIgACEQEDEQH/xAAfAAABBQEBAQEBAQAAAAAAAAAAAQIDBAUGBwgJCgv/xAC1EAACAQMDAgQDBQUEBAAAAX0BAgMABBEFEiExQQYTUWEHInEUMoGRoQgjQrHBFVLR8CQzYnKCCQoWFxgZGiUmJygpKjQ1Njc4OTpDREVGR0hJSlNUVVZXWFlaY2RlZmdoaWpzdHV2d3h5eoOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4eLj5OXm5+jp6vHy8/T19vf4+fr/xAAfAQADAQEBAQEBAQEBAAAAAAAAAQIDBAUGBwgJCgv/xAC1EQACAQIEBAMEBwUEBAABAncAAQIDEQQFITEGEkFRB2FxEyIygQgUQpGhscEJIzNS8BVictEKFiQ04SXxFxgZGiYnKCkqNTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqCg4SFhoeIiYqSk5SVlpeYmZqio6Slpqeoqaqys7S1tre4ubrCw8TFxsfIycrS09TV1tfY2dri4+Tl5ufo6ery8/T19vf4+fr/2gAMAwEAAhEDEQA/APf6KKKAP//Z"
}
```
Biometria Digital Válida
```
{
  "tipoBiometria": "digital",
  "status": "autenticado",
  "dataCaptura": "2025-04-26T21:00:00Z",
  "dispositivo": {
    "fabricante": "Samsung",
    "modelo": "Galaxy S23",
    "sistemaOperacional": "Android 14",
    "latitude": -23.55052,
    "longitude": -46.633308,
    "ipOrigem": "179.234.56.12"
  }
}
```
Biometria Digital Inválida
```
{
  "tipoBiometria": "digital",
  "status": "falha_autenticacao",
  "dataCaptura": "2025-04-26T21:00:00Z",
  "dispositivo": {
    "fabricante": "Samsung",
    "modelo": "Galaxy S23",
    "sistemaOperacional": "Android 14",
    "latitude": -23.55052,
    "longitude": -46.633308,
    "ipOrigem": "179.234.56.12"
  }
}
```
Biometria Documento Válida
```
{
    "tipoBiometria": "documento",
    "dispositivo": {
        "fabricante": "samsung",
        "modelo": "Galaxy S23+",
        "sistemaOperacional": "S916BXXS8CYBE",
				"dataDispositivo": "2025-04-25T15:24:49Z",
        "latitude": -23.1000,
        "longitude": -46.7500,
        "ipOrigem": "192.168.1.10"			
    },
	"nomeImagem": "teste.jpg",
	"imagemBase64": "/9j/4AAQSkZJRgABAQAAAQABAAD/4QEsRXhpZgAATU0AKgAAAAgABgEPAAIAAAAIAAAAVgEQAAIAAAAMAAAAXgExAAIAAAAOAAAAagEyAAIAAAAUAAAAeIdpAAQAAAABAAAAjIglAAQAAAABAAAAwgAAAABzYW1zdW5nAEdhbGF4eSBTMjMrAFM5MTZCWFhTOENZQkUAMjAyNTowNDoyNSAxNToyNDo0OQAAApADAAIAAAAUAAAApqQzAAIAAAAIAAAAujIwMjU6MDQ6MjUgMTU6MjQ6NDkAc2Ftc3VuZwAABAABAAIAAAACTgAAAAACAAUAAAADAAAA9AADAAIAAAACRQAAAAAEAAUAAAADAAABDAAAAAAAAAABAAAAAAAAAAEAAAAAAAAAAQAAAAAAAAABAAAAAAAAAAEAAAAAAAAAAf/bAEMACAYGBwYFCAcHBwkJCAoMFA0MCwsMGRITDxQdGh8eHRocHCAkLicgIiwjHBwoNyksMDE0NDQfJzk9ODI8LjM0Mv/bAEMBCQkJDAsMGA0NGDIhHCEyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMv/AABEIAAoACgMBIgACEQEDEQH/xAAfAAABBQEBAQEBAQAAAAAAAAAAAQIDBAUGBwgJCgv/xAC1EAACAQMDAgQDBQUEBAAAAX0BAgMABBEFEiExQQYTUWEHInEUMoGRoQgjQrHBFVLR8CQzYnKCCQoWFxgZGiUmJygpKjQ1Njc4OTpDREVGR0hJSlNUVVZXWFlaY2RlZmdoaWpzdHV2d3h5eoOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4eLj5OXm5+jp6vHy8/T19vf4+fr/xAAfAQADAQEBAQEBAQEBAAAAAAAAAQIDBAUGBwgJCgv/xAC1EQACAQIEBAMEBwUEBAABAncAAQIDEQQFITEGEkFRB2FxEyIygQgUQpGhscEJIzNS8BVictEKFiQ04SXxFxgZGiYnKCkqNTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqCg4SFhoeIiYqSk5SVlpeYmZqio6Slpqeoqaqys7S1tre4ubrCw8TFxsfIycrS09TV1tfY2dri4+Tl5ufo6ery8/T19vf4+fr/2gAMAwEAAhEDEQA/APf6KKKAP//Z"
}
```
Biometria Documento Inválida
```
{
    "tipoBiometria": "documento",
    "dispositivo": {
        "fabricante": "Apple",
        "modelo": "Galaxy S23+",
        "sistemaOperacional": "S916BXXS8CYBE",
				"dataDispositivo": "2025-04-25T15:24:49Z",
        "latitude": -23.1000,
        "longitude": -46.7500,
        "ipOrigem": "192.168.1.10"			
    },
	"nomeImagem": "teste.jpg",
	"imagemBase64": "/9j/4AAQSkZJRgABAQAAAQABAAD/4QEsRXhpZgAATU0AKgAAAAgABgEPAAIAAAAIAAAAVgEQAAIAAAAMAAAAXgExAAIAAAAOAAAAagEyAAIAAAAUAAAAeIdpAAQAAAABAAAAjIglAAQAAAABAAAAwgAAAABzYW1zdW5nAEdhbGF4eSBTMjMrAFM5MTZCWFhTOENZQkUAMjAyNTowNDoyNSAxNToyNDo0OQAAApADAAIAAAAUAAAApqQzAAIAAAAIAAAAujIwMjU6MDQ6MjUgMTU6MjQ6NDkAc2Ftc3VuZwAABAABAAIAAAACTgAAAAACAAUAAAADAAAA9AADAAIAAAACRQAAAAAEAAUAAAADAAABDAAAAAAAAAABAAAAAAAAAAEAAAAAAAAAAQAAAAAAAAABAAAAAAAAAAEAAAAAAAAAAf/bAEMACAYGBwYFCAcHBwkJCAoMFA0MCwsMGRITDxQdGh8eHRocHCAkLicgIiwjHBwoNyksMDE0NDQfJzk9ODI8LjM0Mv/bAEMBCQkJDAsMGA0NGDIhHCEyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMv/AABEIAAoACgMBIgACEQEDEQH/xAAfAAABBQEBAQEBAQAAAAAAAAAAAQIDBAUGBwgJCgv/xAC1EAACAQMDAgQDBQUEBAAAAX0BAgMABBEFEiExQQYTUWEHInEUMoGRoQgjQrHBFVLR8CQzYnKCCQoWFxgZGiUmJygpKjQ1Njc4OTpDREVGR0hJSlNUVVZXWFlaY2RlZmdoaWpzdHV2d3h5eoOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4eLj5OXm5+jp6vHy8/T19vf4+fr/xAAfAQADAQEBAQEBAQEBAAAAAAAAAQIDBAUGBwgJCgv/xAC1EQACAQIEBAMEBwUEBAABAncAAQIDEQQFITEGEkFRB2FxEyIygQgUQpGhscEJIzNS8BVictEKFiQ04SXxFxgZGiYnKCkqNTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqCg4SFhoeIiYqSk5SVlpeYmZqio6Slpqeoqaqys7S1tre4ubrCw8TFxsfIycrS09TV1tfY2dri4+Tl5ufo6ery8/T19vf4+fr/2gAMAwEAAhEDEQA/APf6KKKAP//Z"
}
```