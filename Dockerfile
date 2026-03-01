FROM gradle:8.14-jdk17 as build
WORKDIR /app
COPY . .
run gradle build --no-daemon

FROM azul/zulu-openjdk:17-latest

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/agendador-tarefas.jar

EXPOSE 8081

CMD ["java", "-jar", "/app/agendador-tarefas.jar"]

