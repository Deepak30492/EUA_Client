package in.gov.nha.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class EuaConfiguration {
    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }
//    @Bean
//    public Docket swaggerApiConfig() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .paths(PathSelectors.any())
//                .apis(RequestHandlerSelectors.basePackage("com.abdm.eua"))
//                .build();
//    }
}
