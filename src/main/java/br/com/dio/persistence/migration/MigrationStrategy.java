package br.com.dio.persistence.migration;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;

public class MigrationStrategy {

    private Connection connection;

    public MigrationStrategy(Connection connection) {
        this.connection = connection;
    }

    public void executeMigration() {
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;

        try (FileOutputStream fos = new FileOutputStream("liquibase.log");
             PrintStream logStream = new PrintStream(fos)) {

            System.setOut(logStream);
            System.setErr(logStream);

            JdbcConnection jdbcConnection = new JdbcConnection(connection);
            Liquibase liquibase = new Liquibase(
                    "db/changelog/db.changelog-master.yml",
                    new ClassLoaderResourceAccessor(),
                    jdbcConnection
            );

            liquibase.update();

        } catch (LiquibaseException | IOException e) {
            e.printStackTrace(originalErr);
        } finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
        }
    }
}
