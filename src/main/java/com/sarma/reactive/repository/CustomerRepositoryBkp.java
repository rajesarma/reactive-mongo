package com.sarma.reactive.repository;

import com.sarma.reactive.dto.CustomerDto;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
public class CustomerRepositoryBkp {
    public List<CustomerDto> getAllCustomers() {
        return IntStream.rangeClosed(1, 20)
                .peek(this::sleep)
                .peek(index -> System.out.println("Process Count :: " + index))
                .mapToObj(index -> new CustomerDto(index+"", "Customer" + index,  index+10))
                .collect(Collectors.toList());
    }

    private void sleep(int i) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Flux<CustomerDto> getAllReactiveCustomers() {
        return Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(index -> System.out.println("Process Count in Reactive flow :: " + index))
                .map(index -> new CustomerDto(index+"", "Customer" + index, index+10));
    }

    public Flux<CustomerDto> getAllReactiveCustomersList() {
        return Flux.range(1, 10)
                .doOnNext(index -> System.out.println("Process Count in Reactive flow :: " + index))
                .map(index -> new CustomerDto(index+"", "Customer" + index, index+10));
    }
}
