package br.com.dio.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static br.com.dio.persistence.entity.BoardColumnKindEnum.CANCEL;
import static br.com.dio.persistence.entity.BoardColumnKindEnum.INITIAL;

@Data
public class BoardEntity {

    private Long id;
    private String name;
    
    
    

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<BoardColumnEntity> getBoardColumns() {
		return boardColumns;
	}

	public void setBoardColumns(List<BoardColumnEntity> boardColumns) {
		this.boardColumns = boardColumns;
	}

	@ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<BoardColumnEntity> boardColumns = new ArrayList<>();

    /**
     * Retorna a coluna marcada como INITIAL
     * @return coluna inicial
     * @throws IllegalStateException se não encontrar
     */
    public BoardColumnEntity getInitialColumn() {
        return getFilteredColumn(bc -> INITIAL.equals(bc.getKind()), "INITIAL");
    }

    /**
     * Retorna a coluna marcada como CANCEL
     * @return coluna de cancelamento
     * @throws IllegalStateException se não encontrar
     */
    public BoardColumnEntity getCancelColumn() {
        return getFilteredColumn(bc -> CANCEL.equals(bc.getKind()), "CANCEL");
    }

    /**
     * Método genérico para buscar uma coluna baseada em um filtro
     * @param filter predicado que define a regra de busca
     * @param kindName nome do tipo buscado (para mensagem de erro)
     * @return coluna correspondente
     */
    private BoardColumnEntity getFilteredColumn(Predicate<BoardColumnEntity> filter, String kindName) {
        return boardColumns.stream()
                .filter(filter)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Coluna do tipo " + kindName + " não encontrada."));
    }
}
