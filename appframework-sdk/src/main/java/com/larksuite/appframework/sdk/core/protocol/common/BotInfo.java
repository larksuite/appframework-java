package com.larksuite.appframework.sdk.core.protocol.common;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class BotInfo {
    /**
     * app 当前状态。
     * 0: 初始化，租户待安装,
     * 1: 租户停用, 2: 租户启用,
     * 3: 安装后待启用, 4: 升级待启用,
     * 5: license过期停用, 6: Lark套餐到期或降级停用,
     */
    Integer activateStatus;
    /**
     * 	app 名称
     */
    String appName;
    // String app_name;
    /**
     * app 图像地址
     */
    String avatarUrl;
    /**
     * app 的 IP 白名单地址
     */
    List<String> ipWhiteList;
    /**
     * 	机器人的open_id
     */
    String openId;
}
