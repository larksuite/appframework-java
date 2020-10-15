package com.larksuite.appframework.sdk.core.protocol.client.contact;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.larksuite.appframework.sdk.core.protocol.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xiaobozhang@aliyun.com
 * @since 2020/8/17
 */
@Getter
@Setter
@ToString
public class UserDetailRequest extends BaseRequest {
    @JsonProperty("department_id")
    private String departmentId;
    private Integer offset = 0;
    @JsonProperty("page_size")
    private Integer pageSize = 100;
    @JsonProperty("fetch_child")
    private Boolean fetchChild;
}
