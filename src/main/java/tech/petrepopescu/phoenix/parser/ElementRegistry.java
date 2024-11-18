package tech.petrepopescu.phoenix.parser;

import tech.petrepopescu.phoenix.parser.elements.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElementRegistry {
    private ElementRegistry() {}
    private static final ElementRegistry INSTANCE = new ElementRegistry();

    public Map<String, List<Element>> elementsToSection = new HashMap<>();

    public static ElementRegistry getInstance() {
        return INSTANCE;
    }

    public void register(String section, Element element) {
        List<Element> elements = elementsToSection.computeIfAbsent(section, k -> new ArrayList<>());
        elements.add(element);
    }

    public void register(String section, List<Element> elements) {
        List<Element> existingElements = elementsToSection.computeIfAbsent(section, k -> new ArrayList<>());
        existingElements.addAll(elements);
    }

    public List<Element> getElementsForSection(String section) {
        return this.elementsToSection.get(section);
    }
}
