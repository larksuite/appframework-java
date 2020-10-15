package com.larksuite.appframework.sdk.core.protocol.client.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.larksuite.appframework.sdk.core.protocol.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xiaobozhang@aliyun.com
 * @since 2020/8/18
 */
@Getter
@Setter
@ToString
public class ListEventRequest extends BaseRequest {
    @JsonProperty("max_results")
    Integer maxResults;
    @JsonProperty("page_token")
    String pageToken;
    @JsonProperty("sync_token")
    String syncToken;
}
