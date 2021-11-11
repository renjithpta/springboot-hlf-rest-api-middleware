package com.ibs.fixmtoacris.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPICofiguration {

    @Bean
    public OpenAPI customOpenAPI(
            @Value("${application.description}") String appDes,
            @Value("${application.version}") String appVersion) {

        return new OpenAPI()
                .info(new Info()
                .title("Hyperledger Fabric FixmToAcris chain REST APIs")
                .version(appVersion)
                .description(appDes)
                .termsOfService("http://swagger.io/terms/")
                .license(new License().name("Apache 2.0")
                .url("")));
    }

    @Bean
    public GroupedOpenApi userRegistrationOpenApi() {
        String packagesToscan[] = {"com.ibs.fixmtoacris.controller"};
        return GroupedOpenApi
                .builder()
                .group("user_registration")
                .packagesToScan(packagesToscan)
                .build();
    }

    @Bean
    public GroupedOpenApi userRoleOpenApi() {
        String packagesToscan[] = {"com.uvsoftgroup.userregistration"};
        return GroupedOpenApi.builder().group("user_role").packagesToScan(packagesToscan)
                .build();
    }

    

}
