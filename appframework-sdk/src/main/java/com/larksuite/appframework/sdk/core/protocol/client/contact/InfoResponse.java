package com.larksuite.appframework.sdk.core.protocol.client.contact;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.larksuite.appframework.sdk.core.protocol.BaseResponse;
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
public class InfoResponse extends BaseResponse {

    private Data data;

    @Getter
    @Setter
    @ToString
    public static class Data {
        @JsonProperty("department_info")
        private DepartmentInfo departmentInfo;
    }

    @Getter
    @Setter
    @ToString
    public static class DepartmentInfo {
        private String id;

        @JsonProperty("leader_employee_id")
        private String leaderEmployeeId;

        @JsonProperty("leader_open_id")
        private String leaderOpenId;

        @JsonProperty("chat_id")
        private String chatId;

        @JsonProperty("member_count")
        private Integer memberCount;

        private String name;

        @JsonProperty("parent_id")
        private String parentId;

        private Integer status;
    }
}
