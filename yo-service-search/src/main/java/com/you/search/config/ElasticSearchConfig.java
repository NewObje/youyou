package com.you.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Michael Liu
 * @create 2020-04-08 12:46
 */
@Configuration
public class ElasticSearchConfig {

    @Value("${elasticsearch.hostlist}")
    private String hostList;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        //创建HttpHost数组，其中存放es主机和端口的配置信息
        String[] hosts = hostList.split(",");
        //创建HttpHost数组，其中存放es主机和端口的配置信息
        HttpHost[] httpHosts = new HttpHost[hosts.length];
        for (int i = 0; i < hosts.length; i++) {
            String item = hosts[i];
            httpHosts[i] = new HttpHost(item.split(":")[0], Integer.parseInt(item.split(":")[1]), "http");
        }
        //创建RestHighLevelClient客户端
        return new RestHighLevelClient(RestClient.builder(httpHosts));
    }

    @Bean
    public RestClient restClient() {
        //创建HttpHost数组，其中存放es主机和端口的配置信息
        String[] hosts = hostList.split(",");
        //创建HttpHost数组，其中存放es主机和端口的配置信息
        HttpHost[] httpHosts = new HttpHost[hosts.length];
        for (int i = 0; i < hosts.length; i++) {
            String item = hosts[i];
            httpHosts[i] = new HttpHost(item.split(":")[0], Integer.parseInt(item.split(":")[1]), "http");
        }
        return RestClient.builder(httpHosts).build();
    }
}
