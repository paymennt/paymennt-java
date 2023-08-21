/************************************************************************
 * Copyright PointCheckout Ltd.
 */
package com.pmnt.api.request;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.pmnt.api.exception.PaymenntApiException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

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
    @Schema(description = "URL to redirect user to after a successful or failed payment.",
            example = "https://shop.example.com/payment-redirect/")
    private String returnUrl;

    public void validate() {
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
            throw new PaymenntApiException(HttpStatus.BAD_REQUEST, "Validation error: " + errorMessage);
        }
    }
}
