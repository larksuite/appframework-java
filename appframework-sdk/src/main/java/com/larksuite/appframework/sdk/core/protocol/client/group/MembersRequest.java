package com.larksuite.appframework.sdk.core.protocol.client.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.larksuite.appframework.sdk.core.protocol.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xiaobozhang@aliyun.com
 * @since 2020/8/11
 */
@Getter
@Setter
@ToString
public class MembersRequest extends BaseRequest {
    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("page_size")
    private Integer pageSize = 100;

    @JsonProperty("page_token")
    private String pageToken;

}
