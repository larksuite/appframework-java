package com.larksuite.appframework.sdk.core.protocol.client.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.larksuite.appframework.sdk.core.protocol.BaseResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author xiaobozhang@aliyun.com
 * @since 2020/8/10
 */
@Getter
@Setter
@ToString
public class GroupListResponse extends BaseResponse {

    private Data data;

    @Getter
    @Setter
    @ToString
    public static class Data {
        @JsonProperty("has_more")
        private boolean hasMore;
        @JsonProperty("page_token")
        private String pageToken;

        List<GroupListItem> groups;
    }

    @Getter
    @Setter
    @ToString
    public static class GroupListItem {
        private String avatar;
        @JsonProperty("chat_id")
        private String chatId;
        private String description;
        private String name;
        @JsonProperty("owner_open_id")
        private String ownerOpenId;
        @JsonProperty("owner_user_id")
        private String ownerUserId;

    }
}
