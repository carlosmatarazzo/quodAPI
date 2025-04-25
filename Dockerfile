# Use a imagem base do OpenJDK para executar a aplicação Java
FROM openjdk:21-jdk-slim

# Defina o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copie o arquivo JAR construído da sua aplicação para o contêiner
# Assumindo que o arquivo JAR está na pasta 'target' e tem o nome 'seu-projeto.jar'
COPY target/*.jar app.jar

# Exponha a porta em que a sua aplicação Spring Boot está rodando (geralmente 8080)
EXPOSE 8080

# Defina o comando para executar a aplicação quando o contêiner iniciar
CMD ["java", "-jar", "app.jar"]