package com.demo.config.db

import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(
        basePackages = ["${Db2.PACKAGE_ROOT}.${Db2.DB_NAME}"],
        entityManagerFactoryRef = Db2.ENTITY_MANAGER,
        transactionManagerRef = Db2.TRANSACTION_MANAGER
)
class Db2 {

    companion object {
        const val PACKAGE_ROOT = "com.demo.dao"
        const val DB_NAME = "sample"
        const val MASTER_DATA_SOURCE = "$DB_NAME-master-data-source"
        const val ENTITY_MANAGER = "$DB_NAME-entity-manager"
        const val TRANSACTION_MANAGER = "$DB_NAME-transaction-manager"
    }

    @Bean(ENTITY_MANAGER)
    fun entityManager(@Qualifier(MASTER_DATA_SOURCE) dataSource: DataSource): LocalContainerEntityManagerFactoryBean {
        return LocalContainerEntityManagerFactoryBean().apply {
            this.dataSource = dataSource
            setPackagesToScan("$PACKAGE_ROOT.$DB_NAME.entity")
            jpaVendorAdapter = HibernateJpaVendorAdapter()
        }
    }

    @Bean(value = [MASTER_DATA_SOURCE], destroyMethod = "")
    @ConfigurationProperties(prefix = "db2.data-source.master")
    fun masterDataSource(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java).build()
    }

    @Bean(TRANSACTION_MANAGER)
    fun transactionManager(
            @Qualifier(ENTITY_MANAGER) entityManager: LocalContainerEntityManagerFactoryBean
    ): PlatformTransactionManager {
        return JpaTransactionManager().apply {
            entityManagerFactory = entityManager.`object`
        }
    }

}