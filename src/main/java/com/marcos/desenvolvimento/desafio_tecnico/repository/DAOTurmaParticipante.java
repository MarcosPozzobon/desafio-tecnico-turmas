package com.marcos.desenvolvimento.desafio_tecnico.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

	public DAOTurmaParticipante(DataSourceConfig dataSourceConfig){
	        this.dataSourceConfig = dataSourceConfig;
	}
	
	public List<TurmaParticipanteResponse> listarTodosOsAlunos(int paginacao){
		
		String consultaListagemParticipantes = "SELECT \r\n"
				+ "	funcionario.nome AS nome_funcionario,\r\n"
				+ "	turma_participante.codigo_turma_participante AS codigo_funcionario_turma\r\n"
				+ "FROM \r\n"
				+ "	turma_participante\r\n"
				+ "INNER JOIN funcionario ON codigo_funcionario = funcionario_id_fk\r\n"
				+ "ORDER BY funcionario.nome\r\n"
				+ "LIMIT ? OFFSET 0";
		List<TurmaParticipanteResponse> listagemParticipantes = new ArrayList<TurmaParticipanteResponse>();
		
		try {
			
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			
			preparedStatement = dataSourceConfig.dataSource().getConnection().prepareStatement(consultaListagemParticipantes);
			preparedStatement.setInt(1, paginacao);
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				TurmaParticipanteResponse turmaParticipante = new TurmaParticipanteResponse();
				String nomeParticipante = resultSet.getString("nome_funcionario");
				int codigoParticipante = resultSet.getInt("codigo_funcionario_turma");
				
				if(nomeParticipante != null && !nomeParticipante.isEmpty()) {
					turmaParticipante.setNome(nomeParticipante);
				} else {
					turmaParticipante.setNome("");
				}
				
				if(codigoParticipante > 0) {
					turmaParticipante.setCodigoParticipanteTurma(codigoParticipante);
				} else {
					turmaParticipante.setCodigoParticipanteTurma(-1);
				}
				listagemParticipantes.add(turmaParticipante);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listagemParticipantes;

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
		}finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.debug("Erro: " + e.getMessage() + " SQL usado para a consulta: " + sqlRemoverParticipante);
            }
        }
    }
	
	public boolean existeRelacaoNaTabelaFuncionario(int codigoFuncionario) {
		String verificaRelacionamento = "SELECT * FROM funcionario WHERE codigo_funcionario = ?";
		ResultSet resultSet = null;
		try(PreparedStatement preparedStatement = dataSourceConfig.dataSource().getConnection().prepareStatement(verificaRelacionamento)){
			
			preparedStatement.setInt(1, codigoFuncionario);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				return true;
			}
			
		}catch (Exception e) {
			LOGGER.error("Erro no metodo de validacao de relacionamento entre turma participante x funcionario: " + e);
		}
		return false;
	}
	
	@Transactional
	public void cadastrarParticipante(int codigoTurmaVinculada, int codigoFuncionario) {

		String sqlInserirNovoParticipanteString = "INSERT INTO turma_participante (turma_id_fk, funcionario_id_fk) VALUES (?, ?)";
		
		try(PreparedStatement preparedStatement = dataSourceConfig.dataSource().getConnection().prepareStatement(sqlInserirNovoParticipanteString)){
			
			if(codigoFuncionario > 0 && codigoTurmaVinculada > 0) {
				preparedStatement.setInt(1, codigoTurmaVinculada);
				preparedStatement.setInt(2, codigoFuncionario);
			}
			
			int registrosAfetados = preparedStatement.executeUpdate();
			
			if(registrosAfetados == 1) {
				LOGGER.info("Inserido um novo participante: " + codigoFuncionario + " na turma: " + codigoTurmaVinculada);
			}
			
		}catch (Exception e) {
			LOGGER.error("Alguma coisa deu errado ao inserir o participante! Log de erro: " + e);
		}
		
	}

}
