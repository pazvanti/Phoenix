package tech.petrepopescu.phoenix.parser;

import java.util.HashMap;
import java.util.Map;

public class VariableRegistry {
    private final Map<String, Map<String, String>> argumentsByFileAndType;
    private static final VariableRegistry INSTANCE = new VariableRegistry();
    private VariableRegistry() {
        argumentsByFileAndType  = new HashMap<>();
    }

    public static VariableRegistry getInstance() {
        return INSTANCE;
    }

    public void add(String fileName, String variableName, String variableType) {
        Map<String, String> variablesForFile = argumentsByFileAndType.get(fileName);
        if (variablesForFile == null) {
            variablesForFile = new HashMap<>();
            argumentsByFileAndType.put(fileName, variablesForFile);
        }
        variablesForFile.put(variableName, variableType);
    }

    public String getType(String fileName, String variableName) {
        Map<String, String> variablesForFile = argumentsByFileAndType.get(fileName);
        if (variablesForFile == null) {
            return null;
        }
        return variablesForFile.get(variableName);
    }
}
