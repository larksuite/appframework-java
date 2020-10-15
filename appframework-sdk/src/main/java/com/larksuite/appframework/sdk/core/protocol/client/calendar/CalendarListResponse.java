package com.larksuite.appframework.sdk.core.protocol.client.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.larksuite.appframework.sdk.core.protocol.BaseResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author xiaobozhang@aliyun.com
 * @since 2020/8/18
 */
@Getter
@Setter
@ToString
public class CalendarListResponse extends BaseResponse {

    private List<Item> data;
    @JsonProperty("sync_token")
    private String syncToken;

    @Getter
    @Setter
    @ToString
    public static class Item {
        String id;
        String summary;
        String description;
        @JsonProperty("default_access_role")
        String defaultAccessRole;
        @JsonProperty("is_private")
        boolean isPrivate;
    }
}
