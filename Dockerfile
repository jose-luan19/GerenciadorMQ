FROM openjdk:11-jre-slim
WORKDIR /app

COPY out/artifacts/Gerenciador_jar/Gerenciador.jar .

# Expõe a porta em que a aplicação Spring Boot será executada e a porta para conectar o remote debug
EXPOSE 8080
EXPOSE 5005

# Comando para iniciar a aplicação Spring Boot com a porta 8000 habilitada para se conectar com remote debug da IDE
CMD ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-Xmx1G", "-Xms128m", "-XX:MaxMetaspaceSize=128m", "-jar", "Gerenciador.jar"]