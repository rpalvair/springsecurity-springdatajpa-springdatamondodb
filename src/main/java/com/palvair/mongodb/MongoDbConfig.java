package com.palvair.mongodb;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by widdy on 20/12/2015.
 */
@Configuration
public class MongoDbConfig extends AbstractMongoConfiguration {
    @Override
    protected String getDatabaseName() {
        return "rpalvair";
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient();
    }


}
