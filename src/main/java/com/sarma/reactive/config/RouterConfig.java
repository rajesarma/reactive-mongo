package com.sarma.reactive.config;

import com.sarma.reactive.dto.CustomerDto;
import com.sarma.reactive.entity.Customer;
import com.sarma.reactive.handler.CustomerHandlerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {

    private CustomerHandlerService customerHandlerService;

    public RouterConfig(CustomerHandlerService customerHandlerService) {
        this.customerHandlerService = customerHandlerService;
    }

    @Bean
    @RouterOperations(
            {
                    @RouterOperation(path = "/router/customer",
                            produces = MediaType.APPLICATION_JSON_VALUE,
                            method = RequestMethod.GET,
                            beanClass = CustomerHandlerService.class,
                            beanMethod = "getCustomers",
                            operation = @Operation(
                                    operationId = "getCustomers",
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200",
                                                    description = "successful operation",
                                                    content = @Content(schema = @Schema(
                                                            implementation = CustomerDto.class
                                                    ))
                                            )
                                    }
                            )),
                    @RouterOperation(path = "/router/customer/reactive/{custId}",
                            produces = MediaType.APPLICATION_JSON_VALUE,
                            method = RequestMethod.GET,
                            beanClass = CustomerHandlerService.class,
                            beanMethod = "getFilterReactiveCustomers",
                            operation = @Operation(
                                    operationId = "getFilterReactiveCustomers",
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200",
                                                    description = "successful operation",
                                                    content = @Content(schema = @Schema(
                                                            implementation = CustomerDto.class
                                                    ))
                                            ),
                                            @ApiResponse(
                                                    responseCode = "404",
                                                    description = "Customer not found with given Customer ID"
                                            )
                                    },
                                    parameters = {
                                            @Parameter(in = ParameterIn.PATH, name = "custId")
                                    }
                            )),
                    @RouterOperation(path = "/router/customer/reactive",
                            produces = MediaType.APPLICATION_JSON_VALUE,
                            method = RequestMethod.POST,
                            beanClass = CustomerHandlerService.class,
                            beanMethod = "saveCustomer",
                            operation = @Operation(
                                    operationId = "saveCustomer",
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200",
                                                    description = "successful operation",
                                                    content = @Content(schema = @Schema(
                                                            implementation = String.class
                                                    ))
                                            ),
                                            @ApiResponse(
                                                    responseCode = "404",
                                                    description = "Bad Request"
                                            )
                                    },
                                    requestBody =
                                    @RequestBody(content = @Content(schema = @Schema(
                                            implementation = CustomerDto.class
                                    )))
                            ))

            }
    )
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
                .GET("/router/customer", customerHandlerService::getCustomers)
                .GET("/router/customer/reactive", customerHandlerService::getReactiveCustomers) // Passing query parameters here
                .GET("/router/customer/delayed/reactive", customerHandlerService::getCustomersDelayed) // Passing query parameters here
                .GET("/router/customer/reactive/{custId}", customerHandlerService::getFilterReactiveCustomers)
                .POST("/router/customer/reactive", customerHandlerService::saveCustomer)
                .build();
    }
}
