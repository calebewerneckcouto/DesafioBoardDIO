# 🧠 Board - Sistema de Gerenciamento de Quadros

Este projeto é um sistema de gerenciamento de quadros no estilo **Kanban**, com suporte a colunas classificadas por tipo, cartões e controle de bloqueios/desbloqueios. É ideal para organizar tarefas de forma visual e adaptável.

---

## 🚀 Tecnologias Utilizadas

- ✅ Java 17+
- ✅ Maven
- ✅ Lombok
- ✅ Liquibase
- ✅ JDBC
- ✅ PostgreSQL (pode ser adaptado)
- 🧪 JUnit (para testes, em desenvolvimento)

---

## 📂 Estrutura do Projeto

```bash
src/
 └── main/
     ├── java/br/com/dio/persistence/
     │   ├── entity/
     │   │   ├── BoardEntity.java
     │   │   ├── BoardColumnEntity.java
     │   │   ├── BoardColumnKindEnum.java
     │   │   ├── CardEntity.java
     │   │   └── BlockEntity.java
     │   └── migration/
     │       └── MigrationStrategy.java
     └── resources/
         └── db/changelog/
             └── db.changelog-master.yml
