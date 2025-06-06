package test.parsing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech.petrepopescu.phoenix.controllers.FragmentController;
import tech.petrepopescu.phoenix.parser.ElementFactory;
import tech.petrepopescu.phoenix.parser.PhoenixParser;
import tech.petrepopescu.phoenix.parser.compiler.Compiler;
import tech.petrepopescu.phoenix.parser.compiler.DynamicClassLoader;
import tech.petrepopescu.phoenix.parser.route.RouteGenerator;
import tech.petrepopescu.phoenix.special.PhoenixSpecialElementsUtil;
import tech.petrepopescu.phoenix.spring.PhoenixErrorHandler;
import tech.petrepopescu.phoenix.spring.PhoenixMessageConverter;
import tech.petrepopescu.phoenix.spring.SecurityConfig;
import tech.petrepopescu.phoenix.spring.config.PhoenixConfiguration;
import tech.petrepopescu.phoenix.spring.config.ViewsConfiguration;
import tech.petrepopescu.phoenix.views.View;
import tech.petrepopescu.utils.ReadFileUtil;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {SecurityConfig.class, PhoenixMessageConverter.class, RouteGenerator.class, Compiler.class, ElementFactory.class,
        DynamicClassLoader.class, PhoenixConfiguration.class, PhoenixSpecialElementsUtil.class, FragmentController.class,
        PhoenixErrorHandler.class})
class SectionTests {
    @Autowired
    private Compiler compiler;

    @Autowired
    private RouteGenerator routeGenerator;

    @Autowired
    private PhoenixSpecialElementsUtil phoenixSpecialElementsUtil;

    @Test
    void test() throws Exception {
        PhoenixConfiguration phoenixConfiguration = new PhoenixConfiguration();
        phoenixConfiguration.setViews(new ViewsConfiguration());
        phoenixConfiguration.getViews().setPath("src/test/resources/section");

        PhoenixParser parser = new PhoenixParser(new ElementFactory(null), routeGenerator, compiler, phoenixConfiguration);
        parser.parse();

        Map<String, List<Object>> tests = new HashMap<>();
        tests.put("simpleSectionTest", List.of(3));
        tests.put("insertOnceTest", List.of(3));

        // We need to repeat the insertOnce test to make sure that second call is correct as well
        tests.put("insertOnceTest", List.of(3));

        tests.put("sectionTest", List.of(10));
        tests.put("complexSectionTest", List.of(1));
        tests.put("sectionTestWithLoop", List.of(1));

        for (Map.Entry<String, List<Object>> entry : tests.entrySet()) {
            String htmlContent = View.of(entry.getKey(), entry.getValue().toArray(new Object[0])).getContent(phoenixSpecialElementsUtil);
            String expected = ReadFileUtil.readFileToString(new File("src/test/resources/section/expected/" + entry.getKey() + ".html"), Charset.defaultCharset());
            assertEquals(expected, htmlContent, "Failed for test: " + entry.getKey());
        }
    }
}
