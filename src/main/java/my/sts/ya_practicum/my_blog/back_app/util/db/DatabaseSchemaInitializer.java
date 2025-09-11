package my.sts.ya_practicum.my_blog.back_app.util.db;

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
    private final Resource schemaScript;

    public DatabaseSchemaInitializer(DataSource dataSource,
                                     @Value("classpath:schema.sql") Resource schemaScript) {
        this.dataSource = dataSource;
        this.schemaScript = schemaScript;
    }

    @EventListener
    public void populate(ContextRefreshedEvent event) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();

        populator.addScript(schemaScript);
        populator.execute(dataSource);
    }
}
