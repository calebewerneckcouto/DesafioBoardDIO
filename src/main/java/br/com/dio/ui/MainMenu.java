package br.com.dio.ui;

import static br.com.dio.persistence.config.ConnectionConfig.getConnection;
import static br.com.dio.persistence.entity.BoardColumnKindEnum.CANCEL;
import static br.com.dio.persistence.entity.BoardColumnKindEnum.FINAL;
import static br.com.dio.persistence.entity.BoardColumnKindEnum.INITIAL;
import static br.com.dio.persistence.entity.BoardColumnKindEnum.PENDING;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import br.com.dio.persistence.entity.BoardColumnEntity;
import br.com.dio.persistence.entity.BoardColumnKindEnum;
import br.com.dio.persistence.entity.BoardEntity;
import br.com.dio.service.BoardQueryService;
import br.com.dio.service.BoardService;

public class MainMenu {

    private final Scanner scanner = new Scanner(System.in);

    public void execute() throws SQLException {
        System.out.println("Bem-vindo ao gerenciador de boards!");

        int option;

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1 - Criar um novo board");
            System.out.println("2 - Selecionar um board existente");
            System.out.println("3 - Excluir um board");
            System.out.println("4 - Sair");

            try {
                System.out.print("Digite a opção: ");
                option = scanner.nextInt();
                scanner.nextLine(); // consome a quebra de linha
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, digite um número inteiro.");
                scanner.nextLine(); // limpa entrada inválida
                continue;
            }

            switch (option) {
                case 1 -> createBoard();
                case 2 -> selectBoard();
                case 3 -> deleteBoard();
                case 4 -> {
                    System.out.println("Encerrando o programa. Até logo!");
                    System.exit(0);
                }
                default -> System.out.println("Opção inválida, informe uma opção do menu.");
            }
        }
    }

    private void createBoard() throws SQLException {
        var entity = new BoardEntity();

        System.out.println("\nInforme o nome do seu board:");
        String boardName = scanner.nextLine();
        entity.setName(boardName);

        int additionalColumns = 0;
        while (true) {
            try {
                System.out.println("Seu board terá colunas além das 3 padrões? Se sim, informe quantas. Caso contrário, digite '0':");
                additionalColumns = scanner.nextInt();
                scanner.nextLine(); // consome a quebra de linha
                if (additionalColumns < 0) {
                    System.out.println("Número inválido. Digite 0 ou mais.");
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Por favor, digite um número inteiro.");
                scanner.nextLine(); // limpa entrada inválida
            }
        }

        List<BoardColumnEntity> columns = new ArrayList<>();

        System.out.println("Informe o nome da coluna inicial do board:");
        String initialColumnName = scanner.nextLine();
        columns.add(createColumn(initialColumnName, INITIAL, 0));

        for (int i = 0; i < additionalColumns; i++) {
            System.out.printf("Informe o nome da coluna de tarefa pendente %d:%n", i + 1);
            String pendingColumnName = scanner.nextLine();
            columns.add(createColumn(pendingColumnName, PENDING, i + 1));
        }

        System.out.println("Informe o nome da coluna final:");
        String finalColumnName = scanner.nextLine();
        columns.add(createColumn(finalColumnName, FINAL, additionalColumns + 1));

        System.out.println("Informe o nome da coluna de cancelamento:");
        String cancelColumnName = scanner.nextLine();
        columns.add(createColumn(cancelColumnName, CANCEL, additionalColumns + 2));

        entity.setBoardColumns(columns);

        try (var connection = getConnection()) {
            var service = new BoardService(connection);
            service.insert(entity);
            System.out.println("✅ Board criado com sucesso!");
        }
    }

    private void selectBoard() throws SQLException {
        System.out.println("Informe o ID do board que deseja selecionar:");
        long idInput = scanner.nextLong();
        scanner.nextLine(); // consome a quebra de linha

        final long id = idInput; // Agora 'id' é final

        try (var connection = getConnection()) {
            var queryService = new BoardQueryService(connection);
            var optional = queryService.findById(id);

            optional.ifPresentOrElse(
                board -> new BoardMenu(board).execute(),
                () -> System.out.printf("Não foi encontrado um board com ID %d%n", id)
            );
        }
    }


    private void deleteBoard() throws SQLException {
        long id;

        while (true) {
            try {
                System.out.println("\nInforme o ID do board que será excluído:");
                id = scanner.nextLong();
                scanner.nextLine(); // consome a quebra de linha
                break;
            } catch (InputMismatchException e) {
                System.out.println("Por favor, digite um número válido.");
                scanner.nextLine(); // limpa entrada inválida
            }
        }

        try (var connection = getConnection()) {
            var service = new BoardService(connection);
            if (service.delete(id)) {
                System.out.printf("✅ O board %d foi excluído com sucesso.%n", id);
            } else {
                System.out.printf("❌ Não foi encontrado um board com ID %d%n", id);
            }
        }
    }

    private BoardColumnEntity createColumn(String name, BoardColumnKindEnum kind, int order) {
        var column = new BoardColumnEntity();
        column.setName(name);
        column.setKind(kind);
        column.setOrder(order);
        return column;
    }
}
