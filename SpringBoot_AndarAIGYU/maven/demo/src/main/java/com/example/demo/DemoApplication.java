package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class DemoApplication {

    @Bean
    RouterFunction<ServerResponse> routes(CustomerRepository cr) {
        return RouterFunctions.route(GET("/customers"), new HandlerFunction<ServerResponse>() {
            @Override
            public Mono<ServerResponse> handle(ServerRequest serverRequest) {
                return ok().body(cr.findAll(), Customer.class);
            }
        })
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}

@Component
class DataWriter implements ApplicationRunner {

    private final CustomerRepository customerRepository;

    public DataWriter(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Flux.just("Deepika", "Richa", "Sweta", "Priyanka")
                .flatMap(name -> customerRepository.save(new Customer(null, name)))
                .subscribe(System.out::println);

    }//runMethodEnds

}//DataWriterClassEnds

interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
}

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
class Customer {

    private String id, name;

}//CustomerClassEnds