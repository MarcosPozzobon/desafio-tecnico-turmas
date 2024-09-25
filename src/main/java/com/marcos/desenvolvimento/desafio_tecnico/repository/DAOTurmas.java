package com.marcos.desenvolvimento.desafio_tecnico.repository;

import com.marcos.desenvolvimento.desafio_tecnico.response.FullResultSetTurmaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DAOTurmas {

    private static final Logger LOGGER = LoggerFactory.getLogger(DAOTurmas.class);

    private final JdbcTemplate jdbcTemplate;

    public DAOTurmas(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public FullResultSetTurmaResponse buscarTurmas(){
        try{

            String consultaTurmas = "";

        }catch (Exception e){
            throw new RuntimeException("Alguma coisa deu errado em " + this.getClass().getName(), e);
        }
        return null;
    }


}
