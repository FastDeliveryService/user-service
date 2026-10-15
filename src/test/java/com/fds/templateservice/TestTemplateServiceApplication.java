package com.fds.templateservice;

import org.springframework.boot.SpringApplication;

public class TestTemplateServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(TemplateServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
