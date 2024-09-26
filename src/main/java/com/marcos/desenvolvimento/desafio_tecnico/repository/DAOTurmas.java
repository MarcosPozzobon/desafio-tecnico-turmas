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

    public FullResultSetTurmaResponse buscarTurmas(int codigoCurso, int resultados) {
        List<TurmaResponse> listaDeTurmas = new ArrayList<>();
        FullResultSetTurmaResponse fullResultSetTurmaResponse = new FullResultSetTurmaResponse();
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
                "WHERE\n" +
                "    curso.codigo_curso = ?\n" +
                "ORDER BY curso.nome\n" +
                "LIMIT ? OFFSET 0";

        try (Connection connection = dataSourceConfig.dataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(consultaInformacaoCompletaTurmas)) {

            preparedStatement.setInt(1, codigoCurso);
            preparedStatement.setInt(2, resultados);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    fullResultSetTurmaResponse.setCodigoCurso(resultSet.getInt("codigo_curso"));
                    fullResultSetTurmaResponse.setNomeCurso(resultSet.getString("nome_curso"));
                    fullResultSetTurmaResponse.setDuracao(resultSet.getInt("duracao_em_minutos"));
                    fullResultSetTurmaResponse.setQuantidadeTurmas(resultSet.getInt("quantidade_turmas"));

                    do {
                        TurmaResponse turmaResponse = new TurmaResponse();
                        turmaResponse.setCodigoTurma(resultSet.getInt("turma"));
                        turmaResponse.setDataInicio(resultSet.getDate("data_inicial"));
                        turmaResponse.setDataFim(resultSet.getDate("data_final"));
                        turmaResponse.setLocal(resultSet.getString("local"));
                        turmaResponse.setQuantidadeParticipantes(resultSet.getInt("quantidade_participantes"));

                        List<TurmaParticipanteResponse> listaDeParticipantes = new ArrayList<>();

                        do {
                            if (resultSet.getInt("codigo") != 0) {  // Verifique se existe um participante
                                TurmaParticipanteResponse turmaParticipanteResponse = new TurmaParticipanteResponse();
                                turmaParticipanteResponse.setCodigoParticipanteTurma(resultSet.getInt("codigo"));
                                turmaParticipanteResponse.setNome(resultSet.getString("nome_funcionario"));
                                listaDeParticipantes.add(turmaParticipanteResponse);
                            }
                        } while (resultSet.next() && resultSet.getInt("turma") == turmaResponse.getCodigoTurma());

                        turmaResponse.setParticipantes(listaDeParticipantes);
                        listaDeTurmas.add(turmaResponse);

                    } while (resultSet.next());
                }
            }
            fullResultSetTurmaResponse.setTurmas(listaDeTurmas);
        } catch (SQLException e) {
            throw new RuntimeException("Alguma coisa deu errado em " + this.getClass().getName(), e);
        }

        return fullResultSetTurmaResponse;
    }
}
