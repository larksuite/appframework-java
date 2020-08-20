package com.larksuite.appframework.sdk.core.protocol.client.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.larksuite.appframework.sdk.core.protocol.BaseResponse;
import lombok.Data;

/**
 *
 * @author zht
 */
@Data
public class DeleteControlRequest extends BaseResponse {
    private String roleId;
    @JsonProperty("Authorization")
    private String authorization;
    private String calendarId;

}
