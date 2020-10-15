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
public class SimpleResponse extends BaseResponse {

    private Data data;

    @Getter
    @Setter
    @ToString
    public static class Data {
        @JsonProperty("has_more")
        private boolean hasMore;
        @JsonProperty("department_infos")
        private List<DepartmentInfo> departmentInfos;
    }

    @Getter
    @Setter
    @ToString
    public static class DepartmentInfo {
        private String id;
        private String name;
        @JsonProperty("parent_id")
        private String parentId;
    }
}
