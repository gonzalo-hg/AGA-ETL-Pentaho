package mx.uam.etl.backend.security;

import lombok.RequiredArgsConstructor;
import mx.uam.etl.backend.filter.AuthotizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private  UserDetailsService userDetailsService;

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and();
		http.csrf().disable();

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().antMatchers("/","/css/**","/js/**").permitAll();
		http.authorizeRequests().antMatchers("/api/login/**","/api/token/refresh/**").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/usuario/**").hasAnyAuthority("user");
		http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/alumnos/**").hasAnyAuthority("admin");
		http.authorizeRequests().antMatchers(HttpMethod.GET,"/v1/files/**").hasAnyAuthority("admin");

		http.addFilterBefore(new AuthotizationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	/*@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManagerBean();
	}*/

}
