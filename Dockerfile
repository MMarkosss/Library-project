# 1. Qual é o sistema base? Um Linux minúsculo (Alpine) com Java 21 já instalado!
FROM eclipse-temurin:21-jre-alpine

# 2. Cria uma pasta interna chamada /app
WORKDIR /app

# 3. Copia o seu arquivo .jar que o Maven gerou para dentro do contêiner, renomeando para app.jar
COPY target/*.jar app.jar

# 4. Avisa que essa máquina vai usar a porta 8080
EXPOSE 8080

# 5. O comando que o contêiner deve rodar quando ligar (java -jar app.jar)
ENTRYPOINT ["java", "-jar", "app.jar"]