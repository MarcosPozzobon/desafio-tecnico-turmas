package com.marcos.desenvolvimento.desafio_tecnico.repository;

import com.marcos.desenvolvimento.desafio_tecnico.config.DataSourceConfig;
import com.marcos.desenvolvimento.desafio_tecnico.response.CursoResponse;
import com.marcos.desenvolvimento.desafio_tecnico.response.FullResultSetTurmaResponse;
import com.marcos.desenvolvimento.desafio_tecnico.response.TurmaInformacoesBasicasResponse;
import com.marcos.desenvolvimento.desafio_tecnico.response.TurmaParticipanteResponse;
import com.marcos.desenvolvimento.desafio_tecnico.response.TurmaResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DAOTurmas {

    private static final Logger LOGGER = LoggerFactory.getLogger(DAOTurmas.class);

    private final JdbcTemplate jdbcTemplate;

    private final DataSourceConfig dataSourceConfig;
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DAOTurmas(JdbcTemplate jdbcTemplate, DataSourceConfig dataSourceConfig){
        this.jdbcTemplate = jdbcTemplate;
        this.dataSourceConfig = dataSourceConfig;
    }

    public List<FullResultSetTurmaResponse> buscarTurmas(int paginacao) {

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<FullResultSetTurmaResponse> fullResultSetTurmaResponseList = new ArrayList<>();

        String consultaInformacaoCompletaTurmas = "SELECT \n" +
                "    curso.codigo_curso AS codigo_curso,\n" +
                "    curso.nome AS nome_curso,\n" +
                "    curso.duracao AS duracao_em_minutos,\n" +
                "    (SELECT COUNT(*) FROM turma WHERE turma.curso_id_fk = curso.codigo_curso) AS quantidade_turmas,\n" +
                "    ARRAY_TO_JSON(ARRAY_AGG(json_build_object(\n" +
                "        'turma', turma.codigo_turma, \n" +
                "        'data_inicio', turma.dt_inicio, \n" +
                "        'data_fim', turma.dt_fim, \n" +
                "        'local', turma.local,\n" +
                "        'quantidade_participantes', (\n" +
                "            SELECT COUNT(*) \n" +
                "            FROM turma_participante \n" +
                "            WHERE turma_participante.turma_id_fk = turma.codigo_turma\n" +
                "        ),\n" +
                "        'participantes', (SELECT ARRAY_TO_JSON(ARRAY_AGG(json_build_object(\n" +
                "            'codigo', turma_participante.codigo_turma_participante, \n" +
                "            'nome_funcionario', funcionario.nome\n" +
                "        )))\n" +
                "        FROM turma_participante\n" +
                "        LEFT JOIN funcionario ON turma_participante.funcionario_id_fk = funcionario.codigo_funcionario\n" +
                "        WHERE turma_participante.turma_id_fk = turma.codigo_turma\n" +
                "        )\n" +
                "    ))) AS turmas\n" +
                "FROM \n" +
                "    curso\n" +
                "LEFT JOIN turma ON turma.curso_id_fk = curso.codigo_curso\n" +
                "GROUP BY \n" +
                "    curso.codigo_curso, curso.nome, curso.duracao\n" +
                "ORDER BY curso.nome\n" +
                "LIMIT ? OFFSET 0";

        try {
            preparedStatement = dataSourceConfig.dataSource().getConnection().prepareStatement(consultaInformacaoCompletaTurmas);
            preparedStatement.setInt(1, paginacao);

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

                        String dataInicio = turmaAtualJson.optString("data_inicio", "");
                        String dataFim = turmaAtualJson.optString("data_fim", "");
                        if (!dataInicio.isEmpty()) {
                            turma.setDataInicio(LocalDate.parse(dataInicio, formatter));
                        }
                        if (!dataFim.isEmpty()) {
                            turma.setDataFim(LocalDate.parse(dataFim, formatter));
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
    	TurmaInformacoesBasicasResponse turmaInformacoesBasicasAtual = new TurmaInformacoesBasicasResponse();
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
}
