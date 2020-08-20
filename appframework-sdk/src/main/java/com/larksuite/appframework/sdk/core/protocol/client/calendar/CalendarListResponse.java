package com.larksuite.appframework.sdk.core.protocol.client.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.larksuite.appframework.sdk.core.protocol.BaseResponse;
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
public class CalendarListResponse extends BaseResponse {

    private Data data;

    @Getter
    @Setter
    @ToString
    public static class Data {
        private Item item;
    }

    @Getter
    @Setter
    @ToString
    public static class Item {
        String id;
        String summary;
        String description;
        @JsonProperty("default_access_role")
        String defaultAccessRole;
    }
}
