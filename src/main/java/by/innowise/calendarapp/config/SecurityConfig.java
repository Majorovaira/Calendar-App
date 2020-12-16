package by.innowise.calendarapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .anyRequest().authenticated();


    }


    @Bean
    public UserDetailsService inMemoryUserDetailsManager() {
        UserDetails manager = User.withUsername("manager")
                .passwordEncoder(passwordEncoder()::encode)
                .password("1111")
                .build();
        UserDetails user = User.withUsername("user")
                .passwordEncoder(passwordEncoder()::encode)
                .password("2222")
                .build();
        UserDetails admin = User.withUsername("admin")
                .passwordEncoder(passwordEncoder()::encode)
                .password("3333")
                .build();
        InMemoryUserDetailsManager memoryUserDetailsManager = new InMemoryUserDetailsManager();
        memoryUserDetailsManager.createUser(manager);
        memoryUserDetailsManager.createUser(user);
        memoryUserDetailsManager.createUser(admin);
        return memoryUserDetailsManager;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.parentAuthenticationManager(authenticationManagerBean())
                .userDetailsService(userDetailsService());
    }
}
