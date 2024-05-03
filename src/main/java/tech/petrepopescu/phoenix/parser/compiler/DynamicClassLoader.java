package tech.petrepopescu.phoenix.parser.compiler;

import org.springframework.stereotype.Component;

@Component
public class DynamicClassLoader extends ClassLoader {
    public Class<?> loadClass(String className, byte[] bytecode) {
        return defineClass(className, bytecode, 0, bytecode.length);
    }
}
