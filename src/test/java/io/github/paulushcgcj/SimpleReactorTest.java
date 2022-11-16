package io.github.paulushcgcj;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This class contains simple examples of Mono/Flux usages
 */
class SimpleReactorTest {


  /**
   * Create a mono from value
   */
  @Test
  void shouldCreateSimpleMono(){

    StepVerifier
        .create(Mono.just("Value") )
        .expectNext("Value")
        .verifyComplete();

  }

  /**
   * Create an empty mono
   */
  @Test
  void shouldCreateSimpleMonoEmpty(){

    StepVerifier
        .create(Mono.empty() )
        .verifyComplete();

  }


  /**
   * Mono can be created from Optional with value
   */
  @Test
  void shouldCreateMonoFromOptional(){

    StepVerifier
        .create(Mono.justOrEmpty(Optional.of("Value")) )
        .expectNext("Value")
        .verifyComplete();

  }

  /**
   * Mono can be created from empty Optional
   */
  @Test
  void shouldCreateEmptyMonoFromOptional(){

    StepVerifier
        .create(Mono.justOrEmpty(Optional.empty()) )
        .verifyComplete();

  }

  /**
   * Reactor behaves much like the stream API. You can filter values
   */
  @Test
  void shouldFilterAndGetEmpty(){

    StepVerifier
        .create(
            Mono
                .justOrEmpty(Optional.of("Value"))
                .filter(value -> value.equals("Potato"))
        )
        .verifyComplete();

  }

  /**
   *
   */
  @Test
  void shouldBehaveLikeIfElse(){

    StepVerifier
        .create(

            Mono
                .justOrEmpty(Optional.of("Value"))
                //try
                .filter(value -> value.equals("Value")) //reached filter and the result is false

                //All those operations will be skipped
                .map(String::toUpperCase) //VALUE
                .map(String::toUpperCase) //VALUE
                .map(String::toUpperCase) //VALUE
                .map(String::toUpperCase) //VALUE
                .map(String::toUpperCase) //VALUE
                .map(String::toUpperCase) //VALUE

                .switchIfEmpty(Mono.just("Carrot")) //catch

                //finally
                .map(String::toLowerCase) //carrot / value


        )
        .expectNext("carrot")
        .verifyComplete();

  }

  /**
   * Reactor behaves much like the stream API. You can filter values, even using other monos as filter
   */
  @Test
  void shouldFilterWhenAndGetEmpty(){

    StepVerifier
        .create(
            Mono
                .justOrEmpty(Optional.of("Value"))
                .filterWhen(value -> Mono.just(value.equals("Potato")))
        )
        .verifyComplete();

  }

  /**
   * Reactor also can manipulate the value on the stream of events with a map
   */
  @Test
  void shouldModifyValue(){

    StepVerifier
        .create(
            Mono
                .justOrEmpty(Optional.of("Value"))
                .map(String::toLowerCase)
        )
        .expectNext("value")
        .verifyComplete();

  }

  /**
   * You can consume other mono inside a mono by using flatMap
   */
  @Test
  void shouldExecuteFlatMapAndGetOtherResult(){

    StepVerifier
        .create(
            Mono
                .justOrEmpty(Optional.of("Value"))
                .flatMap(olderValue -> Mono.just("Potato"))
        )
        .expectNext("Potato")
        .verifyComplete();

  }

  /**
   * You can delay the element on a stream of events
   */
  @Test
  void shouldDelaySimpleMono(){

    StepVerifier
        .create(
            Mono
                .just("Value")
                .delayElement(Duration.ofSeconds(3))
        )
        .expectNext("Value")
        .verifyComplete();

  }

  /**
   * You can delay the subscription, AKA the time when the consumer
   * start to receive the data that is already on a stream of events
   */
  @Test
  void shouldDelaySubSimpleMono(){

    StepVerifier
        .create(
            Mono
                .just("Value")
                .delaySubscription(Duration.ofSeconds(3))
        )
        .expectNext("Value")
        .verifyComplete();

  }

  /**
   * Flux is a stream of data. Do not confuse it with a list of values.
   * Even though they look like a list of values, there is a conceptual difference.
   *
   * Flux is a 0..n stream of values. It means that it can be empty or contains
   * an infinite number of values that can be emitted at any given time
   */
  @Test
  void shouldCreateSimpleFlux(){

    StepVerifier
        .create(Flux.just("Value1","Value2") )
        .expectNext("Value1")
        .expectNext("Value2")
        .verifyComplete();

  }

  /**
   * As Mono, Flux can get it's content from other basic java types, such as lists
   */
  @Test
  void shouldCreateSimpleFluxFromList(){

    StepVerifier
        .create(Flux.fromIterable(List.of("Value1","Value2")) )
        .expectNext("Value1")
        .expectNext("Value2")
        .verifyComplete();

  }

  /**
   * As Mono, Flux can get it's content from other basic java types, such as streams
   */
  @Test
  void shouldCreateSimpleFluxFromStream(){

    StepVerifier
        .create(Flux.fromStream(Stream.of("Value1","Value2")) )
        .expectNext("Value1")
        .expectNext("Value2")
        .verifyComplete();

  }






}
