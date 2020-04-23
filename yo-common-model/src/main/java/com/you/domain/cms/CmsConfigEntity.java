package com.you.domain.cms;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by MichaelLiu on 2019/2/6.
 */
@Data
@ToString
@Document(collection = "cms_config")
public class CmsConfigEntity {

    @Id
    private String id;

    private String name;

    private List<CmsConfigModel> model;

}
