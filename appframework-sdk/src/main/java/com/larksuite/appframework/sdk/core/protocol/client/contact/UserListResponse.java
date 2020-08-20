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
public class UserListResponse extends BaseResponse {

    private Data data;

    @Getter
    @Setter
    @ToString
    public static class Data {
        @JsonProperty("has_more")
        private boolean hasMore;
        @JsonProperty("user_list")
        private List<User> userList;
    }

    @Getter
    @Setter
    @ToString
    public static class User {
        @JsonProperty("employee_id")
        private String employeeId;

        @JsonProperty("open_id")
        private String openId;

        private String name;

        @JsonProperty("employee_no")
        private String employeeNo;
    }
}
