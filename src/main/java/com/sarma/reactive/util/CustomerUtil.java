package com.sarma.reactive.util;

import com.sarma.reactive.dto.CustomerDto;
import com.sarma.reactive.entity.Customer;
import org.springframework.beans.BeanUtils;

public class CustomerUtil {
    public static CustomerDto convert(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer, customerDto);
        return customerDto;
    }

    public static Customer convert(CustomerDto customerDto) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDto, customer);
        return customer;
    }
}
