package com.baeldung.annotation.processor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GraphQLParser {

    private final String queries;

    private final String schemaUrl;

    public GraphQLParser(String queries, String schemaUrl) {
        this.queries = queries;
        this.schemaUrl = schemaUrl;
    }

    private String getSchema() {
        String schemaString = null;
        try {
            var path = Paths.get(schemaUrl);
            schemaString = new String(Files.readAllBytes(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return schemaString;
    }

    
}
