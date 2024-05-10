package tech.petrepopescu.phoenix.parser.elements;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.petrepopescu.phoenix.parser.ElementFactory;
import tech.petrepopescu.phoenix.parser.VariableRegistry;
import tech.petrepopescu.utils.TestUtil;

import java.util.List;
import java.util.Set;

class HtmlElementTests {

    @Test
    void testWithVariable() {
        String line = "<h6>@b</h6>";
        HtmlElement element = new HtmlElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        VariableRegistry.getInstance().add("", "b", "String");
        element.parse("");

        String expected = "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\tcontentBuilder.append(StringEscapeUtils.escapeHtml4(b));\n" +
                "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n";

        Assertions.assertEquals(expected, TestUtil.sanitizeResult(element.write().toString()));
    }

    @Test
    void testWithRawVariable() {
        String line = "<h6>@raw(b)</h6>";
        HtmlElement element = new HtmlElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        VariableRegistry.getInstance().add("", "b", "String");
        element.parse("");

        String expected = "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\tcontentBuilder.append(b);\n" +
                "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n";

        Assertions.assertEquals(expected, TestUtil.sanitizeResult(element.write().toString()));
    }
    @Test
    void testWithRouteElement() {
        String line = "<a href=\"@routes.TestController.testElement(0, 1, 2)\">Link</a>";
        HtmlElement element = new HtmlElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\tcontentBuilder.append(routes.TestController.testElement(0, 1, 2).path());\n" +
                "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n";

        Assertions.assertEquals(expected, TestUtil.sanitizeResult(element.write().toString()));
    }

    @Test
    void testWithEmail() {
        String line = "<a href=\"mailto:test@@test.com\">Email me</a>";
        HtmlElement element = new HtmlElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\tcontentBuilder.append(\"@test.com\");\n" +
                "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n";

        Assertions.assertEquals(expected, TestUtil.sanitizeResult(element.write().toString()));
    }

    @Test
    void testIfInsideHtml() {
        String line = "<tr class=\"@if (i % 2 == 0) {odd} \">";
        HtmlElement element = new HtmlElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\tif (i % 2 == 0) {\n" +
                "\t\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\t}\n" +
                "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n";

        Assertions.assertEquals(expected, TestUtil.sanitizeResult(element.write().toString()));
    }

    @Test
    void testVariableInsideCss() {
        String line = "<input type=\"text\" placeholder=\"Value of a\" value=\"@a\" name=\"a-value\">";
        HtmlElement element = new HtmlElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\tcontentBuilder.append(a);\n" +
                "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n" +
                "\t\tcontentBuilder.append(STATIC_HTML_THISISUUID);\n";

        Assertions.assertEquals(expected, TestUtil.sanitizeResult(element.write().toString()));
    }
}
