package com.example.test;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpClientPoolUtil {
    private static PoolingHttpClientConnectionManager cm;

    private static void init() {
        if (cm == null) {
            cm = new PoolingHttpClientConnectionManager();
            // 整个连接池最大连接数
            cm.setMaxTotal(12000);
            // 每路由最大连接数，默认值是 2
            cm.setDefaultMaxPerRoute(11000);
        }
    }

    /**
     * 通过连接池获取 HttpClient
     *
     * @return
     */
    public static CloseableHttpClient getHttpClient() {
        init();
        return HttpClients.custom().setConnectionManager(cm).build();
    }
}
