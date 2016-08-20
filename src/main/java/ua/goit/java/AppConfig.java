package ua.goit.java;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {

    @Bean
    public Bootstrap bootstrap(ExecutorFactory executorFactory, TaskProvider<Integer> integerTaskProvider){
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.setExecutorFactory(executorFactory);
        bootstrap.setTaskProvider(integerTaskProvider());

        return bootstrap;
    }

    @Bean
    public TaskProvider<Integer> integerTaskProvider(){
        IntegerTaskProvider integerTaskProvider = new IntegerTaskProvider();
        integerTaskProvider.init();
        return integerTaskProvider();
    }

    @Bean
    @Scope("prototype")
    public SerialExecutor<Integer> serialExecutor(){
        return new SerialExecutor();
    }

    @Bean
    public ExecutorFactory executorFactory(){
        return new ExecutorFactory() {
            @Override
            public Executor<Integer> getIntegerExecutor() {
                return serialExecutor();
            }
        };
    }
}
