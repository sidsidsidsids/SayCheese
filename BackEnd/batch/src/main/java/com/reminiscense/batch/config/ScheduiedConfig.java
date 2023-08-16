package com.reminiscense.batch.config;

import com.reminiscense.batch.service.FrameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ScheduiedConfig {
    public static String JOB_NAME="REMOVE_FRAME";

    private final JobLauncher jobLauncher;
    private final FrameService frameService;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Scheduled(cron ="0 0 1 * * *")
    public void scheduledBatch(){
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyyMMdd");
        String date=dateTimeFormatter.format(LocalDateTime.now());

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestDate", date)
                .toJobParameters();
        log.info("날짜 : {}",date);
        try {
            jobLauncher.run(removeFrameJob(), jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Bean
    public Job removeFrameJob(){
        return jobBuilderFactory.get(JOB_NAME)
                .start(removeStep()).build();
    }
    @Bean
    public Step removeStep(){
        return stepBuilderFactory.get("removeStep")
                .tasklet(removeTasklet())
                .allowStartIfComplete(true)
                .build();
    }
    @Bean
    public MethodInvokingTaskletAdapter removeTasklet(){
        MethodInvokingTaskletAdapter methodInvokingTaskletAdapter=new MethodInvokingTaskletAdapter();
        methodInvokingTaskletAdapter.setTargetObject(frameService);
        methodInvokingTaskletAdapter.setTargetMethod("removeFrame");
        return methodInvokingTaskletAdapter;
    }
}
