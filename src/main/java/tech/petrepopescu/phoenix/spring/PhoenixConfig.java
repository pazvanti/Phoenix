package tech.petrepopescu.phoenix.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.petrepopescu.phoenix.controllers.FragmentController;
import tech.petrepopescu.phoenix.parser.ElementFactory;
import tech.petrepopescu.phoenix.parser.PhoenixParser;
import tech.petrepopescu.phoenix.parser.compiler.Compiler;
import tech.petrepopescu.phoenix.parser.compiler.DynamicClassLoader;
import tech.petrepopescu.phoenix.parser.route.RouteGenerator;
import tech.petrepopescu.phoenix.special.PhoenixSpecialElementsUtil;
import tech.petrepopescu.phoenix.spring.config.PhoenixConfiguration;

import java.util.List;

@EnableWebMvc
@Configuration
@EnableConfigurationProperties
@Import({SecurityConfig.class, PhoenixMessageConverter.class, RouteGenerator.class, Compiler.class, ElementFactory.class,
        DynamicClassLoader.class, PhoenixConfiguration.class, PhoenixSpecialElementsUtil.class, FragmentController.class,
        PhoenixErrorHandler.class})
public class PhoenixConfig implements WebMvcConfigurer {
    @Autowired
    private PhoenixMessageConverter phoenixMessageConverter;

    @Autowired
    private PhoenixConfiguration config;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(phoenixMessageConverter);
    }

    @Bean
    public PhoenixParser templateParser(RouteGenerator routeGenerator, ElementFactory elementFactory, Compiler compiler, PhoenixConfiguration configuration) {
        PhoenixParser phoenixParser = new PhoenixParser(elementFactory, routeGenerator, compiler, configuration);
        phoenixParser.parse();
        return phoenixParser;
    }
}
