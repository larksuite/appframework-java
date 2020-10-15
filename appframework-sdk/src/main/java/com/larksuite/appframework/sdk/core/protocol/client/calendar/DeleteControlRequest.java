package com.larksuite.appframework.sdk.core.protocol.client.calendar;

import com.larksuite.appframework.sdk.core.protocol.BaseResponse;
import lombok.Data;

/**
 *
 * @author zht
 */
@Data
public class DeleteControlRequest extends BaseResponse {
    private String roleId;
    private String calendarId;
}
