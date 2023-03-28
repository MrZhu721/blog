package com.zijinge.blogadmin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        // 策略加密 md5不安全  彩虹表  md5加盐
        String zijinge = new BCryptPasswordEncoder().encode("zijinge");
        System.out.println(zijinge);
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        super.configure(webSecurity);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()    //开启登录认证
                // 将下面的静态目录放行
                .antMatchers("/css/**").permitAll()
                .antMatchers("/img/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/plugins/**").permitAll()
                // 通过 自定义认证 方法 认证成功返回true 否则false
                .antMatchers("/admin/**").access("@authService.auth(request,authentication)")    //自定义service 来去实现实时的权限认证
                // authenticated() 通过认证才有权进行访问
                .antMatchers("/pages/**").authenticated()
                .and().formLogin()
                .loginPage("/login.html")   // 自定义登录页面
                .loginProcessingUrl("/login")   // 登录处理接口
                .usernameParameter("username")  // 定义登录是的用户名的key 默认为username
                .passwordParameter("password")  //定义登录是的密码的key 默认为password
                .defaultSuccessUrl("/pages/main.html")  //登录成功跳转的页面
                .failureUrl("/login.html")  //登录失败跳转的页面
                .permitAll()    // 通过 不拦截
                .and().logout() // 退出登录配置
                .logoutUrl("/logout")   // 退出登录接口
                .logoutSuccessUrl("/login.html")
                .permitAll()    // 退出登录的接口放行
                .and()
                .httpBasic()
                .and()
                .csrf().disable()   // 关闭 csrf(跨站伪造攻击) spring-security 默认开启， 如果需要自定义登录则需关闭它
                .headers().frameOptions().sameOrigin(); // 支持iframe页面嵌套
    }
}