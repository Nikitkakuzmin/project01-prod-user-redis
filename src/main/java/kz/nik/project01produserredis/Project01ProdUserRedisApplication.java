package kz.nik.project01produserredis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class Project01ProdUserRedisApplication {
    public static void main(String[] args) {
        SpringApplication.run(Project01ProdUserRedisApplication.class, args);
    }
}

