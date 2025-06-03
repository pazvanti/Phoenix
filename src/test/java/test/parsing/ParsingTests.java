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
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {SecurityConfig.class, PhoenixMessageConverter.class, RouteGenerator.class, Compiler.class, ElementFactory.class,
        DynamicClassLoader.class, PhoenixConfiguration.class, PhoenixSpecialElementsUtil.class, FragmentController.class,
        PhoenixErrorHandler.class})
class ParsingTests {
    @Autowired
    private Compiler compiler;

    @Autowired
    private RouteGenerator routeGenerator;

    @Autowired
    private PhoenixSpecialElementsUtil phoenixSpecialElementsUtil;

    @Test
    void test() throws IOException {
        PhoenixConfiguration phoenixConfiguration = new PhoenixConfiguration();
        phoenixConfiguration.setViews(new ViewsConfiguration());
        phoenixConfiguration.getViews().setPath("src/test/resources/views");

        PhoenixParser parser = new PhoenixParser(new ElementFactory(null), routeGenerator, compiler, phoenixConfiguration);
        parser.parse();

        Map<String, List<Object>> tests = new HashMap<>();
        tests.put("simpleTest", List.of(0, 0));
        tests.put("forAndIfTest", List.of(4, List.of("one", "two", "three"), 3));
        tests.put("advanceForAndIfTest", List.of(0, 3, List.of("one", "two", "three", "for", "five")));
        tests.put("variableTest", List.of(3, 4, List.of("10", "90", "50")));
        tests.put("elseInScript", List.of(3));
        tests.put("autowiredTest", List.of(3));

        List<Object> rawTestParams = new ArrayList<>();
        rawTestParams.add("<span>test</span>");
        rawTestParams.add(null);
        tests.put("rawTest", rawTestParams);

        for (Map.Entry<String, List<Object>> entry : tests.entrySet()) {
            String htmlContent = View.of(entry.getKey(), entry.getValue().toArray(new Object[0])).getContent(phoenixSpecialElementsUtil);
            String expected = ReadFileUtil.readFileToString(new File("src/test/resources/expected/" + entry.getKey() + ".html"), Charset.defaultCharset());
            assertEquals(expected, htmlContent, entry.getKey());
        }
    }
}
