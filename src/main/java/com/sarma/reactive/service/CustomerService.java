package com.sarma.reactive.service;

import com.sarma.reactive.dto.CustomerDto;
import com.sarma.reactive.repository.CustomerRepository;
import com.sarma.reactive.util.CustomerUtil;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {
//    private final CustomerRepositoryBkp customerRepositoryBkp;
    private final CustomerRepository customerRepository;
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Flux<CustomerDto> getAllCustomers() {
        long startTime = System.currentTimeMillis();
        Flux<CustomerDto> customerDtos = customerRepository.findAll().map(CustomerUtil::convert);
        long endTime = System.currentTimeMillis();
        System.out.println("Execution Time :: " + ((endTime - startTime) / 1000) + " seconds");
        return customerDtos;
    }

    public Mono<CustomerDto> getCustomerById(String id) {
        return customerRepository.findById(id).map(CustomerUtil::convert);
    }

    public Flux<CustomerDto> getCustomersInRange(Integer startAge, Integer endAge) {
        return customerRepository.findByAgeBetween(Range.closed(startAge, endAge)).map(CustomerUtil::convert);
    }

    /*public Mono<CustomerDto> save(CustomerDto customerDto) {
        CustomerEntity customerEntity = CustomerUtil.convert(customerDto);
        Mono<CustomerEntity> savedEntityMono = customerRepository.insert(customerEntity);
        return savedEntityMono.map(CustomerUtil::convert);
    }*/

    public Mono<CustomerDto> saveCustomer(Mono<CustomerDto> customerDto) {
        return customerDto.map(CustomerUtil::convert)
                .flatMap(customerRepository::insert)
                .map(CustomerUtil::convert);
    }

    public Mono<CustomerDto> updateCustomer(Mono<CustomerDto> customerDto, String id) {
        return customerRepository.findById(id)
                .flatMap(p ->
                        customerDto.map(CustomerUtil::convert)
                                .doOnNext(e -> e.setId(id)))
                .flatMap(customerRepository::save)
                .map(CustomerUtil::convert);
    }

    public Mono<Void> deleteCustomer(String id) {
        return customerRepository.deleteById(id);
    }


    public Flux<CustomerDto> getAllReactiveCustomers() {
        long startTime = System.currentTimeMillis();
//        Flux<CustomerDto> customers = customerRepositoryBkp.getAllReactiveCustomers();
        Flux<CustomerDto> customers = customerRepository.findAll().map(CustomerUtil::convert);
        long endTime = System.currentTimeMillis();
        System.out.println("Execution Time :: " + ((endTime - startTime) / 1000) + " seconds");
        return customers;
    }
}
