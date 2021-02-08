package com.kalsym.chatbot.flowbuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.builders.RequestHandlerSelectors;

@SpringBootApplication
@EnableSwagger2
public class FlowBuilderCoreApplication extends WebSecurityConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(FlowBuilderCoreApplication.class, args);
    }
    
    @Bean
    public Docket productApi() {
      return new Docket(DocumentationType.SWAGGER_2).select()
         .apis(RequestHandlerSelectors.basePackage("com.kalsym.chatbot.flowbuilder")).build();
   }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.antMatcher("/**")
      .authorizeRequests()
      .antMatchers("/", "/webjars/**", "/error**","/v2/**","/swagger-resources/**","/swagger-ui.html")
      .permitAll()
      .anyRequest()
      .authenticated();
    }
        
    @Override
    public void configure(WebSecurity web) throws Exception {
        //don't check security for login
        web.ignoring().antMatchers("/flow/**","/vertex/**","/conversation/**","/mxgraph/**");
    }
    
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*");
            }
        };
    }
    
}
