package com.demo.demo.core.configurer.cas;

import net.unicon.cas.client.configuration.CasClientConfigurerAdapter;
import net.unicon.cas.client.configuration.EnableCasClient;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 牟欢
 * @Classname CasConfigure
 * @Description TODO
 * @Date 2020-07-10 13:52
 */
@Configuration
@EnableCasClient
public class CasConfigure extends CasClientConfigurerAdapter {

    @Override
    public void configureAuthenticationFilter(FilterRegistrationBean authenticationFilter) {
        super.configureAuthenticationFilter(authenticationFilter);
        authenticationFilter.getInitParameters().put("authenticationRedirectStrategyClass","com.patterncat.CustomAuthRedirectStrategy");
    }
}
