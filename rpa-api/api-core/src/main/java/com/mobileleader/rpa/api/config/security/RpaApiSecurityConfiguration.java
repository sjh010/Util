package com.mobileleader.rpa.api.config.security;

import com.mobileleader.rpa.api.config.security.filter.RpaApiAuthenticationTokenFilter;
import com.mobileleader.rpa.api.config.security.provider.RpaApiAuthenticationProvider;
import com.mobileleader.rpa.api.config.security.voter.RpaApiAccessVoter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class RpaApiSecurityConfiguration extends WebSecurityConfigurerAdapter {

    // @formatter:off
    private static final String[] IGNORING_URLS =
        {"/authentication/robot", "/authentication/studio", "/authentication/publickey"};
    // @formatter:on

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(IGNORING_URLS).antMatchers(HttpMethod.OPTIONS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(rpaApiAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.anonymous().disable()
            .cors()
            .and()
            .csrf().disable()
            .authorizeRequests().accessDecisionManager(affirmativeBased())
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(new RpaApiAuthenticationTokenFilter(), BasicAuthenticationFilter.class);
        // @formatter:on
    }

    @Bean
    public AuthenticationProvider rpaApiAuthenticationProvider() {
        return new RpaApiAuthenticationProvider();
    }

    /**
     * Create AffirmativeBased bean.
     *
     * @return {@link AffirmativeBased}
     */
    @Bean
    public AffirmativeBased affirmativeBased() {
        List<AccessDecisionVoter<? extends Object>> voters = new ArrayList<>();
        voters.add(rpaApiAccessVoter());
        return new AffirmativeBased(voters);
    }

    @Bean
    public RpaApiAccessVoter rpaApiAccessVoter() {
        return new RpaApiAccessVoter();
    }
}
