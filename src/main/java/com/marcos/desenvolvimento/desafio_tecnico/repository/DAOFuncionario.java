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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

@Repository
public class DAOFuncionario {

    private static final Logger LOGGER = LoggerFactory.getLogger(DAOCurso.class);

    private final JdbcTemplate jdbcTemplate;
    
    private final DataSourceConfig dataSourceConfig;

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
                    funcionario.isAtivo());

            LOGGER.info("Realizado um INSERT na tabela funcionario com os seguinte valores: nome={}, cpf={}, data de nascimento={}, cargo={}, data de admissão={} e is_ativo={}",
                    funcionario.getNome(),
                    funcionario.getCpf(),
                    funcionario.getDtNascimento(),
                    funcionario.getCargo(),
                    funcionario.getDtAdmissao(),
                    funcionario.isAtivo());
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
        try{
            String atualizarFuncionario = "UPDATE funcionario SET nome = ?, cpf = ?, dt_nascimento = ?, cargo = ?, dt_admissao = ? WHERE codigo_funcionario = ?";
            jdbcTemplate.update(
                    atualizarFuncionario,
                    funcionarioRequest.getNome(),
                    funcionarioRequest.getCpf(),
                    funcionarioRequest.getDtNascimento(),
                    funcionarioRequest.getCargo(),
                    funcionarioRequest.getDtAdmissao(),
                    codigoFuncionario
            );
        }catch (Exception e){
            throw new RuntimeException("Alguma coisa deu errado em " + this.getClass().getName(), e);
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

    public List<Funcionario> buscarFuncionariosAtivos(int pagina, int tamanho) {
        try {
            String buscaFuncionariosAtivos = "" +
                    "SELECT nome, cpf, dt_nascimento, cargo, dt_admissao FROM funcionario " +
                    "WHERE is_ativo = 'true' " +
                    "ORDER BY nome" +
                    "LIMIT ? OFFSET ?";

            int offset = pagina * tamanho;

            List<Funcionario> funcionariosExistentes = jdbcTemplate.query(
                    buscaFuncionariosAtivos,
                    new Object[]{tamanho, offset},
                    new BeanPropertyRowMapper<>(Funcionario.class)
            );
            return funcionariosExistentes;
        } catch (Exception e) {
            throw new RuntimeException("Alguma coisa deu errado em " + this.getClass().getName(), e);
        }
    }

    public List<Funcionario> buscarFuncionariosInativos(int pagina, int tamanho) {
        try {
            String buscaFuncionariosAtivos = "" +
                    "SELECT nome, cpf, dt_nascimento, cargo, dt_admissao FROM funcionario " +
                    "WHERE is_ativo = 'false' " +
                    "ORDER BY nome" +
                    "LIMIT ? OFFSET ?";

            int offset = pagina * tamanho;

            List<Funcionario> funcionariosExistentes = jdbcTemplate.query(
                    buscaFuncionariosAtivos,
                    new Object[]{tamanho, offset},
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
    			funcionario.setIsAtivo(Boolean.parseBoolean(resultSet.getString("is_ativo")));
    		}
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return funcionario;
    	
    }
}
