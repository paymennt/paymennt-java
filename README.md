# Paymennt Client SDK

The PaymentClient library facilitates the processing and management of payments for various applications. To leverage the capabilities of this library, clients are required to possess an API key and API secret. These credentials can be generated within the Payment Admin UI, granting access to the comprehensive functionality of the library.

Please ensure that you securely store and manage your API key and secret, as they are essential for establishing a secure and efficient connection to the payment system.

## How to use

- Open your project's configuration file (e.g., pom.xml for Maven). Navigate to the dependencies section. Add the following dependency to your project:
    ```sh
		<dependency>
			<groupId>com.paymennt</groupId>
			<artifactId>paymennt-client</artifactId>
			<version>1.0</version>
		</dependency>
    ```
- Connect to the PaymenntClient. Use the apiKey, apiSecret generated from the Paymennt admin. Use TEST, LIVE environment based on your requirements for testing or production.
    ```sh
		CheckoutOperations operations = new PaymenntClient("{API_KEY}", "{API_SECRET}", PaymenntClient.PaymenntEnvironment.TEST).checkoutOperations();
    ```
- To perform checkout operations, such as creating a checkout or retrieving checkout information, ensure that you provide valid parameters (refer official Paymennt API documentation).
    ```sh
	    /**
       * create checkout
       * webCheckoutRequest is the request body required by Paymennt api to create the checkout.
       */
		Checkout checkout = operations.createWebCheckout({webCheckoutRequest});

	    /**
       * get checkout details
       * use checkoutId retrieved from paymennt API.
       */
		Checkout checkout = operations.getCheckout({checkoutId});
    ```

## Official Paymennt API documentation
[Link](https://docs.paymennt.com/api#section/Introduction)
