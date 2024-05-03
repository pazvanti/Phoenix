package tech.petrepopescu.phoenix.parser;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tech.petrepopescu.phoenix.parser.elements.Element;
import tech.petrepopescu.phoenix.parser.elements.HtmlElement;
import tech.petrepopescu.phoenix.parser.elements.builders.*;

import java.util.*;

@Component
public class ElementFactory {
    public static final String DEFAULT_BUILDER_NAME = "contentBuilder";

    private final List<ElementBuilder> elementBuilders;
    private final List<String> staticImports;
    private final FragmentOrStaticImportElementBuilder fragmentOrStaticImportElementBuilder;

    private String builderName = DEFAULT_BUILDER_NAME;
    public ElementFactory(Set<ElementBuilder> elementBuilders) {
        if (CollectionUtils.isEmpty(elementBuilders)) {
            fragmentOrStaticImportElementBuilder = new FragmentOrStaticImportElementBuilder();
            elementBuilders = Set.of(new EscapeCharacterElementBuilder(), new RouteElementBuilder(), new ConstructorElementBuilder(),
                    new IfElementBuilder(), new ElseElementBuilder(), new ElseIfElementBuilder(),
                    new ImportElementBuilder(), new VariableElementBuilder(), new AssetElementBuilder(),
                    new HtmlElementBuilder(), new CommentElementBuilder(), new ForElementBuilder(), new CsrfElementBuilder(),
                    new BreakElementBuilder(), new WithElementBuilder(), new EvalElementBuilder(), new RawElementBuilder(),
                    new NullSafeVariableElementBuilder(), new NullSafeTernaryElementBuilder(), new ContentBlockElementBuilder(),
                    new ForWithIteratorElementBuilder(), fragmentOrStaticImportElementBuilder);
        } else {
            fragmentOrStaticImportElementBuilder = (FragmentOrStaticImportElementBuilder) elementBuilders.stream().filter(FragmentOrStaticImportElementBuilder.class::isInstance).findFirst().orElse(null);
        }
        this.elementBuilders = elementBuilders.stream().sorted(Comparator.comparingInt(ElementBuilder::order)).toList();
        this.staticImports = new ArrayList<>();
    }
    public Element getElement(List<String> lines, int lineNumber) {
        String line = StringUtils.trim(lines.get(lineNumber));
        for (ElementBuilder factory:elementBuilders) {
            if (factory.isValid(line)) {
                return factory.buildFromLine(lines, lineNumber, this, builderName);
            }
        }

        return new HtmlElement(lines, lineNumber, this, builderName);
    }

    public Element getElement(String line) {
        return getElement(Collections.singletonList(line), 0);
    }

    public void defineNewFragment(String name) {
        fragmentOrStaticImportElementBuilder.attachNewFragment(name);
    }

    public void potentialFragmentCall(String fragmentName) {
        fragmentOrStaticImportElementBuilder.discoveredFragmentCall(fragmentName);
    }

    public void unknownFragmentsExists() {
        fragmentOrStaticImportElementBuilder.validateFragments();
    }

    public void addStaticImport(String staticImport) {
        this.staticImports.add(staticImport);
    }

    public boolean isStaticImport(String staticImport) {
        return this.staticImports.contains(staticImport);
    }

    public void setBuilderName(String builderName) {
        this.builderName = builderName;
    }

    public void resetBuilderName() {
        this.builderName = DEFAULT_BUILDER_NAME;
    }
}
