package com.yac.service.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(
		basePackages = {"com.yac.service.module.*.mapper"})//annotationClass = Mapper.class
public class MyBatisAutoConfig {
//	@Bean
//	public PaginationInterceptor paginationInterceptor() {
//		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
//		paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
//		return paginationInterceptor;
//	}
}
