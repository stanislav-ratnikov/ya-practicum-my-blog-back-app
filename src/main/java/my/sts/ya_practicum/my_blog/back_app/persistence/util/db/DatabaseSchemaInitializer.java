package my.sts.ya_practicum.my_blog.back_app.persistence.util.db;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DatabaseSchemaInitializer {

    private final DataSource dataSource;
    private final Resource ddlScript;
    private final Resource dmlScript;

    public DatabaseSchemaInitializer(@Value("classpath:db/ddl.sql") Resource ddlScript,
                                     @Value("classpath:db/dml.sql") Resource dmlScript,
                                     DataSource dataSource) {
        this.dataSource = dataSource;
        this.ddlScript = ddlScript;
        this.dmlScript = dmlScript;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void populateDatabase() {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();

        databasePopulator.addScripts(ddlScript, dmlScript);

        databasePopulator.execute(dataSource);
    }
}
