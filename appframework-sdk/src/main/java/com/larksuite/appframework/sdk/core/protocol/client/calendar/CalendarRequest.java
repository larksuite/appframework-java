package com.larksuite.appframework.sdk.core.protocol.client.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author xiaobozhang@aliyun.com
 * @since 2020/8/18
 */
@Data
public class CalendarRequest {
    private String summary;

    private String description;

    @JsonProperty("is_private")
    private Boolean isPrivate;

    @JsonProperty("default_access_role")
    private String defaultAccessRole;
}
