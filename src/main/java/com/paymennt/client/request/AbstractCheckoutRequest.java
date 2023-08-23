/************************************************************************
 * Copyright PointCheckout Ltd.
 */
package com.paymennt.client.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an abstract checkout request.
 * This class holds various properties and nested classes related to checkout requests.
 * It is meant to be extended for specific types of checkout requests.
 *
 * @author Ankur
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({
        "requestId", "orderId", "currency", "amount", "totals", "branchId", "allowedPaymentMethods",
        "defaultPaymentMethod", "language"
})
public class AbstractCheckoutRequest {

    @NotBlank
    @Length(min = 1, max = 50)
    private String requestId;

    @NotBlank
    @Length(min = 1, max = 50)
    private String orderId;

    @NotBlank
    @Length(min = 3, max = 3)
    private String currency;

    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal amount;

    @Size(min = 2, max = 3)
    private String language;

    private Long branchId;

    private List<PaymentMethod> allowedPaymentMethods;

    private PaymentMethod defaultPaymentMethod;

    @Valid
    private CheckoutRequestTotals totals;

    @JsonIgnore
    private Map<String, String> otherFields = new HashMap<String,String>();

    @NotNull
    @Valid
    private CheckoutCustomer customer;

    @NotNull
    @Valid
    private CheckoutAddress billingAddress;

    @Valid
    private CheckoutAddress deliveryAddress;

    @Valid
    @NotEmpty
    private List<CheckoutItem> items;

    /**
     * Represents a checkout address.
     */
    @Getter
    @Setter
    public static class CheckoutAddress implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        @NotBlank
        @Length(max = 100)
        private String name;

        @NotBlank
        @Length(max = 255)
        private String address1;

        @Length(max = 255)
        private String address2;

        @NotBlank
        @Length(max = 50)
        private String city;

        @Length(max = 50)
        private String state;

        @Length(max = 20)
        private String zip;

        @NotBlank
        @Length(min = 2, max = 3)
        private String country;
    }

    /**
     * Represents a checkout customer.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    public static class CheckoutCustomer implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        @Length(max = 50)
        private String id;

        @NotBlank
        @Length(max = 60)
        private String firstName;

        @NotBlank
        @Length(max = 40)
        private String lastName;

        @NotBlank
        @Length(max = 50)
        private String email;

        @Length(max = 20)
        private String phone;
    }

    /**
     * Represents a checkout item.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckoutItem {

        Long id;

        @NotBlank
        @Length(max = 500)
        private String name;

        @Length(max = 200)
        private String sku;

        private BigDecimal unitprice;

        @DecimalMin(value = "0", inclusive = false)
        @NotNull
        private BigDecimal quantity;

        @NotNull
        private BigDecimal linetotal;
    }

    /**
     * Represents a payment method.
     */
    public enum PaymentMethod {
        CARD,
        CRYPTO,
        POINTCHECKOUT,
        VISA,
        MASTERCARD,
        AMEX,
        UNIONPAY,
        TABBY,
        CAREEM_PAY,
        MADA;
    }

    /**
     * Represents checkout request totals.
     */
    @Getter
    @Setter
    public static class CheckoutRequestTotals {

        @DecimalMin(value = "0", inclusive = true)
        @NotNull
        private BigDecimal subtotal;

        @DecimalMin(value = "0")
        @NotNull
        private BigDecimal tax;

        @DecimalMin(value = "0")
        private BigDecimal shipping;

        @DecimalMin(value = "0")
        private BigDecimal handling;

        @DecimalMin(value = "0")
        private BigDecimal discount;

        private boolean skipTotalsValidation = true;
    }
}

