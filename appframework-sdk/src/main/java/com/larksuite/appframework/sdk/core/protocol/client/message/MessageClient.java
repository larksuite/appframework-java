package com.larksuite.appframework.sdk.core.protocol.client.message;

import com.larksuite.appframework.sdk.core.protocol.*;
import com.larksuite.appframework.sdk.exception.LarkClientException;
import com.larksuite.appframework.sdk.utils.HttpClient;
import com.larksuite.appframework.sdk.utils.HttpException;
import com.larksuite.appframework.sdk.utils.JsonUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.larksuite.appframework.sdk.core.protocol.OpenApiClient.DEFAULT_CONN_TIMEOUT;

/**
 * @author xiaobozhang@aliyun.com
 * @since 2020/8/20
 */
public class MessageClient extends OpenApiClient.AbstractSubClient {

    /**
     * message
     */
    private static final String SendMessagePath = "/open-apis/message/v4/send/";
    private static final String SendMessageBatchPath = "/open-apis/message/v4/batch_send/";
    private static final String UploadImagePath = "/open-apis/image/v4/put/";
    private static final String FetchImagePath = "/open-apis/image/v4/get";


    public SendMessageResponse sendMessage(String tenantAccessToken, SendMessageRequest req) throws LarkClientException {
        return call(() -> getHttpClient().doPostJson(getBasePath() + SendMessagePath, DEFAULT_CONN_TIMEOUT, 5000, createHeaderWithAuthorization(tenantAccessToken), JsonUtil.larkFormatToJsonString(req)), SendMessageResponse.class);
    }

    public SendMessageBatchResponse sendMessageBatch(String tenantAccessToken, SendMessageBatchRequest req) throws LarkClientException {
        return call(() -> getHttpClient().doPostJson(getBasePath() + SendMessageBatchPath, DEFAULT_CONN_TIMEOUT, 5000, createHeaderWithAuthorization(tenantAccessToken), JsonUtil.larkFormatToJsonString(req)), SendMessageBatchResponse.class);
    }


    public UploadImageResponse uploadImage(String tenantAccessToken, UploadImageRequest req) throws LarkClientException {
        return call(() -> {
            List<HttpClient.Field> fl = new ArrayList<>(2);
            fl.add(new HttpClient.FileField("image", req.getImageFile(), req.getOriginName()));
            fl.add(new HttpClient.DataField("image_type", "message"));

            return getHttpClient().doPostFile(getBasePath() + UploadImagePath,
                    DEFAULT_CONN_TIMEOUT, 5000,
                    createHeaderWithAuthorization(tenantAccessToken), fl);

        }, UploadImageResponse.class);
    }

    public InputStream fetchImage(String tenantAccessToken, FetchImageRequest req) throws LarkClientException {
        try {
            return getHttpClient().doGetFile(buildGetUrl(getBasePath() + FetchImagePath, req), DEFAULT_CONN_TIMEOUT, 3000, createHeaderWithAuthorization(tenantAccessToken));
        } catch (HttpException e) {
            throw translateToLarkClientException(e);
        }
    }

    protected MessageClient(OpenApiClient parent) {
        super(parent);
    }
}
