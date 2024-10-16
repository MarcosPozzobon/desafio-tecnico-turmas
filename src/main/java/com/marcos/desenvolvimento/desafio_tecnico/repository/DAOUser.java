package com.marcos.desenvolvimento.desafio_tecnico.repository;

import com.marcos.desenvolvimento.desafio_tecnico.config.DataSourceConfig;
import com.marcos.desenvolvimento.desafio_tecnico.entity.Role;
import com.marcos.desenvolvimento.desafio_tecnico.repository.dto.UsuarioDTO;
import com.marcos.desenvolvimento.desafio_tecnico.security.service.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Repository
public class DAOUser {

    private static final Logger LOGGER = LoggerFactory.getLogger(DAOUser.class);

    private final DataSourceConfig dataSourceConfig;

    private final JdbcTemplate jdbcTemplate;

    public DAOUser(DataSourceConfig dataSourceConfig, JdbcTemplate jdbcTemplate){
        this.dataSourceConfig = dataSourceConfig;
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserDetailsImpl obterUsuarioPorLogin(String login) {
        String buscaUsuarioPorId = "SELECT \n" +
                "    users.id AS user_id,\n" +
                "    users.login AS login,\n" +
                "    users.senha AS senha,\n" +
                "    funcionario.nome AS nome_funcionario,\n" +
                "    ARRAY_AGG(DISTINCT roles.role_desc) AS roles\n" +
                "FROM users\n" +
                "INNER JOIN usuario_acesso ON usuario_acesso.usuario_id_fk = users.id\n" +
                "INNER JOIN funcionario ON funcionario.codigo_funcionario = users.funcionario_id_fk\n" +
                "INNER JOIN roles ON roles.id = usuario_acesso.role_id_fk\n" +
                "WHERE users.login = ?\n" +
                "GROUP BY \n" +
                "    users.id,\n" +
                "    users.login,\n" +
                "    users.senha,\n" +
                "    funcionario.nome\n";
        try {
            UserDetailsImpl user = jdbcTemplate.queryForObject(
                    buscaUsuarioPorId,
                    new Object[]{login},
                    (rs, rowNum) -> {
                        UserDetailsImpl usuarioAcesso = new UserDetailsImpl();
                        usuarioAcesso.setId(rs.getInt("user_id"));

                        UsuarioDTO usuario = new UsuarioDTO();
                        usuario.setLogin(rs.getString("login"));
                        usuario.setSenha(rs.getString("senha"));

                        usuarioAcesso.setUser(usuario);

                        String[] rolesArray = (String[]) rs.getArray("roles").getArray();
                        Set<Role> rolesSet = new HashSet<>();
                        for (String roleDesc : rolesArray) {
                            Role role = new Role();
                            role.setRoleDesc(roleDesc);
                            rolesSet.add(role);
                        }
                        usuarioAcesso.setRoles(rolesSet); // Define o Set<Role> no UserDetailsImpl

                        usuario.setSenha(rs.getString("senha"));
                        return usuarioAcesso;
                    }
            );
            LOGGER.info("Realizado um SELECT na tabela users no seguinte horário: " + LocalDateTime.now());
            return user;
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao realizar um SELECT na tabela users. Detalhes da exception: " + e.getMessage());
            throw e;
        }
    }
}
