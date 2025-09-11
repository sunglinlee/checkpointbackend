import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // 針對 /api/ 底下所有路徑套用此規則
                .allowedOrigins("https://storage.googleapis.com") // ！！！這裡必須換成你 GCS 前端的確切來源！！！
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允許的 HTTP 方法
                .allowedHeaders("*") // 允許所有標頭
                .allowCredentials(true) // 是否允許攜帶憑證 (例如 cookies)
                .maxAge(3600); // 預檢請求 (OPTIONS) 的快取時間 (秒)
    }
}