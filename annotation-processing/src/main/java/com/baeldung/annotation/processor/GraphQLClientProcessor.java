package com.baeldung.annotation.processor;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

@SupportedAnnotationTypes("com.baeldung.annotation.processor.GraphQLClient")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class GraphQLClientProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (final TypeElement annotation : annotations) {
            var annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            for (final var element : annotatedElements) {
                var className = ((TypeElement) element)
                        .getQualifiedName()
                        .toString();
                var clientAnnotation = element.getAnnotation(GraphQLClient.class);

                var queries = clientAnnotation.queries();
                if (queries.isEmpty()) {
                    processingEnv
                            .getMessager()
                            .printMessage(Diagnostic.Kind.ERROR,
                                          "@" + annotation.getSimpleName() + ": Argument 'queries' cannot be empty!");
                }
                var schemaUrl = clientAnnotation.schemaUrl();

                if (schemaUrl.isEmpty()) {
                    processingEnv
                            .getMessager()
                            .printMessage(Diagnostic.Kind.ERROR,
                                          "@" + annotation.getSimpleName() + ": Argument 'schemaUrl' cannot be empty!");
                }

                var parser = new GraphQLParser(queries, schemaUrl);
                var classString = new ClassStringGenerator(className).getClassString(Set.of());

                try {
                    createFile(classString, className + "Impl");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    private void createFile(String classString, String fileName) throws IOException {


        JavaFileObject builderFile = processingEnv
                .getFiler()
                .createSourceFile(fileName);
        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            out.println(classString);
        }
    }


}



