package com.larksuite.appframework.sdk.core.protocol.client.contact;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.larksuite.appframework.sdk.core.protocol.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author xiaobozhang@aliyun.com
 * @since 2020/8/17
 */
@Getter
@Setter
@ToString
public class BatchUserRequest extends BaseRequest {
    @JsonProperty("employee_ids")
    private List<String> employeeIds;
    @JsonProperty("open_ids")
    private List<String> openIds;
}
