package com.larksuite.appframework.sdk.core.protocol.client.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author xiaobozhang@aliyun.com
 * @since 2020/8/18
 */
@Data
public class CalendarUpdateRequest {
    private String summary;

    private String description;

    @JsonProperty("default_access_role")
    private String defaultAccessRole;
}
