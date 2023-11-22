package com.jsti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

import com.alibaba.fastjson.parser.ParserConfig;

@SpringBootApplication
@ServletComponentScan
@EnableAsync
@EnableCaching
public class ApiApplication {

	public static void main(String[] args) {
		ParserConfig.getGlobalInstance().addAccept("com.jsti.");
		ParserConfig.getGlobalInstance().addAccept("org.springframework.security.schedule.");
        SpringApplication.run(ApiApplication.class, args);
	}
}
