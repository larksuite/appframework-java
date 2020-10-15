package com.larksuite.appframework.sdk.core.protocol.client.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.larksuite.appframework.sdk.core.protocol.BaseResponse;
import lombok.Data;

import java.util.List;

/**
 *
 * @author zht
 */
@Data
public class ControlListResponse extends BaseResponse {

    private List<ControlItem> data;

    @Data
   public static class ControlItem{
        private Scope scope;
        private String role;
   }

   @Data
   public static class Scope{

        private String type;

        @JsonProperty("open_id")
        private String openId;

        @JsonProperty("employee_id")
        private String employeeId;
   }
}
