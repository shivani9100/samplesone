package com.Inventory.Project.AssectService.Security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public Docket docket() {

		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.Inventory.Project.AssectService")).build();
	}

	@Autowired
	private UserDetailsImple userDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().cors().and().authorizeRequests().antMatchers("/employee/authenticate").permitAll()
		.antMatchers("/employee/forgot-password/**").permitAll().antMatchers("/CompanyDetails/**").permitAll()
		.antMatchers("/LocationDetails/**").permitAll().antMatchers("/FloorDetails/**").permitAll()
		.antMatchers("/BuildingDetails/**").permitAll().antMatchers("/BlockDetails/**").permitAll()
		.antMatchers("/employee/confirm-reset/***").permitAll().antMatchers("/employee/reset-password")
		.permitAll().antMatchers("/rest/assettype/*****").permitAll().antMatchers("/employee/update/**")
		.permitAll().antMatchers("/employee/search/**").permitAll()

		.antMatchers("/rest/assettype/download/AssetExcelFile/**").permitAll()
		.antMatchers("/rest/assettype/download/Dummyfile/**").permitAll()
		.antMatchers("/rest/assettype/Upload/assetType/**").permitAll()

		.antMatchers("/Assectservice/getAllAssects/**").permitAll()

		// For Brand
		.antMatchers("/BrandMaster/download/BrandExcelFile/**").permitAll()
		.antMatchers("/BrandMaster/download/BrandExcelFileDummyFile/**").permitAll()
		.antMatchers("/BrandMaster/upload/brandexcelfile/**").permitAll()

		// For Model
		.antMatchers("/Modelservice/download/ModelExcelFile/**").permitAll()
		.antMatchers("/Modelservice/model/download/Dummyfile/**").permitAll()
		.antMatchers("/Modelservice/Upload/exceldata/**").permitAll()

		// For Vendor
		.antMatchers("/vendorservice/download/vendorExcelfile/**").permitAll()
		.antMatchers("/vendorservice/download/dummyvendorexcelfile/**").permitAll()
		.antMatchers("/vendorservice/upload/VendorExcelFile/**").permitAll()

		// For HardDiskType
		.antMatchers("/hardDiskOfType/download/hardDiskTypeExcelFile/**").permitAll()
		.antMatchers("/hardDiskOfType/download/HardDiskTypeDummyExcelFile/**").permitAll()
		.antMatchers("/hardDiskOfType/updload/hardDiskType/**").permitAll()
		.antMatchers("/hardDiskOfType/harddiskTypeformat/download/**").permitAll()

		// For HardDiskCapacity
		.antMatchers("/hardDiskCapacity/download/HardDiskCapacityExcelFile/**").permitAll()
		.antMatchers("/hardDiskCapacity/download/HardDiksCapacityDummyExcelFile/**").permitAll()
		.antMatchers("/hardDiskCapacity/upload/HardDiskcapacity/**").permitAll()

		.antMatchers("/hardDiskCapacity/harddiskCapacityformat/download/**").permitAll()

		// For RamType
		.antMatchers("/ramtypemaster/download/RamTypelFile/**").permitAll()
		.antMatchers("/ramtypemaster/download/ramtype/Dummyfile/**").permitAll()
		.antMatchers("/ramtypemaster/Upload/RamType/**").permitAll()

		// For RamCapacity

		.antMatchers("/rammaster/download/RamCapacityExcelFile/**").permitAll()
		.antMatchers("/rammaster/download/RamCapacityDummyExcelFile/**").permitAll()
		.antMatchers("/rammaster/updload/Ramcapacity/**").permitAll()

		// For Vendor
		.antMatchers("/vendorservice/download/vendorExcelfile/**").permitAll()
		.antMatchers("/vendorservice/download/dummyvendorexcelfile/**").permitAll()
		.antMatchers("/vendorservice/upload/VendorExcelFile/**").permitAll()

		.antMatchers("/Assectservice/download/AssetExcelFile/**").permitAll()

		.antMatchers("/Assectservice/download/dummyAssetExcelFile/**").permitAll()
		.antMatchers("/Assectservice/download/AssetCSVFile/**").permitAll()
		.antMatchers("/Assectservice/Upload/assetType/**").permitAll()

		.antMatchers("/assectEmployee/download/AssetEmployeeExcelFile/**").permitAll()
		.antMatchers("/assectEmployee/download/AssetEmployeeCSVFile/**").permitAll()

		.antMatchers("/role/**").hasAuthority("Admin").antMatchers("/employee/**").hasAuthority("Admin")
		.antMatchers("/rest/assettype/**").hasAuthority("Admin").antMatchers("/BrandMaster/**")
		.hasAuthority("Admin").antMatchers("/Modelservice/**").hasAuthority("Admin")
		.antMatchers("/vendorservice/**").hasAuthority("Admin").antMatchers("/hardDiskOfType/**")
		.hasAuthority("Admin").antMatchers("/hardDiskCapacity/**").hasAuthority("Admin")
		.antMatchers("/rammaster/**").hasAuthority("Admin").antMatchers("/ramtypemaster/**")
		.hasAuthority("Admin").antMatchers("/Assectservice/**").hasAuthority("Admin")

		.antMatchers("/requestforasset/**").permitAll()

		.anyRequest().authenticated().and().sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().logout().permitAll();
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public AuthenticationManager customAuthenticationManager() throws Exception {
		return authenticationManager();
	}

	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setPasswordEncoder(bCryptPasswordEncoder());
		authProvider.setUserDetailsService(userDetailsService);
		return authProvider;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
				"/configuration/security", "/swagger-ui.html", "/webjars/**");
	}


}
