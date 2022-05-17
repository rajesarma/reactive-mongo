package com.sarma.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ReactiveTest {

    @Test
    public void monoTest() {
        Mono<String> stringMono = Mono.just("Spring");
        stringMono.subscribe(System.out::print);
    }

    @Test
    public void fluxTest() {
        Flux<String> stringFlux = Flux.just("Spring", "Boot", "React")
                .concatWithValues("Angular", "Vue");
        stringFlux.subscribe(System.out::println);
    }
}