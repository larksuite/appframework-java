/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client;

import com.larksuite.appframework.sdk.core.protocol.common.User;
import java.util.ArrayList;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BatchMessageDestination {

    private List<String> departmentIds;

    private List<String> openIds;

    private List<String> userIds;

    public void setUsers(List<User> users){
      List<String> cache =  users.stream().map(user->user.getUserId()).collect(Collectors.toList());
      if(userIds!=null){
          userIds.addAll(cache);
      }else{
          userIds = cache;
      }
    }

}
