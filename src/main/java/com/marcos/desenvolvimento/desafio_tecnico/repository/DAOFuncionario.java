package com.marcos.desenvolvimento.desafio_tecnico.repository;

import com.marcos.desenvolvimento.desafio_tecnico.config.DataSourceConfig;
import com.marcos.desenvolvimento.desafio_tecnico.entity.Funcionario;
import com.marcos.desenvolvimento.desafio_tecnico.request.FuncionarioRequest;
import com.marcos.desenvolvimento.desafio_tecnico.response.FuncionarioResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class DAOFuncionario {

    private static final Logger LOGGER = LoggerFactory.getLogger(DAOCurso.class);

    private final JdbcTemplate jdbcTemplate;
    
    private final DataSourceConfig dataSourceConfig;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DAOFuncionario(JdbcTemplate jdbcTemplate, DataSourceConfig dataSourceConfig){
        this.jdbcTemplate = jdbcTemplate;
        this.dataSourceConfig = dataSourceConfig;
    }

    @Transactional
    public void salvarFuncionario(final Funcionario funcionario) {
        String queryInclusaoFuncionario = "INSERT INTO funcionario (nome, cpf, dt_nascimento, cargo, dt_admissao, is_ativo) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(queryInclusaoFuncionario,
                    funcionario.getNome(),
                    funcionario.getCpf(),
                    funcionario.getDtNascimento(),
                    funcionario.getCargo(),
                    funcionario.getDtAdmissao(),
                    funcionario.getIsAtivo());

            LOGGER.info("Realizado um INSERT na tabela funcionario com os seguinte valores: nome={}, cpf={}, data de nascimento={}, cargo={}, data de admissão={} e is_ativo={}",
                    funcionario.getNome(),
                    funcionario.getCpf(),
                    funcionario.getDtNascimento(),
                    funcionario.getCargo(),
                    funcionario.getDtAdmissao(),
                    funcionario.getIsAtivo());
        } catch (Exception e) {
            LOGGER.error("Erro ao realizar um insert na tabela funcionario! Classe de erro: " + this.getClass().getName(), e);
        }
    }

    public boolean isFuncionarioAtualAtivo(int codigoFuncionario) {
        try {
            String queryVerificaFuncionarioAtivo = "SELECT COUNT(1) FROM funcionario WHERE codigo_funcionario = ? AND is_ativo = 'true'";
            Integer count = jdbcTemplate.queryForObject(queryVerificaFuncionarioAtivo, new Object[]{codigoFuncionario}, Integer.class);
            return count != null && count > 0;
        } catch (Exception e) {
            LOGGER.error("Erro ao verificar se o funcionário está ativo. Possível erro: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    public void atualizarFuncionario(FuncionarioRequest funcionarioRequest, int codigoFuncionario){

        String atualizarFuncionario = "UPDATE funcionario SET nome = ?, cpf = ?, dt_nascimento = ?, cargo = ?, is_ativo = ?, dt_admissao = ? WHERE codigo_funcionario = ?";
        try(PreparedStatement preparedStatement = dataSourceConfig.dataSource().getConnection().prepareStatement(atualizarFuncionario)){

            if(funcionarioRequest.getNome() != null && !funcionarioRequest.getNome().isEmpty()){
                preparedStatement.setString(1, funcionarioRequest.getNome());
            }

            if(funcionarioRequest.getCpf() != null && !funcionarioRequest.getCpf().isEmpty()){
                preparedStatement.setString(2, funcionarioRequest.getCpf());
            }

            if(funcionarioRequest.getDtNascimento() != null){
                preparedStatement.setDate(3, Date.valueOf(funcionarioRequest.getDtAdmissao()));
            }

            if(funcionarioRequest.getCargo() != null && !funcionarioRequest.getCargo().isEmpty()){
                preparedStatement.setString(4, funcionarioRequest.getCargo());
            }

            if(funcionarioRequest.getIsAtivo() != null && !funcionarioRequest.getIsAtivo().isEmpty()){
                preparedStatement.setString(5, funcionarioRequest.getIsAtivo());
            }

            if(funcionarioRequest.getDtNascimento() != null){
                preparedStatement.setDate(6, Date.valueOf(funcionarioRequest.getDtNascimento()));
            }

            if(codigoFuncionario > 0){
                preparedStatement.setInt(7, codigoFuncionario);
            }

            int registrosAfetados = preparedStatement.executeUpdate();

            if(registrosAfetados == 1){
                LOGGER.info("O funcionario de codigo: " + codigoFuncionario + " foi atualizado.");
            } else {
                LOGGER.warn("Alguma coisa estranha que nao deveria ter acontecido, aconteceu. Verificar no banco de dados.");
            }

        }catch (Exception exception){
            exception.printStackTrace();
        }


    }

    public List<Funcionario> buscarFuncionarioPorNome(String nomeFuncionario) {
        try {
            String buscaFuncionarioPorNome = "" +
                    "SELECT nome, cpf, dt_nascimento, cargo, dt_admissao FROM funcionario " +
                    "WHERE nome ILIKE ? AND is_ativo = 'true' ORDER BY nome";

            if (nomeFuncionario != null && !nomeFuncionario.isBlank()) {
                String nomeParam = "%" + nomeFuncionario + "%";

                List<Funcionario> funcionarioExistente = jdbcTemplate.query(
                        buscaFuncionarioPorNome,
                        new Object[]{nomeParam},
                        new BeanPropertyRowMapper<>(Funcionario.class)
                );
                return funcionarioExistente;
            }
        } catch (Exception e) {
            throw new RuntimeException("Alguma coisa deu errado em " + this.getClass().getName(), e);
        }
        throw new IllegalArgumentException("Argumento inválido: " + nomeFuncionario + " passado em :" + this.getClass().getName());
    }

    public List<Funcionario> buscarFuncionariosAtivos(int paginacao) {
        try {
            String buscaFuncionariosAtivos = "" +
                    "SELECT nome, cpf, dt_nascimento, cargo, dt_admissao FROM funcionario " +
                    "WHERE is_ativo = 'true' " +
                    "ORDER BY nome" +
                    " LIMIT ? OFFSET 0";

            List<Funcionario> funcionariosExistentes = jdbcTemplate.query(
                    buscaFuncionariosAtivos,
                    new Object[]{paginacao},
                    new BeanPropertyRowMapper<>(Funcionario.class)
            );
            return funcionariosExistentes;
        } catch (Exception e) {
            throw new RuntimeException("Alguma coisa deu errado em " + this.getClass().getName(), e);
        }
    }

    public List<Funcionario> buscarFuncionariosInativos(int paginacao) {
        try {
            String buscaFuncionariosAtivos = "" +
                    "SELECT nome, cpf, dt_nascimento, cargo, dt_admissao FROM funcionario " +
                    "WHERE is_ativo = 'false' " +
                    "ORDER BY nome" +
                    " LIMIT ? OFFSET 0";

            List<Funcionario> funcionariosExistentes = jdbcTemplate.query(
                    buscaFuncionariosAtivos,
                    new Object[]{paginacao},
                    new BeanPropertyRowMapper<>(Funcionario.class)
            );
                return funcionariosExistentes;
        } catch (Exception e) {
            throw new RuntimeException("Alguma coisa deu errado em " + this.getClass().getName(), e);
        }
    }
    
    public Funcionario buscarFuncionarioPorCodigo(int codigoFuncionario) {
    	String buscaFuncionarioPorCodigo = "SELECT * FROM funcionario\r\n"
    			+ "WHERE codigo_funcionario = ?";
    	PreparedStatement preparedStatement = null;
    	ResultSet resultSet = null;
    	
    	Funcionario funcionario = new Funcionario();
    	
    	try {
    		
    		preparedStatement = dataSourceConfig.dataSource().getConnection().prepareStatement(buscaFuncionarioPorCodigo);
    		preparedStatement.setInt(1, codigoFuncionario);
    		resultSet = preparedStatement.executeQuery();
    		
    		while(resultSet.next()) {
    			
    			funcionario.setCodigoFuncionario(resultSet.getInt("codigo_funcionario"));
    			funcionario.setNome(resultSet.getString("nome"));
    			funcionario.setCargo(resultSet.getString("cargo"));
    			funcionario.setCpf(resultSet.getString("cpf"));
    			funcionario.setDtNascimento(null);
    			funcionario.setDtAdmissao(null);
    			funcionario.setIsAtivo(resultSet.getString("is_ativo"));
    		}
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return funcionario;
    	
    }
}
