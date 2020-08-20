package com.larksuite.appframework.sdk.core.protocol.client.contact;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.larksuite.appframework.sdk.core.protocol.BaseResponse;
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
public class ScopeResponse extends BaseResponse {

    private Data data;

    @Getter
    @Setter
    @ToString
    public static class Data {
        @JsonProperty("authed_departments")
        private List<String> authedDepartments;
        @JsonProperty("authed_employee_ids")
        public List<String> authedEmployeeIds;
        @JsonProperty("authed_open_ids")
        public List<String> authedOpenIds;
    }
}
