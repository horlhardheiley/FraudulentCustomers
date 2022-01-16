package com.ola.customer;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;

    CustomerService(CustomerRepository customerRepository,RestTemplate restTemplate){
        this.customerRepository = customerRepository;
        this.restTemplate = restTemplate;
    }
    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder().
                firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
        customerRepository.saveAndFlush(customer);



        // check if email is valid
        //  check if email is taken
        // check if fraudster
        // send notification
        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject("http:FRAUD/api/v1/fraud-check/{customerId}",
                FraudCheckResponse.class,customer.getId());

        if(fraudCheckResponse.isFraudster()){
            throw  new IllegalStateException("fraudster");
        }
    }
}
