package org.sds.elevateconnect.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class DbManagementService implements IDbManagementService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DataSource dataSource;

    @Override
    public void rebuildDatabase() {
        executeSqlScriptFromFileName("sql/rebuildDatabase.sql");
    }

    @Override
    public void insertSampleData() {
        executeSqlScriptFromFileName("sql/insertSampleData.sql");
    }

    private void executeSqlScriptFromFileName(String filename) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();

        populator.addScript(new ClassPathResource(filename));

        populator.execute(dataSource);
    }
}
