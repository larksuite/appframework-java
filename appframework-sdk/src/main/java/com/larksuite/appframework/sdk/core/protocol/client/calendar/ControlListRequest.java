package com.larksuite.appframework.sdk.core.protocol.client.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.larksuite.appframework.sdk.core.protocol.BaseRequest;
import lombok.Data;

/**
 *
 * @author zht
 */
@Data
public class ControlListRequest extends BaseRequest {

    @JsonProperty("Authorization")
    private String authorization;
    private String calendarId;
}
