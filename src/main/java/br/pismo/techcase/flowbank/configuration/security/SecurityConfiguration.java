package br.pismo.techcase.flowbank.configuration.security;

import br.pismo.techcase.flowbank.configuration.filters.ApiKeyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfiguration {
    @Bean
    public FilterRegistrationBean<ApiKeyFilter> apiKeyFilterBean(ApiKeyFilter apiKeyFilter) {
        FilterRegistrationBean<ApiKeyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(apiKeyFilter);
        registrationBean.addUrlPatterns("/api/v1/*");
        registrationBean.setOrder(Integer.MIN_VALUE);
        return registrationBean;
    }
}
