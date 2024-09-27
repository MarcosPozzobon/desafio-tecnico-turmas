package com.marcos.desenvolvimento.desafio_tecnico.repository;

import com.marcos.desenvolvimento.desafio_tecnico.config.DataSourceConfig;
import com.marcos.desenvolvimento.desafio_tecnico.response.FullResultSetTurmaResponse;
import com.marcos.desenvolvimento.desafio_tecnico.response.TurmaParticipanteResponse;
import com.marcos.desenvolvimento.desafio_tecnico.response.TurmaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DAOTurmas {

    private static final Logger LOGGER = LoggerFactory.getLogger(DAOTurmas.class);

    private final JdbcTemplate jdbcTemplate;

    private final DataSourceConfig dataSourceConfig;

    public DAOTurmas(JdbcTemplate jdbcTemplate, DataSourceConfig dataSourceConfig){
        this.jdbcTemplate = jdbcTemplate;
        this.dataSourceConfig = dataSourceConfig;
    }

    public List<FullResultSetTurmaResponse> buscarTurmas(int paginacao) {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<FullResultSetTurmaResponse> fullResultSetTurmaResponseList = new ArrayList<>();

        String consultaInformacaoCompletaTurmas = "SELECT \n" +
                "    curso.codigo_curso AS codigo_curso,\n" +
                "    curso.nome AS nome_curso,\n" +
                "    curso.duracao AS duracao_em_minutos,\n" +
                "    (SELECT COUNT(*) FROM turma WHERE turma.curso_id_fk = curso.codigo_curso) AS quantidade_turmas,\n" +
                "    turma.codigo_turma AS turma,\n" +
                "    turma.dt_inicio AS data_inicial,\n" +
                "    turma.dt_fim AS data_final,\n" +
                "    turma.local AS local,\n" +
                "    (SELECT COUNT(*) FROM turma_participante WHERE turma_id_fk = turma.codigo_turma) AS quantidade_participantes,\n" +
                "    turma_participante.codigo_turma_participante AS codigo,\n" +
                "    funcionario.nome AS nome_funcionario\n" +
                "FROM \n" +
                "    curso\n" +
                "LEFT OUTER JOIN turma ON turma.curso_id_fk = curso.codigo_curso\n" +
                "LEFT OUTER JOIN turma_participante ON turma_participante.turma_id_fk = turma.codigo_turma\n" +
                "LEFT OUTER JOIN funcionario ON turma_participante.funcionario_id_fk = funcionario.codigo_funcionario\n" +
                "ORDER BY curso.nome\n" +
                "LIMIT ? OFFSET 0";

        try {
            preparedStatement = dataSourceConfig.dataSource().getConnection().prepareStatement(consultaInformacaoCompletaTurmas);
            preparedStatement.setInt(1, paginacao);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                List<TurmaParticipanteResponse> listaDeParticipantes = new ArrayList<>();

                TurmaParticipanteResponse participante = new TurmaParticipanteResponse();
                participante.setCodigoParticipanteTurma(resultSet.getInt("codigo"));
                participante.setNome(resultSet.getString("nome_funcionario"));
                listaDeParticipantes.add(participante);

                TurmaResponse turma = new TurmaResponse();
                turma.setCodigoTurma(resultSet.getInt("turma"));
                turma.setDataInicio(resultSet.getDate("data_inicial"));
                turma.setDataFim(resultSet.getDate("data_final"));
                turma.setLocal(resultSet.getString("local"));
                turma.setQuantidadeParticipantes(resultSet.getInt("quantidade_participantes"));
                turma.setParticipantes(listaDeParticipantes);

                FullResultSetTurmaResponse fullResultSetTurmaResponse = new FullResultSetTurmaResponse();
                fullResultSetTurmaResponse.setCodigoCurso(resultSet.getInt("codigo_curso"));
                fullResultSetTurmaResponse.setNomeCurso(resultSet.getString("nome_curso"));
                fullResultSetTurmaResponse.setDuracao(resultSet.getInt("duracao_em_minutos"));
                fullResultSetTurmaResponse.setQuantidadeTurmas(resultSet.getInt("quantidade_turmas"));
                fullResultSetTurmaResponse.setTurmas(List.of(turma));

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
            }
        }

        return fullResultSetTurmaResponseList;
    }

}
