package com.marcos.desenvolvimento.desafio_tecnico.repository;

import com.marcos.desenvolvimento.desafio_tecnico.config.DataSourceConfig;
import com.marcos.desenvolvimento.desafio_tecnico.entity.Curso;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class DAOCurso {

    private static final Logger LOGGER = LoggerFactory.getLogger(DAOCurso.class);

    private final DataSourceConfig dataSourceConfig;

    private final JdbcTemplate jdbcTemplate;

    private final DAOTurmas daoTurmas;

    public DAOCurso(JdbcTemplate jdbcTemplate, DAOTurmas daoTurmas, DataSourceConfig dataSourceConfig) {
        this.jdbcTemplate = jdbcTemplate;
        this.daoTurmas = daoTurmas;
        this.dataSourceConfig =dataSourceConfig;
    }

    @Transactional
    public void salvarCurso(final Curso curso) {
        String queryInclusaoCurso = "INSERT INTO curso (nome, duracao, descricao, is_ativo) VALUES (?, ?, ?, ?)";
        String isAtivo = "true";
        try {
            jdbcTemplate.update(queryInclusaoCurso, curso.getNome(), curso.getDuracao(), curso.getDescricao(), isAtivo);
            LOGGER.info("Realizado um INSERT na tabela curso com os seguintes valores: nome={}, descricao={}, duracao={}, is_ativo={}",
                    curso.getNome(), curso.getDescricao(), curso.getDuracao(), isAtivo);
        } catch (Exception e) {
            LOGGER.error("Erro ao realizar um insert na tabela curso! Classe de erro: " + this.getClass().getName(), e);
        }
    }

    @Transactional
    public void atualizarNomeCurso(final Curso curso, int codigoCurso) {
        String queryAtualizacaoCurso = "UPDATE curso SET nome = ?, descricao = ?, duracao = ? WHERE codigo_curso = ?";
        try {
            jdbcTemplate.update(queryAtualizacaoCurso, curso.getNome(), curso.getDescricao(), curso.getDuracao(), codigoCurso);
            LOGGER.info("Realizado um UPDATE na tabela curso no campo nome com o seguinte valor: {} , " +
                    "passando o seguinte código: {}", curso.getNome(), codigoCurso);
        } catch (Exception e) {
            LOGGER.error("Erro ao realizar um update na tabela curso! Classe de erro: " + this.getClass().getName(), e);
            throw e;
        }
    }

    public List<Curso> listarTodosOsCursos() {
        String queryListaCursos = "SELECT * FROM curso;";
        try {
            List<Curso> cursos = jdbcTemplate.query(queryListaCursos, new BeanPropertyRowMapper<>(Curso.class));
            LOGGER.info("Realizado um SELECT * FROM na tabela curso no seguinte horário: " + LocalDateTime.now());
            return cursos;
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao realizar um SELECT * FROM na tabela cursos. Detalhes da exception: " + e.getMessage());
            throw e;
        }
    }

    @Transactional
    public void inativarCurso(int codigoCurso){
        String sql = "UPDATE curso SET is_ativo = 'false' WHERE codigo_curso = ?";
        try(PreparedStatement preparedStatement = dataSourceConfig.dataSource().getConnection().prepareStatement(sql)){

            preparedStatement.setInt(1, codigoCurso);
            int registrosAfetados = preparedStatement.executeUpdate();

            if(registrosAfetados == 1){
                LOGGER.info("O curso de ID: " + codigoCurso + " foi inativado com sucesso.");
            } else {
                LOGGER.warn("Alguma coisa nao saiu como o esperado. Verificar no banco de dados os registros referentes ao codigo: " + codigoCurso);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Transactional
    public void ativarCurso(int codigoCurso) {

        String sql = "UPDATE curso SET is_ativo = 'true' WHERE codigo_curso = ?";
        try(PreparedStatement preparedStatement = dataSourceConfig.dataSource().getConnection().prepareStatement(sql)){

            preparedStatement.setInt(1, codigoCurso);
            int registrosAfetados = preparedStatement.executeUpdate();

            if(registrosAfetados == 1){
                LOGGER.info("O curso de ID: " + codigoCurso + " foi ativado com sucesso.");
            } else {
                LOGGER.warn("Alguma coisa nao saiu como o esperado. Verificar no banco de dados os registros referentes ao codigo: " + codigoCurso);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public boolean isCursoInativado(int codigoCurso) {
        String sql = "SELECT is_ativo FROM curso WHERE codigo_curso = ?";
        ResultSet resultSet = null;

        try (PreparedStatement preparedStatement = dataSourceConfig.dataSource().getConnection().prepareStatement(sql)) {

            preparedStatement.setInt(1, codigoCurso);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return "false".equalsIgnoreCase(resultSet.getString("is_ativo"));
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    @Transactional
    public void deletarCurso(int codigoCurso){

        if(daoTurmas.isVinculadaAUmCurso(codigoCurso)){ // verifica se existe pelo menos 1 turma vinculada ao curso
            throw new UnsupportedOperationException("Você não pode deletar esse curso, pois existe pelo menos 1 turma vinculada e ele.");
        }

        if(!isCursoInativado(codigoCurso)){ // verifica se o curso esta inativado antes de deletar ele... do contrario, prossegue
            throw new UnsupportedOperationException("Favor desativar o curso antes de deletar ele! Completamente!");
        }

        String deletarCursoDefinitivo = "DELETE from curso WHERE codigo_curso = ?";

        try(PreparedStatement preparedStatement = dataSourceConfig.dataSource().getConnection().prepareStatement(deletarCursoDefinitivo)){

            preparedStatement.setInt(1, codigoCurso);
            int linhasAfetadas = preparedStatement.executeUpdate();

            if(linhasAfetadas == 1){
                LOGGER.info("O código de ID: " + codigoCurso + " foi removido completamente do banco de dados!");
            } else {
                LOGGER.warn("Alguma coisa deu errado com o código de curso: " + codigoCurso + " validar no banco de dados!");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void deleteTurmaParticipantes(int cursoId) {
        String sql = "DELETE FROM turma_participante WHERE turma_id_fk IN (SELECT codigo_turma FROM turma WHERE curso_id_fk = ?)";
        jdbcTemplate.update(sql, cursoId);
    }

    private void deleteTurmas(int cursoId) {
        String sql = "DELETE FROM turma WHERE curso_id_fk = ?";
        jdbcTemplate.update(sql, cursoId);
    }

    private void deleteCurso(int cursoId) {
        String sql = "DELETE FROM curso WHERE codigo_curso = ?";
        jdbcTemplate.update(sql, cursoId);
    }

    @Transactional
    public void deletarCursoComForce(int codigoCurso, String forceParametro) {
        if (forceParametro.equalsIgnoreCase("force")) {
            deleteTurmaParticipantes(codigoCurso);
            deleteTurmas(codigoCurso);
            deleteCurso(codigoCurso);
            LOGGER.info("Curso e registros relacionados foram removidos com sucesso!");
        } else {
            throw new UnsupportedOperationException("Parâmetro 'force' não fornecido. Não é possível deletar o curso.");
        }
    }

}
