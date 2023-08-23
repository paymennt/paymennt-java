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
 * Represents a generic API response.
 * This class encapsulates the response structure returned from API calls.
 * It includes properties for success status, elapsed time, error message, and result.
 * The class is generic, allowing you to specify the type of the result.
 *
 * @param <T> The type of the result in the API response.
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
