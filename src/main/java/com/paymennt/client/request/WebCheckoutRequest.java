/************************************************************************
 * Copyright PointCheckout Ltd.
 */
package com.paymennt.client.request;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.paymennt.client.exception.PaymenntClientException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 *
 * @author Ankur
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({
        "requestId", "orderId", "currency", "amount","totals", "items", "customer",
        "billingAddress", "deliveryAddress", "returnUrl", "branchId", "allowedPaymentMethods",
        "defaultPaymentMethod",  "language"
})
public class WebCheckoutRequest extends AbstractCheckoutRequest {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    @NotBlank
    private String returnUrl;

    public void validate() throws PaymenntClientException {
        Validator validator = VALIDATOR_FACTORY.getValidator();
        Set<ConstraintViolation<WebCheckoutRequest>> violations = validator.validate(this);

        StringBuilder errorMessage = new StringBuilder();
        if (!violations.isEmpty()) {
            errorMessage.append("Parameter validation failed:\n");
            for (ConstraintViolation<WebCheckoutRequest> violation : violations) {
                String fieldName = violation.getPropertyPath().toString();
                String message = violation.getMessage();
                errorMessage.append("- ").append(fieldName).append(": ").append(message).append("\n");
            }
            throw new PaymenntClientException("Validation error: " + errorMessage);
        }
    }
}
