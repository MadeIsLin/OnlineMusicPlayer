package com.example.onlinemusicplayer.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class AppConfig implements WebMvcConfigurer{
    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MusicInterceptor())
                .addPathPatterns("/**") // 拦截所有的url
                .excludePathPatterns("/user/login") // 登录功能不拦截。不登录，哪来的信息给我们验证？
                .excludePathPatterns("/user/login2"); // 虽然login2只是演示，但也属于登录功能，也就不拦截了
    }
}
