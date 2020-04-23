package com.you.cmsclient.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Michael Liu
 * @create 2020-03-05 17:48
 */
@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.database}")
    String database;

    @Bean
    public GridFSBucket getGridFSBucket(MongoClient client) {
        MongoDatabase database = client.getDatabase(this.database);
        GridFSBucket bucket = GridFSBuckets.create(database);
        return bucket;
    }
}
