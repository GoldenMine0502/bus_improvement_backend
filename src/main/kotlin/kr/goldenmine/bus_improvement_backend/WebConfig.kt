package kr.goldenmine.bus_improvement_backend

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class WebConfig : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
//        registry.addMapping("/**") // 모든 엔드포인트에 대해 CORS 허용
//            .allowedOrigins("http://home.goldenmine.kr:3000", "http://locahost:3000") // 특정 출처만 허용
//            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//            .allowedHeaders("*")
//            .allowCredentials(true); // 인증 포함 허용
    }
}