package com.mobileleader.rpa.view.config.security;

import com.mobileleader.rpa.view.config.security.details.RpaViewAuthenticationDetailsSource;
import com.mobileleader.rpa.view.config.security.handler.RpaViewAccessDeniedHandler;
import com.mobileleader.rpa.view.config.security.handler.RpaViewAuthenticationFailureHandler;
import com.mobileleader.rpa.view.config.security.handler.RpaViewAuthenticationSuccessHandler;
import com.mobileleader.rpa.view.config.security.handler.RpaViewLogoutSuccessHandler;
import com.mobileleader.rpa.view.config.security.provider.RpaViewAuthenticationProvider;
import com.mobileleader.rpa.view.config.security.voter.RpaViewAccessVoter;
import com.mobileleader.rpa.view.config.security.voter.RpaViewAffirmativeBased;
import com.mobileleader.rpa.view.util.RequestHeaderUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class RpaViewSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private RedirectStrategy redirectStrategy;

    // @formatter:off
    private static final String[] RESOURCES_PATH =
        {"/js/**", "/css/**", "/fonts/**", "/images/**", "/favicon.ico"};
    // @formatter:on

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(RESOURCES_PATH);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.cors()
            .and()
            .csrf().ignoringAntMatchers("/login")
            .and()
            .authenticationProvider(authenticationProvider())
            .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
            .and()
            .authorizeRequests().anyRequest().authenticated()
            .accessDecisionManager(affirmativeBased())
            .requestMatchers(new RequestMatcher() {
                @Override
                public boolean matches(HttpServletRequest request) {
                    return CorsUtils.isPreFlightRequest(request);
                }
            }).permitAll()
            .and()
            .formLogin().loginPage("/login").permitAll()
                .usernameParameter("userId").passwordParameter("password")
                .loginProcessingUrl("/login_process")
                .authenticationDetailsSource(new RpaViewAuthenticationDetailsSource())
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHander())
            .and()
            .logout()
                .logoutUrl("/logout").permitAll()
                .logoutSuccessUrl("/login")
                .clearAuthentication(true)
            .and()
            .sessionManagement().invalidSessionStrategy(new InvalidSessionStrategy() {
                @Override
                public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException {
                    if (!RequestHeaderUtils.isAjaxRequest(request)) {
                        request.getSession(true);
                        redirectStrategy.sendRedirect(request, response, "/login");
                    }
                }
            });
        // @formatter:on
    }

    /**
     * CORS Configuration.
     *
     * @return {@link CorsConfigurationSource}
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHander() {
        return new RpaViewAuthenticationFailureHandler();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new RpaViewLogoutSuccessHandler();
    }

    @Bean
    public RpaViewAccessDeniedHandler accessDeniedHandler() {
        return new RpaViewAccessDeniedHandler();
    }

    @Bean
    public RpaViewAuthenticationProvider authenticationProvider() {
        return new RpaViewAuthenticationProvider();
    }

    @Bean
    public RpaViewAuthenticationSuccessHandler authenticationSuccessHandler() {
        return new RpaViewAuthenticationSuccessHandler();
    }

    /**
     * Create AffirmativeBased bean.
     *
     * @return {@link RpaViewAffirmativeBased}
     */
    @Bean
    public RpaViewAffirmativeBased affirmativeBased() {
        List<AccessDecisionVoter<? extends Object>> voters = new ArrayList<>();
        voters.add(accessVoter());
        return new RpaViewAffirmativeBased(voters);
    }

    @Bean
    public RpaViewAccessVoter accessVoter() {
        return new RpaViewAccessVoter();
    }
}
