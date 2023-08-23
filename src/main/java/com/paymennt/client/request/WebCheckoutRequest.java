/************************************************************************
 * Copyright PointCheckout Ltd.
 */
package com.paymennt.client.request;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.paymennt.client.exception.PaymenntClientException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.Set;

/**
 * Represents a web checkout request, extending AbstractCheckoutRequest.
 * This class holds specific properties for creating a web checkout.
 * It provides validation for mandatory parameters required to create the web checkout.
 *
 * @author Ankur
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({
        "requestId", "orderId", "currency", "amount", "totals", "items", "customer",
        "billingAddress", "deliveryAddress", "returnUrl", "branchId", "allowedPaymentMethods",
        "defaultPaymentMethod", "language"
})
@Log4j2
public class WebCheckoutRequest extends AbstractCheckoutRequest {

    @NotBlank
    private String returnUrl;

    /**
     * Validates the mandatory parameters required to create the web checkout.
     *
     * @throws PaymenntClientException If parameter validation fails.
     */
    public void validate() throws PaymenntClientException {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<WebCheckoutRequest>> violations = validator.validate(this);

        StringBuilder errorMessage = new StringBuilder();
        if (!violations.isEmpty()) {
            errorMessage.append("Parameter validation failed:\n");
            for (ConstraintViolation<WebCheckoutRequest> violation : violations) {
                String fieldName = violation.getPropertyPath().toString();
                String message = violation.getMessage();
                errorMessage.append("- ").append(fieldName).append(": ").append(message).append("\n");
            }
            log.error("Validation error: {} for requestId: {}, orderId: {}", errorMessage, getRequestId(), getOrderId());
            throw new PaymenntClientException("Validation error: " + errorMessage);
        }
        log.info("Web checkout request is valid. RequestId: {}, orderId: {}", getRequestId(), getOrderId());
    }
}
