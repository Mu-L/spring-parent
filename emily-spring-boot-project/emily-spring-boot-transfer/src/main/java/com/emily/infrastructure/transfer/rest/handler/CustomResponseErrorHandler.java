package com.emily.infrastructure.transfer.rest.handler;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * 自定义异常处理
 *
 * @author Emily
 * @since 2020/08/18
 */
public class CustomResponseErrorHandler implements ResponseErrorHandler {

    /**
     * 判定响应是否有任何错误
     *
     * @param response 响应对象
     * @return true :返回的响应有错误，false无错误
     * @throws IOException 异常
     */
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return true;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
    }
}
