package com.demo.demo.core.configurer.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity   //开启web的安全模式  里面包含@configration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    //在ioc容器获取自定义user
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //登出后进入系统主页(默认登陆页面)
        http.logout().logoutSuccessUrl("/index/login");
        //记住我（设置属性为 remeber）
        http.rememberMe().rememberMeParameter("remeber");
        //对登陆的授权请求
        http.authorizeRequests()
                //此处设置所有的静态资源可访问
                .antMatchers("/js/**", "/css/**", "/images/*", "/plugins/**", "/bootstrap/**").permitAll()
                .antMatchers("/static/file/**").permitAll()
                .antMatchers("/http/**").permitAll()
                .antMatchers("/vendor/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/freeIp/getIp").permitAll()

                // 集成cas 放行路径
                .antMatchers("/cas/login").permitAll()
                .antMatchers("/freeIp/getJobNumberByFreeIp").permitAll()
                // 集成钉钉单点登陆 放行路径
                .antMatchers("/ding/*").permitAll()
                // security 登陆界面放行
                .antMatchers("/index/login").permitAll()

                .anyRequest().authenticated()
                .and()
                // 设置自定义登陆页面
                .formLogin().loginPage("/index/login")
                // 设置登陆成功页
                .defaultSuccessUrl("/index/main").permitAll()
                // 自定义登陆用户名和密码参数，默认为username和password
                .usernameParameter("userName")
                .passwordParameter("password")
                //跳转到错误页面
                .failureUrl("/index/error")
                .and();
        // 关闭CSRF跨域   post请求可以成功
        http.csrf().disable();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }
            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals(charSequence.toString());
            }
        });
    }
}
