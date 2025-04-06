plugins {
    id("java")
    id("eclipse") // Suporte ao comando `gradlew eclipse` no Eclipse
}

group = "br.com.dio"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Liquibase para controle de versões do banco de dados
    implementation("org.liquibase:liquibase-core:4.29.1")

    // Driver JDBC para PostgreSQL
    implementation("org.postgresql:postgresql:42.7.2")

    // Lombok (com annotation processor)
    implementation("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
}

tasks.test {
    useJUnitPlatform()
}
