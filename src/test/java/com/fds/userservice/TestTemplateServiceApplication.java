package com.fds.userservice;

import org.springframework.boot.SpringApplication;

public class TestTemplateServiceApplication {

    static void main(String[] args) {
        SpringApplication.from(UserServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
