package io.github.paulushcgcj;

import reactor.core.publisher.Mono;

import java.time.Duration;

public class WebClientStub {



  public Mono<Contents> doGet(String content){
    return
        Mono
            .just(new Contents().withData(content))
            .delayElement(Duration.ofSeconds(3));
  }

}
