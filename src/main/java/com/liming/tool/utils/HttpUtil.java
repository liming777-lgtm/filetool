package com.liming.tool.utils;

import com.alibaba.fastjson2.JSONObject;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    public static final String BASE_URL = "http://liming777.xyz:8080";

    private HttpUtil() {
    }

    private static final CloseableHttpClient client;
    private static final HttpClientResponseHandler<String> responseHandler = new BasicHttpClientResponseHandler();

    static {
        ConnectionConfig connectionConfig = ConnectionConfig.custom().setConnectTimeout(Timeout.ofSeconds(5)).build();
        PoolingHttpClientConnectionManager manager = PoolingHttpClientConnectionManagerBuilder.create()
                .setDefaultConnectionConfig(connectionConfig).build();
        RequestConfig.custom().setConnectionRequestTimeout(Timeout.ofSeconds(3));
        client = HttpClients.custom()
                .setConnectionManager(manager)
                .build();
    }

    public static void closeClient() {
        try {
            client.close();
        } catch (IOException e) {
            LOGGER.error("http客户端关闭失败");
        }
    }

    public static String request(JSONObject json, HttpUriRequestBase httpMethod) {
        httpMethod.addHeader("userInfo","9YY46z3jfQ371ZQ9");
        if (json != null) {
            httpMethod.setEntity(new StringEntity(json.toString(), ContentType.APPLICATION_JSON));
        }
        try {
            return client.execute(httpMethod, responseHandler);
        } catch (IOException e) {
            LOGGER.error("请求失败,参数{}", json, e);
        }
        return "";
    }

}
