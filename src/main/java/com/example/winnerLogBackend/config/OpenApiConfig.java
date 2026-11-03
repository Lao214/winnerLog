package com.example.winnerLogBackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("WinnerLog - 足球比赛AI预测导入与胜率排行榜系统 API")
                        .description("支持 GPT、通义千问等 AI 模型的足球比赛预测导入、未完赛建档、完赛结果结算判定、五大联赛/欧冠/世界杯/欧洲杯赛季胜率排行榜实时更新与模型对决统计。")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("WinnerLog Sports AI Dev Team")
                                .email("dev@winnerlog.local"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
