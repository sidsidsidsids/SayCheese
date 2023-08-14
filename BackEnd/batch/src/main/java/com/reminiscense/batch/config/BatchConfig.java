package com.reminiscense.batch.config;

import com.reminiscense.batch.service.FrameService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


@Configuration
@EnableScheduling
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final Environment env;
    @Bean
    public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        return launcher;
    }

    @Bean
    public JobRepository jobRepository(PlatformTransactionManager transactionManager)
            throws Exception {
        HikariConfig config =new HikariConfig();
        config.setJdbcUrl(env.getProperty("batch.jdbc.url"));
        config.setDriverClassName(env.getProperty("batch.jdbc.driver"));
        config.setUsername(env.getProperty("batch.jdbc.username"));
        config.setPassword(env.getProperty("batch.jdbc.password"));
        DataSource dataSource=new HikariDataSource(config);
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setTablePrefix("batch_");
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);
        factory.setDatabaseType("MySQL"); // Set your database type (HSQL, MySQL, etc.)
        return factory.getObject();
    }








}
