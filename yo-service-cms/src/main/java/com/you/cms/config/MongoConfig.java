package com.you.cms.config;

import com.mongodb.MongoClient;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.*;
import com.mongodb.client.gridfs.model.GridFSDownloadByNameOptions;
import com.mongodb.client.gridfs.model.GridFSDownloadOptions;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.session.ClientSession;
import org.bson.BsonValue;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Michael Liu
 * @create 2020-02-28 22:42
 */
@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.database}")
    String db;

    /**
     * 打开下载流对象
     * @param mongoClient
     * @return
     */
    @Bean
    public GridFSBucket getGridFSBucket(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase(db);
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);
        return gridFSBucket;
    }
}
