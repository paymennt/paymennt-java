/************************************************************************
 * Copyright PointCheckout Ltd.
 */
package com.paymennt.client.response;

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
public class ApiResponse<T> {
    @JsonProperty("success")
    private boolean success;

    @JsonProperty("elapsed")
    private long elapsed;

    @JsonProperty("error")
    private String error;

    @JsonProperty("result")
    private T result;
}