package com.you.domain.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @Author: MichaelLiu
 * @Description:
 * @Date:Created in 2019/1/24 10:04.
 * @Modified By:
 */
@Data
@ToString
@Document(collection = "cms_page")
public class CmsPage {
    /**
     * 页面名称、别名、访问地址、类型（静态/动态）、页面模版、状态
     */
    //站点ID
    @ApiModelProperty("站点id")
    private String siteId;

    //页面ID
    @Id
    private String pageId;

    //页面名称
    @ApiModelProperty("页面名称")
    private String pageName;

    //别名
    @ApiModelProperty("别名")
    private String pageAliase;

    //访问地址
    @ApiModelProperty("访问地址")
    private String pageWebPath;

    //参数
    @ApiModelProperty("参数")
    private String pageParameter;

    //物理路径
    @ApiModelProperty("物理路径")
    private String pagePhysicalPath;

    //类型（静态/动态）
    @ApiModelProperty("类型（静态/动态)")
    private String pageType;

    //页面模版
    @ApiModelProperty("页面模版")
    private String pageTemplate;

    //页面静态化内容
    @ApiModelProperty("页面静态化内容")
    private String pageHtml;

    //状态
    @ApiModelProperty("状态")
    private String pageStatus;

    //创建时间
    @ApiModelProperty("创建时间")
    private Date pageCreateTime;

    //模版id
    @ApiModelProperty("模版id")
    private String templateId;

    //参数列表
    @ApiModelProperty("参数列表")
    private List<CmsPageParam> pageParams;

    //静态文件Id
    @ApiModelProperty("静态文件Id")
    private String htmlFileId;

    //数据Url
    @ApiModelProperty("数据Url")
    private String dataUrl;

}
