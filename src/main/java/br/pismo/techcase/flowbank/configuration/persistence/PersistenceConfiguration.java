package br.pismo.techcase.flowbank.configuration.persistence;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = {"br.pismo.techcase.flowbank.adapters.out.persistence"})
@EntityScan(basePackages = {"br.pismo.techcase.flowbank.infrastructure.persistence.model"})
@EnableJpaRepositories(basePackages = {"br.pismo.techcase.flowbank.adapters.out.persistence"})
@EnableTransactionManagement
public class PersistenceConfiguration {

}
