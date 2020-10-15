package com.larksuite.appframework.sdk.core.protocol.client.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.larksuite.appframework.sdk.core.protocol.BaseRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xiaobozhang@aliyun.com
 * @since 2020/8/10
 */
@Getter
@Setter
public class GroupListRequest extends BaseRequest {

    @JsonProperty("page_size")
    private Integer pageSize;
    @JsonProperty("page_token")
    private String pageToken;
}
