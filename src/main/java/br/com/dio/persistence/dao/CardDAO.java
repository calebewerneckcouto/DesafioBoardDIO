package br.com.dio.persistence.dao;

import static br.com.dio.persistence.converter.OffsetDateTimeConverter.toOffsetDateTime;
import static java.util.Objects.nonNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import br.com.dio.dto.CardDetailsDTO;
import br.com.dio.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CardDAO {

    private Connection connection;

    public CardDAO(Connection connection) {
        this.connection = connection;
    }

    public CardEntity insert(final CardEntity entity) throws SQLException {
        var sql = "INSERT INTO CARDS (title, description, board_column_id) VALUES (?, ?, ?) RETURNING id;";
        try (var statement = connection.prepareStatement(sql)) {
            var i = 1;
            statement.setString(i++, entity.getTitle());
            statement.setString(i++, entity.getDescription());
            statement.setLong(i, entity.getBoardColumn().getId());
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                entity.setId(resultSet.getLong("id"));
            }
        }
        return entity;
    }

    public void moveToColumn(final Long columnId, final Long cardId) throws SQLException {
        var sql = "UPDATE CARDS SET board_column_id = ? WHERE id = ?;";
        try (var statement = connection.prepareStatement(sql)) {
            var i = 1;
            statement.setLong(i++, columnId);
            statement.setLong(i, cardId);
            statement.executeUpdate();
        }
    }

    public Optional<CardDetailsDTO> findById(final Long id) throws SQLException {
        var sql =
                """
                SELECT c.id AS id,
                       c.title AS title,
                       c.description AS description,
                       b.blocked_at AS blocked_at,
                       b.block_reason AS block_reason,
                       c.board_column_id AS board_column_id,
                       bc.name AS column_name,
                       (SELECT COUNT(sub_b.id)
                          FROM BLOCKS sub_b
                         WHERE sub_b.card_id = c.id) AS blocks_amount
                  FROM CARDS c
             LEFT JOIN BLOCKS b
                    ON c.id = b.card_id
                   AND b.unblocked_at IS NULL
             INNER JOIN BOARDS_COLUMNS bc
                     ON bc.id = c.board_column_id
                 WHERE c.id = ?;
                """;

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    var dto = new CardDetailsDTO(
                            resultSet.getLong("id"),
                            resultSet.getString("title"),
                            resultSet.getString("description"),
                            nonNull(resultSet.getString("block_reason")),
                            toOffsetDateTime(resultSet.getTimestamp("blocked_at")),
                            resultSet.getString("block_reason"),
                            resultSet.getInt("blocks_amount"),
                            resultSet.getLong("board_column_id"),
                            resultSet.getString("column_name")
                    );
                    return Optional.of(dto);
                }
            }
        }

        return Optional.empty();
    }
}
