/************************************************************************
 * Copyright PointCheckout Ltd.
 */
package com.pmnt.api.model;

import com.pmnt.api.request.AbstractCheckoutRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 *
 * @author Ankur
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Checkout {

        private String id;
        private String displayId;
        private String checkoutKey;
        private String requestId;
        private String orderId;
        private String currency;
        private BigDecimal amount;
        private BigDecimal cashAmount;
        private String branchId;
        private String branchName;
        private CheckoutStatus status;
        private String redirectUrl;
        private PaymentView usedPaymentMethod;
        private BigDecimal totalRefunded;
        private AbstractCheckoutRequest.CheckoutCustomer customer;
        private AbstractCheckoutRequest.CheckoutAddress billingAddress;
        private AbstractCheckoutRequest.CheckoutAddress deliveryAddress;
        private String firebaseDatabase;
        private String firebaseCollection;
        private String firebaseDocument;
        private DateTime timestamp;

        public enum CheckoutStatus {
                PENDING,
                AUTHORIZED,
                PENDING_CONFIRMATION,
                PAID,
                FAILED,
                CANCELLED,
                EXPIRED,
                REFUNDED,
                PARTIALLY_REFUNDED,
                CHARGEBACK;
        }

        public enum PaymentView {
                POINTCHECKOUT("PointCheckout", false),
                CARD("Debit/Credit Cards"),
                TABBY("Tabby"),
                CAREEM_PAY("Careem pay"),

                CRYPTO("Crypto"),
                SOLANA_PAY("Solana pay");

                @Getter
                @Setter
                private String label;

                @Getter
                @Setter
                private boolean hasDetails;

                PaymentView(String label) {
                        this(label, true);
                }

                PaymentView(String label, boolean hasDetails) {
                        this.label = label;
                        this.hasDetails = hasDetails;
                }


                @Override
                public String toString() {
                        switch (this) {
                                case CARD:
                                        return "Card";
                                default:
                                        return this.label;
                        }
                }

        }
}
