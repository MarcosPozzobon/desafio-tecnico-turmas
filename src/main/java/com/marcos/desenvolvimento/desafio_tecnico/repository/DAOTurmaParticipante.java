package com.marcos.desenvolvimento.desafio_tecnico.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.marcos.desenvolvimento.desafio_tecnico.config.DataSourceConfig;
import com.marcos.desenvolvimento.desafio_tecnico.response.TurmaParticipanteResponse;

@Repository
public class DAOTurmaParticipante {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DAOTurmaParticipante.class);

	private final DataSourceConfig dataSourceConfig;
	    
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

	public DAOTurmaParticipante(DataSourceConfig dataSourceConfig){
	        this.dataSourceConfig = dataSourceConfig;
	}
	
	public List<TurmaParticipanteResponse> listarTodosOsAlunos(int paginacao){
		
		String consultaString = "SELECT \r\n"
				+ "	funcionario.nome AS nome_funcionario,\r\n"
				+ "	turma_participante.codigo_turma_participante AS codigo_funcionario_turma\r\n"
				+ "FROM \r\n"
				+ "	turma_participante\r\n"
				+ "INNER JOIN funcionario ON codigo_funcionario = funcionario_id_fk\r\n"
				+ "ORDER BY funcionario.nome\r\n"
				+ "LIMIT 10 OFFSET 0";
		
		//TODO
		return null;

	}
	
	 public boolean existeParticipante(int codigoParticipanteTurma) {
	    	
	    	String verificaExistenciaParticipanteTurma = "SELECT * FROM turma_participante WHERE codigo_turma_participante = ?";
	    	
	    	PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			
	    	try {	
	    		preparedStatement = dataSourceConfig.dataSource().getConnection().prepareStatement(verificaExistenciaParticipanteTurma);
	    		preparedStatement.setInt(1, codigoParticipanteTurma);
	    		
	    		resultSet = preparedStatement.executeQuery();
	    		int contagemRegistrosEncontrados = 0;
	    		
	    		if(resultSet.next()) {
	    			contagemRegistrosEncontrados++;
	    		}
	    		
	    		if(contagemRegistrosEncontrados > 0) {
	    			return true;
	    		}
	    	
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
	            try {
	                if (resultSet != null) resultSet.close();
	                if (preparedStatement != null) preparedStatement.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	                LOGGER.debug("Erro: " + e.getMessage() + " SQL usado para a consulta: " + verificaExistenciaParticipanteTurma);
	            }
	        }
	    	LOGGER.info("O participante de código {} não foi encontrado!", codigoParticipanteTurma);
	    	return false;
	    }
	
	
	@Transactional
    public void removerParticipanteDaTurma(int codigoParticipanteTurma) {
    	
    	PreparedStatement preparedStatement = null;
    	String sqlRemoverParticipante = "DELETE FROM turma_participante WHERE codigo_turma_participante = ?";
    	
    	try {
    		
    		if(existeParticipante(codigoParticipanteTurma)) {
    			preparedStatement = dataSourceConfig.dataSource().getConnection().prepareStatement(sqlRemoverParticipante);
        		preparedStatement.setInt(1, codigoParticipanteTurma);
        		
        		int linhasAfetadas = preparedStatement.executeUpdate();
        		if (linhasAfetadas == 1) {
        			LOGGER.info("O participante de código {} foi excluído de sua respectiva turma.");
        		} else if (linhasAfetadas == 0) {
        			LOGGER.warn("Nenhum participante foi removido por algum motivo. Provavelmente o problema está no código fornecido.");
        		}
    		}
		} catch (Exception e) {
			LOGGER.error("Erro na classe " + this.getClass().getName() + " ao tentar realizar um DELETE na tabela turma_participante. Erro: " + e);
		}
    }

}
