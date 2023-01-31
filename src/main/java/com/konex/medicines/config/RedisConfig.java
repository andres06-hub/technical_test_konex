package com.konex.medicines.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

  @Value("${spring.data.redis.host}")
  private String redisHost;

  @Value("${spring.data.redis.port}")
  private int redisPort;

  @Value("${spring.data.redis.password}")
  private String pwd;

  // @Bean
  // JedisConnectionFactory jedisConnectionFactory() {
  //   JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
  //   // jedisConFactory.setHostName("localhost");
  //   jedisConFactory.getConnection();
  //   // jedisConFactory.setPort(6379);
  //   return jedisConFactory;
  // }
  @Bean
  LettuceConnectionFactory redisConnectionFactory() {
    LettuceConnectionFactory ltF = new LettuceConnectionFactory();
    ltF.setHostName(this.redisHost);
    ltF.setPort(this.redisPort);
    ltF.setPassword(this.pwd);
    ltF.setDatabase(0);
    ltF.setClientName("konex");
    return ltF;
  }

  @Bean
  RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(this.redisConnectionFactory());
    return template;
  }
}
