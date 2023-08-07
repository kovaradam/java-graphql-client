package com.baeldung.annotation.processor;

import java.text.MessageFormat;
import java.util.Set;

public class ClassStringGenerator {

    private final String className;

    public ClassStringGenerator(String className) {
        this.className = className;
    }

    public String getClassString(Set<String> methods) {
        var packageName = "";
        int lastDot = className.lastIndexOf('.');
        if (lastDot > 0) {
            packageName = MessageFormat.format("package {0};", className.substring(0, lastDot));
        }
        var interfaceName = className.substring(lastDot + 1);

        var methodsString = methods
                .stream()
                .reduce("", (prev, current) -> prev + "\n" + current);

        return """
               {0}
                                       
               public class {1}Impl implements {1} {
                   {2}
               }
               """
                .replace("{0}", packageName)
                .replace("{1}", interfaceName)
                .replace("{2}", methodsString);
    }

    private String getMethodString(String type, String name, String query) {
        return MessageFormat.format("""
                                    public {0} {1} () {
                                        final var query = "{2}";
                                        return query;
                                    }
                                    """, type, name, query);
    }


}
