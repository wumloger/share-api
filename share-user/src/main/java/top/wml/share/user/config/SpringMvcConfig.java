package top.wml.share.user.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.wml.share.common.interceptor.Loginterceptor;

//@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {
    @Resource
    private Loginterceptor loginterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(loginterceptor);
    }
}
