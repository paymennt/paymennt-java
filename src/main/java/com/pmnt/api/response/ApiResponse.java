/************************************************************************
 * Copyright PointCheckout Ltd.
 */
package com.pmnt.api.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Ankur
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse {
    @JsonProperty("success")
    private boolean success = true;

    @JsonProperty("elapsed")
    private long elapsed = 10;

    @JsonProperty("result")
    @JsonAlias("error")
    private Object result;

}