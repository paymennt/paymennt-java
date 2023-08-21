/************************************************************************
 * Copyright PointCheckout Ltd.
 */
package com.pmnt.api.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "A unique Identifier for this request, can be used later to query the status on the checkout.",
            example = "ORD-1234-r1")
    @Length(min = 1, max = 50)
    private String requestId;

    @NotBlank
    @Schema(description = "A merchant unique transaction ID for this checkout. This can be the order ID. Must match any reference shown to the user during checkout / order history.",
            example = "ORD-1234")
    @Length(min = 1, max = 50)
    private String orderId;

    @NotBlank
    @Length(min = 3, max = 3)
    @Schema(description = "ISO 4217 Currency Code (3 letter currency code).", example = "AED")
    private String currency;

    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    @Schema(description = "The amount customers must pay.", example = "100.0")
    private BigDecimal amount;

    @Size(min = 2, max = 3)
    @Schema(description = "2 Letter ISO 639-1 language code, for viewing the payment page. When the language is not supported, the system will revert to the default.",
            example = "EN")
    private String language;

    @Schema(description = "The paymennt branch ID associated with this checkout.")
    private Long branchId;

    @ArraySchema(schema = @Schema(example = "CARD"))
    @Schema(description = "Array containing payment methods to be available at the payment page. When left empty all available options will show. When provided the payment methods order in the payment page will follow the same order passed in this parameter. ")
    private List<PaymentMethod> allowedPaymentMethods;

    @Schema(description = "Active payment method when opening the payment page. When left empty the default for your account at paymennt will be used.",
            example = "CARD")
    private PaymentMethod defaultPaymentMethod;

    @Valid
    @Schema(description = "Order totals")
    private CheckoutRequestTotals totals;

    @JsonIgnore
    private Map<String, String> otherFields = new HashMap<>();

    @NotNull
    @Valid
    @Schema(description = "Details of the customer making the order.")
    private CheckoutCustomer customer;

    @NotNull
    @Valid
    @Schema(description = "Customer billing address.")
    private CheckoutAddress billingAddress;

    @Valid
    @Schema(description = "Customer delivery address.")
    private CheckoutAddress deliveryAddress;

    @Valid
    @NotEmpty
    @Schema(description = "An array of the items associated with this order.")
    private List<CheckoutItem> items;


    @Getter
    @Setter
    public static class CheckoutAddress implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        @NotBlank
        @Length(max = 100)
        @Schema(description = "Address person name.", example = "[NAME]")
        private String name;

        @NotBlank
        @Length(max = 255)
        @Schema(description = "Address line 1.", example = "[ADDRESS 1]")
        private String address1;

        @Length(max = 255)
        @Schema(description = "Address line 2.", example = "[ADDRESS 2]")
        private String address2;

        @NotBlank
        @Length(max = 50)
        @Schema(description = "Address city.", example = "[CITY]")
        private String city;

        @Length(max = 50)
        @Schema(description = "Address state.", example = "[STATE]")
        private String state;

        @Length(max = 20)
        @Schema(description = "Address zip code.", example = "12345")
        private String zip;

        @NotBlank
        @Length(min = 2, max = 3)
        @Schema(description = "Address country (ISO 3166-1 alpha-3 or alpha-2 country code)", example = "AE")
        private String country;

    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class CheckoutCustomer implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        @Length(max = 50)
        @Schema(description = "Customer identifier at the merchant system.", example = "123456")
        private String id;

        @NotBlank
        @Length(max = 60)
        @Schema(description = "Customer first name.", example = "[First name]")
        private String firstName;

        @NotBlank
        @Length(max = 40)
        @Schema(description = "Customer last name.", example = "[Last name]")
        private String lastName;

        @NotBlank
        @Length(max = 50)
        @Schema(description = "Customer email address.", example = "[CUSTOMER EMAIL]")
        private String email;

        @Length(max = 20)
        @Schema(description = "Customer phone number with international calling code (Ex. 971567xxxxxx)..",
                example = "[CUSTOMER PHONE]")
        private String phone;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckoutItem {

        Long id;

        @NotBlank
        @Length(max = 500)
        @Schema(description = "The name of the item being sold to the customer.", example = "Dark grey sunglasses")
        private String name;

        @Length(max = 200)
        @Schema(description = "Merchant item SKU.", example = "1116521")
        private String sku;

        @Schema(description = "Product unit price.", example="50.0")
        private BigDecimal unitprice;

        @DecimalMin(value = "0", inclusive = false)
        @NotNull
        @Schema(description = "Quantity of item being bought by the user.", example="2")
        private BigDecimal quantity;

        @NotNull
        @Schema(description = "Total selling price for quantity of this item.", example="100.0")
        private BigDecimal linetotal;

    }

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

    @Getter
    @Setter
    public static class CheckoutRequestTotals {


        @DecimalMin(value = "0", inclusive = true)
        @NotNull
        @Schema(description = "Sum of all individual line items (without discount / tax / shipping / etc).", example = "100.0")
        private BigDecimal subtotal;

        @DecimalMin(value = "0")
        @NotNull
        @Schema(description = "Total tax value on the order.", example = "5.0")
        private BigDecimal tax;

        @DecimalMin(value = "0")
        @Schema(description = "Total value of shipping.", example = "3.0")
        private BigDecimal shipping;

        @DecimalMin(value = "0")
        @Schema(description = "Handling fees for the order.", example = "2.0")
        private BigDecimal handling;

        @DecimalMin(value = "0")
        @Schema(description = "Value of discount applied to Subtotal.", example = "10.0")
        private BigDecimal discount;

        @Schema(description = "Disable totals validation.", example = "false", defaultValue = "true")
        private boolean skipTotalsValidation = true;
    }
}

