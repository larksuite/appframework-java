package com.larksuite.appframework.sdk.core.protocol.client.calendar;

import com.larksuite.appframework.sdk.core.protocol.BaseRequest;
import lombok.Data;

/**
 *
 * @author zht
 */
@Data
public class CreateControlRequest extends BaseRequest {
    private String role;
    private ControlListResponse.Scope scope;
}
