package com.marcos.desenvolvimento.desafio_tecnico.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcos.desenvolvimento.desafio_tecnico.config.DataSourceConfig;
import com.marcos.desenvolvimento.desafio_tecnico.request.TurmaAtualizacaoRequest;
import com.marcos.desenvolvimento.desafio_tecnico.request.TurmaRequest;
import com.marcos.desenvolvimento.desafio_tecnico.response.CursoResponse;
import com.marcos.desenvolvimento.desafio_tecnico.response.FullResultSetTurmaResponse;
import com.marcos.desenvolvimento.desafio_tecnico.response.TurmaInformacoesBasicasResponse;
import com.marcos.desenvolvimento.desafio_tecnico.response.TurmaParticipanteResponse;
import com.marcos.desenvolvimento.desafio_tecnico.response.TurmaResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class DAOTurmas {

    private static final Logger LOGGER = LoggerFactory.getLogger(DAOTurmas.class);
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final DataSourceConfig dataSourceConfig;
    
    private final ObjectMapper objectMapper;


    public DAOTurmas(DataSourceConfig dataSourceConfig, ObjectMapper objectMapper){
        this.dataSourceConfig = dataSourceConfig;
        this.objectMapper = objectMapper;
    }

    public List<FullResultSetTurmaResponse> buscarTurmas(String dataInicialFitro, String dataFinalFitro, int paginacao) {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<FullResultSetTurmaResponse> fullResultSetTurmaResponseList = new ArrayList<>();

        String consultaInformacaoCompletaTurmas = "SELECT \r\n"
        		+ "    curso.codigo_curso AS codigo_curso,\r\n"
        		+ "    curso.nome AS nome_curso,\r\n"
        		+ "    curso.duracao AS duracao_em_minutos,\r\n"
        		+ "    (SELECT COUNT(*) FROM turma WHERE turma.curso_id_fk = curso.codigo_curso) AS quantidade_turmas,\r\n"
        		+ "    ARRAY_TO_JSON(ARRAY_AGG(json_build_object(\r\n"
        		+ "        'turma', turma.codigo_turma, \r\n"
        		+ "        'data_inicio', turma.dt_inicio, \r\n"
        		+ "        'data_fim', turma.dt_fim, \r\n"
        		+ "        'local', turma.local,\r\n"
        		+ "        'quantidade_participantes', (\r\n"
        		+ "            SELECT COUNT(*) \r\n"
        		+ "            FROM turma_participante \r\n"
        		+ "            WHERE turma_participante.turma_id_fk = turma.codigo_turma\r\n"
        		+ "        ),\r\n"
        		+ "        'participantes', (\r\n"
        		+ "            SELECT ARRAY_TO_JSON(ARRAY_AGG(json_build_object(\r\n"
        		+ "                'codigo', turma_participante.codigo_turma_participante, \r\n"
        		+ "                'nome_funcionario', funcionario.nome\r\n"
        		+ "            )))\r\n"
        		+ "            FROM turma_participante\r\n"
        		+ "            LEFT JOIN funcionario ON turma_participante.funcionario_id_fk = funcionario.codigo_funcionario\r\n"
        		+ "            WHERE turma_participante.turma_id_fk = turma.codigo_turma \r\n"
        		+ "        )\r\n"
        		+ "    ))) AS turmas\r\n"
        		+ "FROM \r\n"
        		+ "    curso\r\n"
        		+ "LEFT JOIN turma ON turma.curso_id_fk = curso.codigo_curso\r\n"
        		+ "WHERE turma.dt_inicio BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD')\r\n"
        		+ "GROUP BY \r\n"
        		+ "    curso.codigo_curso, curso.nome, curso.duracao\r\n"
        		+ "ORDER BY curso.nome\r\n"
        		+ "LIMIT ? OFFSET 0";
        
        try {
            preparedStatement = dataSourceConfig.dataSource().getConnection().prepareStatement(consultaInformacaoCompletaTurmas);

            preparedStatement.setString(1, dataInicialFitro);
            preparedStatement.setString(2, dataFinalFitro);
            preparedStatement.setInt(3, paginacao);
            

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                List<TurmaResponse> listaDeTurmas = new ArrayList<>();
                FullResultSetTurmaResponse fullResultSetTurmaResponse = new FullResultSetTurmaResponse();

                String jsonTurmas = resultSet.getString("turmas");
                if (jsonTurmas != null && !jsonTurmas.isEmpty()) {
                    JSONArray jsonInformacaoTurmas = new JSONArray(jsonTurmas);
                    for (int i = 0; i < jsonInformacaoTurmas.length(); i++) {
                        JSONObject turmaAtualJson = jsonInformacaoTurmas.getJSONObject(i);

                        int codigoTurma = turmaAtualJson.optInt("turma", 0);
                        if (codigoTurma == 0) {
                            continue; // caso a gente nao ache turmas, definimos um padrao valor como zero p/ elas e ignoramos com esse continue;
                        }

                        TurmaResponse turma = new TurmaResponse();
                        turma.setCodigoTurma(codigoTurma);

                        String dataInicial = turmaAtualJson.getString("data_inicio");
                        String dataFinal = turmaAtualJson.getString("data_fim");

                        if (!dataInicial.isEmpty()) {
                            turma.setDataInicio(LocalDate.parse(dataInicial, formatter));
                        }
                        if (!dataFinal.isEmpty()) {
                            turma.setDataFim(LocalDate.parse(dataFinal, formatter));
                        }

                        turma.setLocal(turmaAtualJson.optString("local", ""));
                        turma.setQuantidadeParticipantes(turmaAtualJson.optInt("quantidade_participantes", 0));

                        String jsonParticipantes = turmaAtualJson.optString("participantes", null);
                        if (jsonParticipantes != null && !jsonParticipantes.isEmpty()) {
                            List<TurmaParticipanteResponse> listaDeParticipantes = getParticipanteResponses(jsonParticipantes);
                            turma.setParticipantes(listaDeParticipantes);
                        }

                        listaDeTurmas.add(turma);
                    }
                }

                if (!listaDeTurmas.isEmpty()) {
                    fullResultSetTurmaResponse.setTurmas(listaDeTurmas);
                } else {
                    fullResultSetTurmaResponse.setTurmas(new ArrayList<>());
                }

                fullResultSetTurmaResponse.setCodigoCurso(resultSet.getInt("codigo_curso"));
                fullResultSetTurmaResponse.setNomeCurso(resultSet.getString("nome_curso"));
                fullResultSetTurmaResponse.setDuracao(resultSet.getInt("duracao_em_minutos"));
                fullResultSetTurmaResponse.setQuantidadeTurmas(resultSet.getInt("quantidade_turmas"));

                fullResultSetTurmaResponseList.add(fullResultSetTurmaResponse);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.debug("Erro: " + e.getMessage() + " SQL usado para a consulta: " + consultaInformacaoCompletaTurmas + " argumento passado para o SQL: " + paginacao);
            }
        }
        return fullResultSetTurmaResponseList;
    }

    private static List<TurmaParticipanteResponse> getParticipanteResponses(String jsonParticipantes) {
        JSONArray jsonInformacaoParticipantes = new JSONArray(jsonParticipantes);
        List<TurmaParticipanteResponse> listaDeParticipantes = new ArrayList<>();
        for (int j = 0; j < jsonInformacaoParticipantes.length(); j++) {
            TurmaParticipanteResponse participanteAtual = new TurmaParticipanteResponse();
            JSONObject participanteAtualJson = jsonInformacaoParticipantes.optJSONObject(j);
            participanteAtual.setCodigoParticipanteTurma(participanteAtualJson.optInt("codigo", 0));
            participanteAtual.setNome(participanteAtualJson.optString("nome_funcionario", ""));
            listaDeParticipantes.add(participanteAtual);
        }
        return listaDeParticipantes;
    }
    
    public List<TurmaInformacoesBasicasResponse> listarTurmasInformacaoBasica(int paginacao){
    	
    	PreparedStatement preparedStatement = null;
    	ResultSet resultSet = null;
    	List<TurmaInformacoesBasicasResponse> listaDeTurmasInformacaoBasica = new ArrayList<TurmaInformacoesBasicasResponse>();
    	
    	String buscarInfoBasicaTurmas = "SELECT \r\n"
    			+ "    turma.dt_inicio AS data_inicial,\r\n"
    			+ "    turma.dt_fim AS data_final,\r\n"
    			+ "    turma.local AS local_das_aulas,\r\n"
    			+ "    curso.nome AS nome_curso,\r\n"
    			+ "    curso.descricao AS descricao,\r\n"
    			+ "    curso.duracao AS duracao_em_minutos,\r\n"
    			+ "	curso.is_ativo AS ativo\r\n"
    			+ "FROM turma\r\n"
    			+ "INNER JOIN curso ON curso.codigo_curso = turma.curso_id_fk\r\n"
    			+ "WHERE curso.is_ativo <> 'false'\r\n"
    			+ "ORDER BY curso.nome\r\n"
    			+ "LIMIT ? OFFSET 0";
    	try {
    		
    		preparedStatement = dataSourceConfig.dataSource().getConnection().prepareStatement(buscarInfoBasicaTurmas);
    		preparedStatement.setInt(1, paginacao);
    		resultSet = preparedStatement.executeQuery();
    		
    		while(resultSet.next()) {
    			
    			String dataInicialTurma = resultSet.getString("data_inicial");
    			String dataFinalTurma = resultSet.getString("data_final");

                TurmaInformacoesBasicasResponse turmaInformacoesBasicasAtual = new TurmaInformacoesBasicasResponse();

    			turmaInformacoesBasicasAtual.setDataInicio(LocalDate.parse(dataInicialTurma, formatter));
    			turmaInformacoesBasicasAtual.setDataFim(LocalDate.parse(dataFinalTurma, formatter));
    			turmaInformacoesBasicasAtual.setLocal(resultSet.getString("local_das_aulas"));
    			
    			CursoResponse cursoResponse = new CursoResponse();
    			cursoResponse.setNome(resultSet.getString("nome_curso"));
    			cursoResponse.setDescricao(resultSet.getString("descricao"));
    			cursoResponse.setDuracao(resultSet.getInt("duracao_em_minutos"));
    			cursoResponse.setIsAtivo(resultSet.getString("ativo"));
    			
    			turmaInformacoesBasicasAtual.setCursoAssociado(cursoResponse);
    			listaDeTurmasInformacaoBasica.add(turmaInformacoesBasicasAtual);
    		}

    		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.debug("Erro: " + e.getMessage() + " SQL usado para a consulta: " + buscarInfoBasicaTurmas + " argumento passado para o SQL: " + paginacao);
            }
        }
    	return listaDeTurmasInformacaoBasica;
    }
    
    public List<HashMap<String, Object>> listarTurmasVinculadas(int codigoCurso, int paginacao){
    	String consultaTurmasVinculadas = "SELECT \r\n"
    			+ "	turma.codigo_turma AS codigo_turma,\r\n"
    			+ "	turma.dt_inicio AS data_inicial,\r\n"
    			+ "	turma.dt_fim AS data_final,\r\n"
    			+ "	turma.local AS local_das_aulas,\r\n"
    			+ "	curso.nome AS nome_curso,\r\n"
    			+ "	curso.descricao AS descricao,\r\n"
    			+ "	curso.duracao AS duracao_em_minutos,\r\n"
    			+ "	curso.is_ativo AS ativo,\r\n"
    			+ "	(SELECT COUNT(*) FROM turma_participante WHERE turma_participante.turma_id_fk = turma.codigo_turma) AS qtd_participantes \r\n"
    			+ "FROM \r\n"
    			+ "	turma\r\n"
    			+ "INNER JOIN curso ON curso.codigo_curso = turma.curso_id_fk\r\n"
    			+ "WHERE \r\n"
    			+ "	curso.codigo_curso = ?\r\n"
    			+ "ORDER BY \r\n"
    			+ "	turma.dt_inicio\r\n"
    			+ "LIMIT ? OFFSET 0";
    	PreparedStatement preparedStatement = null;
    	ResultSet resultSet = null;
    	
    	List<HashMap<String, Object>> listaTurmasVinculadas = new ArrayList<HashMap<String,Object>>();
    	
    	try {
    		
    		preparedStatement = dataSourceConfig.dataSource().getConnection().prepareStatement(consultaTurmasVinculadas);
    		preparedStatement.setInt(1, codigoCurso);
    		preparedStatement.setInt(2, paginacao);
    		resultSet = preparedStatement.executeQuery();
    		
    		while(resultSet.next()) {
    			
    			HashMap<String, Object> finalJsonResponse = new HashMap<String, Object>();
    			
    			if(resultSet.getInt("codigo_turma") > 0) {
    				finalJsonResponse.put("codigo_turma", resultSet.getInt("codigo_turma"));
    			}
    			
    			String dataInicial = resultSet.getString("data_inicial");
    			String dataFinal = resultSet.getString("data_final");
    			
    			if(dataInicial != null && !dataInicial.isEmpty()) {
    				finalJsonResponse.put("data_inicial", LocalDate.parse(dataInicial, formatter));
    			} else if(dataFinal != null && !dataFinal.isEmpty()) {
    				finalJsonResponse.put("data_final", LocalDate.parse(dataFinal, formatter));
    			}
    			
    			finalJsonResponse.put("local_das_aulas", resultSet.getString("local_das_aulas"));
    			finalJsonResponse.put("nome_curso", resultSet.getString("nome_curso"));
    			finalJsonResponse.put("duracao_em_minutos", resultSet.getInt("duracao_em_minutos"));
    			finalJsonResponse.put("ativo", Boolean.parseBoolean(resultSet.getString("ativo")));
    			finalJsonResponse.put("quantidade_participantes", resultSet.getInt("qtd_participantes"));
    			
    			listaTurmasVinculadas.add(finalJsonResponse);
    			
    		}
    		
    	}catch (Exception e) {
    		e.printStackTrace();
		} finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.debug("Erro: " + e.getMessage() + " SQL usado para a consulta: " + consultaTurmasVinculadas + " argumento passado para o SQL: " + paginacao);
            }
        }
    	return listaTurmasVinculadas;
    }
    
    public boolean isVinculadaAUmCurso(int codigoCurso) {
    	String verificarVinculoEntreTurmaXCurso = "SELECT * FROM turma\r\n"
    			+ "INNER JOIN curso ON codigo_curso = curso_id_fk\r\n"
    			+ "WHERE codigo_curso = ?";
    	
    	PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
    	try {
    		
    		preparedStatement = dataSourceConfig.dataSource().getConnection().prepareStatement(verificarVinculoEntreTurmaXCurso);
    		preparedStatement.setInt(1, codigoCurso);
    		
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
		}finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.debug("Erro: " + e.getMessage() + " SQL usado para a consulta: " + verificarVinculoEntreTurmaXCurso + " argumento passado para o SQL: " + codigoCurso);
            }
        }
    	return false;
    }
    
    public boolean participantesVinculadosTurma(int codigoCurso) {
    	String verificaVinculoTurmaXParticipante = "SELECT * FROM turma_participante\r\n"
    			+ "INNER JOIN turma ON codigo_turma = turma_id_fk\r\n"
    			+ "WHERE turma_id_fk = ?";
    	PreparedStatement preparedStatement = null;
    	ResultSet resultSet = null;
    	try {
			
    		preparedStatement = dataSourceConfig.dataSource().getConnection().prepareStatement(verificaVinculoTurmaXParticipante);
    		preparedStatement.setInt(1, codigoCurso);
    		
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
		}
    	finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.debug("Erro: " + e.getMessage() + " SQL usado para a consulta: " + verificaVinculoTurmaXParticipante + " argumento passado para o SQL: " + codigoCurso);
            }
        }
    	return false;
    }
    
    @Transactional
    public HashMap<String, Object> deletarTurma(int codigoTurma) {
    	
        HashMap<String, Object> finalJsonResponse = new HashMap<>();
        String deletarTurmaPermanentemente = "DELETE FROM turma WHERE codigo_turma = ?";

        try (PreparedStatement preparedStatement = dataSourceConfig.dataSource().getConnection().prepareStatement(deletarTurmaPermanentemente)) {
        	
            if (isVinculadaAUmCurso(codigoTurma)) {

                // verifica se n existem participantes vinculados a turma
                if (!participantesVinculadosTurma(codigoTurma)) {
                    preparedStatement.setInt(1, codigoTurma);
                    int contagemRegistrosAfetados = preparedStatement.executeUpdate();

                    // se uma turma for deletada com sucesso, retorna a resposta (isso deve deletar apenas 1 turma)
                    if (contagemRegistrosAfetados == 1) {
                        finalJsonResponse.put("turma_deletada", codigoTurma);
                        finalJsonResponse.put("horario_remocao", LocalDateTime.now());
                    } else {
                        finalJsonResponse.put("status", 400);
                        finalJsonResponse.put("timestamp", LocalDateTime.now());
                        finalJsonResponse.put("msg", "Nenhuma turma encontrada com o código fornecido: " + codigoTurma);
                    }

                } else {
                    finalJsonResponse.put("timestamp", LocalDateTime.now());
                    finalJsonResponse.put("status", 400);
                    finalJsonResponse.put("turma_fornecida", codigoTurma);
                    finalJsonResponse.put("msg", "Não é possível deletar a turma, pois há participantes vinculados a ela.");
                }

            } else {
                finalJsonResponse.put("erro", "O código fornecido não faz referência a nenhum curso. Código passado: " + codigoTurma);
            }

        } catch (SQLException e) {
            LOGGER.error("Erro ao tentar deletar a turma com código {}: {}", codigoTurma, e.getMessage(), e);
            finalJsonResponse.put("erro", "Erro ao deletar a turma: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Erro inesperado: {}", e.getMessage(), e);
            finalJsonResponse.put("erro", "Erro inesperado: " + e.getMessage());
        }

        return finalJsonResponse;
    }
    
    @Transactional
    public void cadastrarTurma(TurmaRequest turmaJsonRequest) {
    	
    	String sqlInclusaoNovaTurma = "INSERT INTO turma (dt_inicio, dt_fim, local, curso_id_fk) VALUES (?, ?, ?, ?)";
    	
    	try(PreparedStatement preparedStatement = dataSourceConfig.dataSource().getConnection().prepareStatement(sqlInclusaoNovaTurma)){

    		if (turmaJsonRequest.getDtInicio() != null) {
                preparedStatement.setDate(1, Date.valueOf(turmaJsonRequest.getDtInicio())); 
            } else {
                preparedStatement.setNull(1, java.sql.Types.DATE); 
            }

            if (turmaJsonRequest.getDtFim() != null) {
                preparedStatement.setDate(2, Date.valueOf(turmaJsonRequest.getDtFim())); 
            } else {
                preparedStatement.setNull(2, java.sql.Types.DATE); 
            }

            if (turmaJsonRequest.getLocal() != null && !turmaJsonRequest.getLocal().isEmpty()) {
                preparedStatement.setString(3, turmaJsonRequest.getLocal());
            } else {
                preparedStatement.setNull(3, java.sql.Types.VARCHAR); 
            }

            if (turmaJsonRequest.getCursoIdFk() > 0) {
                preparedStatement.setInt(4, turmaJsonRequest.getCursoIdFk());
            } else {
                preparedStatement.setNull(4, java.sql.Types.INTEGER); 
            }
    		
    		int linhasAfetadas = preparedStatement.executeUpdate();
    		
    		if(linhasAfetadas == 1) {
    			LOGGER.info("Realizado um insert na tabela de turma com os seguintes valores: " + objectMapper.writeValueAsString(turmaJsonRequest));
    		} else {
    			LOGGER.warn("Provavelmente alguma coisa deu errado. Validar o último insert realizado às: " + LocalDateTime.now());
    		}

    	}catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
    
    @Transactional
    public TurmaInformacoesBasicasResponse atualizarTurma(final TurmaAtualizacaoRequest turmaAtualizacaoJsonRequest, int codigoTurma) {
        String sqlAtualizarInformacoesTurma = "UPDATE turma SET dt_inicio = ?, dt_fim = ?, local = ? WHERE codigo_turma = ?";
        TurmaInformacoesBasicasResponse finalJsonTurmaInformacaoBasicaResponse = new TurmaInformacoesBasicasResponse();

        try (PreparedStatement preparedStatement = dataSourceConfig.dataSource().getConnection().prepareStatement(sqlAtualizarInformacoesTurma)) {

            if (turmaAtualizacaoJsonRequest.getDtInicio() != null) {     
                preparedStatement.setDate(1, Date.valueOf(turmaAtualizacaoJsonRequest.getDtInicio())); 
                finalJsonTurmaInformacaoBasicaResponse.setDataInicio(turmaAtualizacaoJsonRequest.getDtInicio());
            } else {
                preparedStatement.setNull(1, java.sql.Types.DATE); 
            }

            if (turmaAtualizacaoJsonRequest.getDtFim() != null) {
                preparedStatement.setDate(2, Date.valueOf(turmaAtualizacaoJsonRequest.getDtFim()));
                finalJsonTurmaInformacaoBasicaResponse.setDataFim(turmaAtualizacaoJsonRequest.getDtFim());
            } else {
                preparedStatement.setNull(2, java.sql.Types.DATE); 
            }

            if (turmaAtualizacaoJsonRequest.getLocal() != null && !turmaAtualizacaoJsonRequest.getLocal().isEmpty()) {
                preparedStatement.setString(3, turmaAtualizacaoJsonRequest.getLocal());
                finalJsonTurmaInformacaoBasicaResponse.setLocal(turmaAtualizacaoJsonRequest.getLocal());
            } else {
                preparedStatement.setNull(3, java.sql.Types.VARCHAR); 
            }

            if (codigoTurma > 0) {
                preparedStatement.setInt(4, codigoTurma);
            } else {
                LOGGER.warn("Código da turma inválido: " + codigoTurma);
                throw new IllegalArgumentException("Código da turma deve ser maior que zero.");
            }

            int linhasAfetadas = preparedStatement.executeUpdate();
            
            if (linhasAfetadas == 1) {
                LOGGER.info("A turma de código: " + codigoTurma + " foi alterada em: " + LocalDate.now());
                return finalJsonTurmaInformacaoBasicaResponse;
            } else {
                LOGGER.warn("Mais registros foram afetados indevidamente. Verificar o código afetado no banco de dados! Código: " + codigoTurma);
            }
            
        } catch (SQLException e) {
            LOGGER.error("Erro ao atualizar turma. Mensagem: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao atualizar turma.", e);
        }
        return null;
    }
    
}
