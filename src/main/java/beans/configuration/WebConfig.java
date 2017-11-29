package beans.configuration;

import beans.controller.util.pdf.ItextPdfView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.Properties;


@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"beans"})
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/WEB-INF/pages/**").addResourceLocations("/pages/");
    }

    @Bean
    public MultipartResolver multipartResolver() {
        StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
        return resolver;
    }

    //for pdf view
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
                .defaultContentType(MediaType.TEXT_HTML)
                .parameterName("type")
                .favorParameter(true)
                .ignoreUnknownPathExtensions(false)
                .ignoreAcceptHeader(false)
                .useJaf(true);
    }

    //for pdf view
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.freeMarker();
        registry.enableContentNegotiation(
                new ItextPdfView()
        );
    }

    //for freemarker
    @Bean
    public FreeMarkerViewResolver freemarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setCache(true);
        resolver.setPrefix("");
        resolver.setSuffix(".ftl");
        return resolver;
    }

    //for freemarker
    @Bean
    public FreeMarkerConfigurer freemarkerConfig() {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPath("/WEB-INF/pages/");
        return freeMarkerConfigurer;
    }

    //exception mapping
    @Bean(name = "simpleMappingExceptionResolver")
    public SimpleMappingExceptionResolver createSimpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();

        Properties mappings = new Properties();

        r.setExceptionMappings(mappings);
        r.setDefaultErrorView("error");
        return r;
    }
}
