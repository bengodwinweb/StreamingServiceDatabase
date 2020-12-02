package com.streamingservicebackend;

import com.streamingservicebackend.database.DatabaseHandler;
import com.streamingservicebackend.database.DbSeeder;
import com.streamingservicebackend.server.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class Main {

    public static void main(String[] args) {
        if (Config.SEED_DB) {
            DbSeeder seeder = new DbSeeder(new DatabaseHandler());
            seeder.seed();
        }

        SpringApplication.run(Main.class, args);
    }
}
