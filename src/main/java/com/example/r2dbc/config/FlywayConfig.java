package com.example.r2dbc.config;

import javax.annotation.PostConstruct;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {
  @Value("${app.datasource.flyway.url}")
  private String url;

  @Value("${app.datasource.flyway.username}")
  private String username;

  @Value("${app.datasource.flyway.password}")
  private String password;

  @PostConstruct
  public void migrate() {
    Flyway flyway =
        new Flyway(
            new FluentConfiguration()
                .defaultSchema("public")
                .dataSource(url, username, password)
                .cleanDisabled(true)
                .schemas("public")
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .outOfOrder(false));

    flyway.repair();
    flyway.migrate();
  }
}
