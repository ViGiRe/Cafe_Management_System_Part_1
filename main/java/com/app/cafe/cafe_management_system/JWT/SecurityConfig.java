package com.app.cafe.cafe_management_system.JWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

	@Autowired
	CustomerUserDetailService customerUserDetailService;
	
	@Autowired
	jwtFilter jwtFilter;
	
	 @Bean
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
			
		}

	@Autowired
	public SecurityConfig(CustomerUserDetailService customerUserDetailService) {
	    this.customerUserDetailService = customerUserDetailService;
	}
	
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	
        auth.userDetailsService(customerUserDetailService).passwordEncoder(passwordEncoder());
    }
    
	
	
	 
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(this.customerUserDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
	public AuthenticationManager authenticationManager(
	        AuthenticationConfiguration auth) throws Exception {
	    return auth.getAuthenticationManager();
	}

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
            .and()
            .csrf().disable()
            .authorizeHttpRequests()
                .requestMatchers("/user/signup","/user/login", "/user/forgotPassword")
                .permitAll()
                .anyRequest().authenticated()
            .and()
            .exceptionHandling()
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
 
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.authenticationProvider(daoAuthenticationProvider());
        DefaultSecurityFilterChain defaultSecurityFilterChain = http.build();
        return defaultSecurityFilterChain;
    }
}
//		
//	    @Bean
//		public PasswordEncoder passwordEncoder() {
//			return new BCryptPasswordEncoder();
//			
//		}
//	    
//		@Bean
//		public AuthenticationManager authenticationManager(
//		        AuthenticationConfiguration auth) throws Exception {
//		    return auth.getAuthenticationManager();
//		}
//		
//		@Bean
//		public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//			
//			http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
//			.and()
//			.csrf().disable()
//			.authorizeHttpRequests()
//			.requestMatchers("/user/login","/user/signup","/user/forgotPassword")
//			.permitAll()
//			.anyRequest()
//			.authenticated()
//			.and()
//			.exceptionHandling()
//			.and()
//			.sessionManagement()
//			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//			
//			http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//			return http.build();
//			
//		}
	
//	
//    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder.userDetailsService(customerUserDetailService).passwordEncoder(passwordEncoder());
//        System.out.println("Done...finito");
//    }
	
//	protected void configure(
//	        AuthenticationManagerBuilder auth) throws Exception {
//	   auth.userDetailsService(customerUserDetailService);
//	}

//	@Bean
//	public Authentication authenticate(Authentication authentication) throws Exception {
//		String user = authentication.getName();
//		String password = authentication.getCredentials().toString();
//		String forgotPassword = authentication.getCredentials().toString();
//		UserDetails usern = customerUserDetailService.loadUserByUsername(user);
//		
//	    return checkPassword(usern, password);
//	}
//	
//	 private Authentication checkPassword(UserDetails user, String rawPassword) {
//	        if (Objects.equals(rawPassword, user.getPassword())) {
//	            return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
//	        } else {
//	            throw new BadCredentialsException("Bad credentials");
//	        }
//	      }
		
	
//	  @Bean
//	    public WebSecurityCustomizer webSecurityCustomizer(CustomerUserDetailService customerUserDetailService) {
//	        return (web) -> web.ignoring().requestMatchers("/ignore1", "/ignore2");
//	    }


