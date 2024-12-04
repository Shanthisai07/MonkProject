package com.example.monk.DBConfig;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class MonkDBConfig {

	@Bean("monkDataSource")
	public DataSource MonkDataSource() {
		
		BasicDataSource monkDataSource = new BasicDataSource();
		
		monkDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		monkDataSource.setUrl("jdbc:mysql://localhost:3306/monk");
		monkDataSource.setUsername("root");
		monkDataSource.setPassword("yourPassword");
		monkDataSource.setMaxTotal(2);
		monkDataSource.setMaxIdle(4);
		monkDataSource.setMinIdle(2);
		monkDataSource.setPoolPreparedStatements(true);
		return monkDataSource;
	
	}

	@Bean("monkJdbcTemplate")
	public JdbcTemplate jdbcTemplate(@Qualifier("monkDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
}
