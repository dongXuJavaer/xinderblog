package com.xinder.api.response.dto.qqlogin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Xinder
 * @date 2023-01-30 13:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class IdsDto implements Serializable {

    @JsonProperty("unionid")
    private String unionid;

    @JsonProperty("openid")
    private String openId;

    @JsonProperty("client_id")
    private String clientId;
}
