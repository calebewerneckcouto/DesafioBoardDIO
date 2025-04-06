package br.com.dio.service;

import br.com.dio.dto.CardDetailsDTO;
import br.com.dio.persistence.dao.CardDAO;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class CardQueryService {

    private  Connection connection;

    public CardQueryService(Connection connection) {
		this.connection=connection;
	}

	public Optional<CardDetailsDTO> findById(final Long id) throws SQLException {
        var dao = new CardDAO(connection);
        return dao.findById(id);
    }

}
