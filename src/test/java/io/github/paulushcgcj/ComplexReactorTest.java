package io.github.paulushcgcj;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComplexReactorTest {

  private final WebClientStub client = new WebClientStub();
  private final PaginatedStub paginated = new PaginatedStub();

  @Test
  void shouldConsumeService() {

    StepVerifier
        .create(
            Mono
                .just("data")
                .doOnNext(System.out::println)
                .flatMap(client::doGet)
                .doOnNext(System.out::println)
                .map(Contents::getData)
        )
        .expectNext("data")
        .verifyComplete();

  }

  @Test
  void shouldProcessListOfRequests() {

    Mono
        .just(List.of("Element 1", "Element 2", "Element 3")) //Most common scenario, you have a list in a mono
        .flatMapIterable(Function.identity()) //we convert it to flux to make it easier to process each one individually
        .flatMap(client::doGet) //execute the transaction for each entry individually
        .collectList() //get back as a list of mono
        .subscribe();

  }

  @Test
  void shouldPaginate() {

    AtomicInteger pageCounter = new AtomicInteger(0);
    List<Contents> contentList = new ArrayList<>();

    StepVerifier
        .create(
            Mono
                .just(contentList) //This example, I started as a List, as I want a list of contents as a result
                .expand(value -> paginated.getPage(pageCounter.getAndIncrement(), 10))
                .flatMapIterable(Function.identity())//I'm using an external counter, but you can use whatever you want
                //We are "expanding" our request, which means, we will execute this until we receive an empty response
                .collectList()
                //As the expand returns a flux, I make it into a list to extract it, but you can do anything you want here
        )
        .assertNext(contents -> assertEquals(60,contents.size()))
        //In my test, I want to check if I got only my 60 results. Keep in mind that the initial list content will also be added
        //that's why I started with an empty list
        .verifyComplete();


  }

}
