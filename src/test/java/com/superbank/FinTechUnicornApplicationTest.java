/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.superbank;

import static org.junit.Assert.assertEquals;
import static spark.Spark.awaitInitialization;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import com.superbank.dao.model.Account;
import com.superbank.dao.model.Transaction;
import com.superbank.utils.ConversionUtils;


public class FinTechUnicornApplicationTest {
	
	private String HOST_URL = "http://localhost:4567";

	private static final Logger LOGGER = LogManager.getLogger(FinTechUnicornApplicationTest.class);
	
	private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();
    
    @Before
    public void setup() {
    	FinTechUnicornApplication.main(null);
        awaitInitialization();
    }
    
	@Test public void testGetAccount_200() throws IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(HOST_URL + "/account/1"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        logResponse(response);

    	assertEquals(200, response.statusCode());
    }
    
	@Test public void testGetAccount_404_Empty() throws IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(HOST_URL + "/account/123"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        logResponse(response);

    	assertEquals(404, response.statusCode());
    }
    
	@Test public void testGetAccount_422_badinput() throws IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(HOST_URL + "/account/asd"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        logResponse(response);

    	assertEquals(422, response.statusCode());
    }
    
	@Test public void testPostAccount_400() throws IOException, InterruptedException{
		Account account = new Account();
		account.setId(42);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(ConversionUtils.buildFormData(account))
                .uri(URI.create(HOST_URL + "/account"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        logResponse(response);

    	assertEquals(HttpStatus.BAD_REQUEST_400, response.statusCode());
    }

	@Test public void testPostAccount_201() throws IOException, InterruptedException{
		Account account = new Account();
		account.setCurrency("EUR");
		account.setEmail("email@gmail.com");
		account.setPhone("1234567890");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(ConversionUtils.buildFormData(account))
                .uri(URI.create(HOST_URL + "/account"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        logResponse(response);

    	assertEquals(HttpStatus.CREATED_201, response.statusCode());
    }

	@Test public void testGetAllAccounts_200() throws IOException, InterruptedException{

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(HOST_URL + "/accounts"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        logResponse(response);

    	assertEquals(HttpStatus.OK_200, response.statusCode());
    }

	@Test public void testAccountCreation() throws IOException, InterruptedException{
		Account account = new Account();
		account.setCurrency("EUR");
		account.setEmail("email@gmail.com");
		account.setPhone("1234567890");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(ConversionUtils.buildFormData(account))
                .uri(URI.create(HOST_URL + "/account"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        logResponse(response);

    	assertEquals(HttpStatus.CREATED_201, response.statusCode());
    	
    	Account accountCreated = ConversionUtils.fromJson(response.body(), Account.class);
    	
    	HttpRequest requestGet = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(HOST_URL + "/account/" + accountCreated.getId()))
                .build();

        HttpResponse<String> responseGet = httpClient.send(requestGet, HttpResponse.BodyHandlers.ofString());

        logResponse(responseGet);

    	assertEquals(200, responseGet.statusCode());
    }

	@Test public void testGetAllTransactions_200() throws IOException, InterruptedException{
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(HOST_URL + "/transactions/1"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        logResponse(response);

    	assertEquals(HttpStatus.OK_200, response.statusCode());
    }

	@Test public void testPostTransaction_201_cleared() throws IOException, InterruptedException{
		Transaction transaction = new Transaction();
		transaction.setFromAccount(1);
		transaction.setToAccount(2);
		transaction.setCurrency("EUR");
		transaction.setAmount(BigDecimal.TEN);
		transaction.setStatus("CLEARED");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(ConversionUtils.buildFormData(transaction))
                .uri(URI.create(HOST_URL + "/transaction"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        logResponse(response);

    	assertEquals(HttpStatus.CREATED_201, response.statusCode());
    }

	@Test public void testPostTransaction_400() throws IOException, InterruptedException{
		Transaction transaction = new Transaction();

        HttpRequest request = HttpRequest.newBuilder()
                .POST(ConversionUtils.buildFormData(transaction))
                .uri(URI.create(HOST_URL + "/transaction"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        logResponse(response);

    	assertEquals(HttpStatus.BAD_REQUEST_400, response.statusCode());
    }

	@Test public void testPostTransaction_201_pending() throws IOException, InterruptedException{
		HttpRequest requestAccount1 = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(HOST_URL + "/account/1"))
                .build();
        HttpResponse<String> responseAccount1 = httpClient.send(requestAccount1, HttpResponse.BodyHandlers.ofString());
        Account account1Before = ConversionUtils.fromJson(responseAccount1.body(), Account.class);

		HttpRequest requestAccount2 = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(HOST_URL + "/account/2"))
                .build();
        HttpResponse<String> responseAccount2 = httpClient.send(requestAccount2, HttpResponse.BodyHandlers.ofString());
        Account account2Before = ConversionUtils.fromJson(responseAccount2.body(), Account.class);
		
		Transaction transaction = new Transaction();
		transaction.setFromAccount(1);
		transaction.setToAccount(2);
		transaction.setCurrency("EUR");
		transaction.setAmount(BigDecimal.TEN);
		transaction.setStatus("PENDING");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(ConversionUtils.buildFormData(transaction))
                .uri(URI.create(HOST_URL + "/transaction"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        logResponse(response);

    	assertEquals(HttpStatus.CREATED_201, response.statusCode());
    	

        HttpResponse<String> responseAccount1After = httpClient.send(requestAccount1, HttpResponse.BodyHandlers.ofString());
        Account account1After = ConversionUtils.fromJson(responseAccount1After.body(), Account.class);
        HttpResponse<String> responseAccount2After = httpClient.send(requestAccount2, HttpResponse.BodyHandlers.ofString());
        Account account2After = ConversionUtils.fromJson(responseAccount2After.body(), Account.class);
        
    	assertEquals(account1Before.getBalance().subtract(BigDecimal.TEN), account1After.getBalance());
    	assertEquals(account2Before.getBalance().add(BigDecimal.TEN), account2After.getBalance());
    }
	
	private void logResponse (HttpResponse<String> response) {
        LOGGER.info("Response CODE [" + response.statusCode() + "] | BODY [" + response.body() + "]");
	}
	
}
