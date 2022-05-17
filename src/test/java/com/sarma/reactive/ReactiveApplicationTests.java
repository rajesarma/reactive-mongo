package com.sarma.reactive;

import com.sarma.reactive.controller.CustomerController;
import com.sarma.reactive.dto.CustomerDto;
import com.sarma.reactive.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(CustomerController.class)
class ReactiveApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private CustomerService customerService;

	@Test
	public void getCustomers() {
		Flux<CustomerDto> customerDtos = Flux.just(
				new CustomerDto("ABC101", "Customer1",  10),
				new CustomerDto("ABC102", "Customer2",  20),
				new CustomerDto("ABC103", "Customer3",  30)
		);
		when(customerService.getAllCustomers()).thenReturn(customerDtos);

		Flux<CustomerDto> responseBody = webTestClient.get()
				.uri("/customer")
				.exchange()
				.expectStatus()
				.isOk()
				.returnResult(CustomerDto.class)
				.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNext(new CustomerDto("ABC101", "Customer1",  10)) // we have 3 objects here
				.expectNext(new CustomerDto("ABC102", "Customer2",  20)) // we have 3 objects here
				.expectNext(new CustomerDto("ABC103", "Customer3",  30)) // we have 3 objects here
				.verifyComplete();
	}

	@Test
	public void getCustomerById() {
		Mono<CustomerDto> customerDto = Mono.just(new CustomerDto("ABC101", "Customer1",  10));
		when(customerService.getCustomerById(any())).thenReturn(customerDto);

		Flux<CustomerDto> responseBody = webTestClient.get()
				.uri("/customer/ABC101")
				.exchange()
				.expectStatus()
				.isOk()
				.returnResult(CustomerDto.class)
				.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNextMatches(customer -> customer.getName().equals("Customer1")) // we have 3 objects here
				.verifyComplete();
	}


	@Test
	public void saveCustomerTest() {
		Mono<CustomerDto> customerDto = Mono.just(new CustomerDto("ABC101", "Customer1",  10));
		when(customerService.saveCustomer(customerDto)).thenReturn(customerDto);

		webTestClient.post()
				.uri("/customer")
				.body(Mono.just(customerDto), CustomerDto.class)
				.exchange()
				.expectStatus()
				.isOk();
	}

	@Test
	public void updateCustomerTest() {
		Mono<CustomerDto> customerDto = Mono.just(new CustomerDto("ABC101", "Customer1",  10));
		when(customerService.updateCustomer(customerDto, "ABC101")).thenReturn(customerDto);

		webTestClient.put()
				.uri("/customer/ABC101")
				.body(Mono.just(customerDto), CustomerDto.class)
				.exchange()
				.expectStatus()
				.isOk();
	}

	@Test
	public void deleteCustomerTest() {
		Mono<CustomerDto> customerDto = Mono.just(new CustomerDto("ABC101", "Customer1",  10));
		BDDMockito.given(customerService.deleteCustomer(any())).willReturn(Mono.empty());

		webTestClient.delete()
				.uri("/customer/ABC101")
				.exchange()
				.expectStatus()
				.isOk();
	}



}
