package tech.petrepopescu.phoenix.parser;

import tech.petrepopescu.phoenix.parser.route.ByteCodeObject;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ByteCodeGeneratingFileManager extends ForwardingJavaFileManager<JavaFileManager> {
    private final Map<String, ByteCodeObject> storedBytecodeObjects = new HashMap<>();

    public ByteCodeGeneratingFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
       ByteCodeObject object = new ByteCodeObject(className, kind);
       storedBytecodeObjects.put(className, object);
       return object;
    }

    public byte[] getBytecode(String className) {
        return storedBytecodeObjects.get(className).getBytecode().toByteArray();
    }

    public Map<String, ByteCodeObject> getBytecodeObjects() {
        return storedBytecodeObjects;
    }
}