package com.larksuite.appframework.sdk.core.protocol.client.bot;

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
public class BotInfoResponse extends BaseResponse {

    private Bot bot;

    @Getter
    @Setter
    @ToString
    public static class Bot {
        @JsonProperty("activate_status")
        private Double activateStatus;
        @JsonProperty("app_name")
        private String appName;
        @JsonProperty("avatar_url")
        private String avatarUrl;
        @JsonProperty("open_id")
        private String openId;
        @JsonProperty("ip_white_list")
        private List<String> ipWhiteList;
    }
}
