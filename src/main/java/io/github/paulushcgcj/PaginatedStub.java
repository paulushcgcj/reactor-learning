package io.github.paulushcgcj;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Slf4j
public class PaginatedStub {


  public Mono<List<Contents>> getPage(int page, int size) {

    log.info("Requesting {} {}",page,size);

    if (page > 5)
      return Mono.empty();

    return
        Flux
            .range(0, size)
            .map(content -> Contents.builder().data("Page " + page + " size " + size + " content " + content).build())
            .collectList()
            .delayElement(Duration.ofSeconds(1));

  }
}
