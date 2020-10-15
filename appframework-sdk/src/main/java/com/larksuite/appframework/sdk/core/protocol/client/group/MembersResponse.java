package com.larksuite.appframework.sdk.core.protocol.client.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.larksuite.appframework.sdk.core.protocol.BaseResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author xiaobozhang@aliyun.com
 * @since 2020/8/11
 */
@Getter
@Setter
@ToString
public class MembersResponse extends BaseResponse {

    private Data data;

    @Getter
    @Setter
    @ToString
    public static class Data {
        @JsonProperty("chat_id")
        private String chatId;
        @JsonProperty("has_more")
        private Boolean hasMore;
        List<Member> members;
    }

    @Getter
    @Setter
    @ToString
    public static class Member {
        @JsonProperty("open_id")
        private String openId;
        @JsonProperty("user_id")
        private String userId;
        private String name;
    }
}
