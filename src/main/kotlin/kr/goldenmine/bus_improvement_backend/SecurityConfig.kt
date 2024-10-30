package kr.goldenmine.bus_improvement_backend

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfig(
//    val authenticationProvider: UserAuthenticationProvider,
//    val authFilter: AuthFilter
) {
//    @Bean
//    fun authManager(http: HttpSecurity): AuthenticationManager {
//        val authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder::class.java)
//        authenticationManagerBuilder.authenticationProvider(authenticationProvider)
//        return authenticationManagerBuilder.build()
//    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { csrf -> csrf.disable() }
        http.cors { cors -> cors.disable() }
        return http.build()
    }

//    @Throws(java.lang.Exception::class)
//    override fun authenticationManager(): AuthenticationManager {
//        return ProviderManager(listOf(authenticationProvider as AuthenticationProvider))
//    }

//    @Bean
//    @Throws(Exception::class)
//    fun filterChain(http: HttpSecurity): SecurityFilterChain {
//        http.authorizeRequests()
//            .antMatchers("/**").permitAll()
////            .antMatchers("/account").permitAll()
////            .antMatchers("/auth").permitAll()
////            .antMatchers("/html").permitAll()
////            .antMatchers("/statistics").hasIpAddress("localhost")
//            .and()
////            .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter::class.java)
//            .csrf().disable()
//            .cors().disable()
//
//
//        return http.build()
//    }
//    @Throws(Exception::class)
//    fun configure(http: HttpSecurity) {
//        http.httpBasic {
//            it.disable()
//        }
//
//        http.authorizeRequests {
//            it.anyRequest().permitAll()
//        }
//
//        http.csrf {
//            it.disable().authorizeHttpRequests {
//                it.anyRequest().permitAll()
//            }
//        }
//        http.httpBasic().disable()
//        http.csrf().disable()
//            .authorizeRequests().requestMatchers(HttpMethod.OPTIONS,"*/").permitAll()
//            .requestMatchers(HttpMethod.GET,"/login").permitAll();
//        security.httpBasic().disable()
//    }
}