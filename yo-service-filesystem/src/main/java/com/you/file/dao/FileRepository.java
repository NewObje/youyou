package com.you.file.dao;

import com.you.domain.filesystem.FileSystemEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Michael Liu
 * @create 2020-02-27 16:29
 */
public interface FileRepository extends MongoRepository<FileSystemEntity, String> {
}
