package com.larksuite.appframework.sdk.core.protocol.client.contact;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.larksuite.appframework.sdk.core.protocol.BaseResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class UserDetailResponse extends BaseResponse {

    private Data data;

    @Getter
    @Setter
    @ToString
    public static class Data {
        @JsonProperty("has_more")
        private boolean hasMore;
        @JsonProperty("user_infos")
        private List<Detail> userInfos;
    }

    @Getter
    @Setter
    @ToString
    public static class Detail {
        private String name;

        @JsonProperty("name_py")
        private String namePy;

        @JsonProperty("en_name")
        private String enName;

        @JsonProperty("employee_id")
        private String employeeId;

        @JsonProperty("employee_no")
        private String employeeNo;

        @JsonProperty("open_id")
        private String openId;

        private int status;

        @JsonProperty("employee_type")
        private int employeeType;

        @JsonProperty("avatar_72")
        private String avatar72;

        @JsonProperty("avatar_240")
        private String avatar240;

        @JsonProperty("avatar_640")
        private String avatar640;

        @JsonProperty("avatar_url")
        private String avatarUrl;

        private int gender;

        private String email;

        private String mobile;

        private String description;

        private String country;

        private String city;

        @JsonProperty("work_station")
        private String workStation;

        @JsonProperty("is_tenant_manager")
        private Boolean isTenantManager;

        @JsonProperty("join_time")
        private Long joinTime;

        @JsonProperty("update_time")
        private Long updateTime;

        @JsonProperty("leader_employee_id")
        private String leaderEmployeeId;

        @JsonProperty("leader_open_id")
        private String leaderOpenId;

        private List<String> departments;

        @JsonProperty("custom_attrs")
        private Map<String, Object> customAttrs;
    }
}
