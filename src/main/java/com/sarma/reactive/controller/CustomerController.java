package com.sarma.reactive.controller;

import com.sarma.reactive.dto.CustomerDto;
import com.sarma.reactive.service.CustomerService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("")
    public Flux<CustomerDto> getAllCustomers() {
        return customerService.getAllCustomers();
    }


    @GetMapping("/{custId}")
    public Mono<CustomerDto> getAllCustomers(@PathVariable String custId) {
        return customerService.getCustomerById(custId);
    }

    @GetMapping("/age-range")
    public Flux<CustomerDto> getAllCustomers(
            @RequestParam Integer startAge,
            @RequestParam Integer endAge
    ) {
        return customerService.getCustomersInRange(startAge,endAge);
    }

    @PostMapping()
    public Mono<CustomerDto> saveCustomer(@RequestBody Mono<CustomerDto> customerDtoMono) {
        return customerService.saveCustomer(customerDtoMono);
    }

    @PutMapping("/{id}")
    public Mono<CustomerDto> saveCustomer(@RequestBody Mono<CustomerDto> customerDtoMono, @PathVariable String id) {
        return customerService.updateCustomer(customerDtoMono, id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCustomer(@PathVariable String id) {
        return customerService.deleteCustomer(id);
    }


    @GetMapping(value = "/reactive", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CustomerDto> getAllReactiveCustomers() {
        return customerService.getAllReactiveCustomers();
    }

}
