package com.sarma.reactive.handler;

import com.sarma.reactive.dto.CustomerDto;
import com.sarma.reactive.repository.CustomerRepositoryBkp;
import io.netty.util.internal.StringUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerHandlerService {
    private CustomerRepositoryBkp customerRepositoryBkp;

    public CustomerHandlerService(CustomerRepositoryBkp customerRepositoryBkp) {
        this.customerRepositoryBkp = customerRepositoryBkp;
    }

    public Mono<ServerResponse> getCustomers(ServerRequest serverRequest) {
        Flux<CustomerDto> allReactiveCustomersList = customerRepositoryBkp.getAllReactiveCustomersList();
        return ServerResponse.ok().body(allReactiveCustomersList, CustomerDto.class);
    }

    public Mono<ServerResponse> getCustomersDelayed(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(customerRepositoryBkp.getAllReactiveCustomers(), CustomerDto.class);
    }

    public Mono<ServerResponse> getReactiveCustomers(ServerRequest serverRequest) {


        // If query Param is present
        if (serverRequest.queryParam("custId").isPresent())  {
            Integer custId = Integer.parseInt(serverRequest.queryParam("custId").get());

            /*Mono<Customer> customer = customerRepository.getAllReactiveCustomersList()
                    .filter(e -> e.getId().equals(custId))
                    .take(1)
                    .single();*/

            Mono<CustomerDto> customer = customerRepositoryBkp.getAllReactiveCustomersList()
                    .filter(e -> e.getId().equals(custId))
                    .next();

            return ServerResponse.ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .body(customer, CustomerDto.class);
        }

        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(
//                        serverRequest.queryParam("custId").isPresent() ?
                        customerRepositoryBkp.getAllReactiveCustomersList()
//                        : customerRepository.getAllReactiveCustomersList().filter(e -> e.getId().equals(Integer.parseInt(serverRequest.queryParam("custId").get())))
                        , CustomerDto.class);
    }

    /**
     * In this method we can use the servlet request to get headers, pathVariable, requestParameter
     * @param serverRequest Servlet Request
     * @return Server Response
     */
    public Mono<ServerResponse> getFilterReactiveCustomers(ServerRequest serverRequest) {

        // Headers
        /*if (serverRequest.headers().header("custId")) {
            custId = Integer.parseInt(serverRequest.pathVariable("custId"));
        }*/

//        Flux<Customer> allReactiveCustomersList = customerRepository.getAllReactiveCustomersList();

        // Path Variable
        if (!StringUtil.isNullOrEmpty(serverRequest.pathVariable("custId"))) {
            Integer custId = Integer.parseInt(serverRequest.pathVariable("custId"));
            Flux<CustomerDto> allReactiveCustomersList = customerRepositoryBkp.getAllReactiveCustomersList()
                    .filter(e -> e.getId().equals(custId));

            return ServerResponse.ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .body(allReactiveCustomersList, CustomerDto.class);
        }

        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(customerRepositoryBkp.getAllReactiveCustomersList(), CustomerDto.class);
    }

    public Mono<ServerResponse> saveCustomer(ServerRequest request) {
        Mono<CustomerDto> customerMono = request.bodyToMono(CustomerDto.class);
        Mono<String> monoString = customerMono.map(c -> c.getId() + " :: " + c.getName());
        return  ServerResponse.ok().body(monoString, String.class);
    }
}
