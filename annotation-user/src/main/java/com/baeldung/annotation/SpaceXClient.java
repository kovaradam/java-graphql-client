package com.baeldung.annotation;

import com.baeldung.annotation.processor.GraphQLClient;


@GraphQLClient(schemaUrl = "./annotation-user/schema.graphql",
               //language=GraphQL
               queries = """
                         query getName {
                            me {
                                name                 
                            }
                         }
                         """)
public interface SpaceXClient {
}
