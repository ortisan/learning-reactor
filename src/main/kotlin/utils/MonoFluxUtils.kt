package utils

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class MonoFluxUtils {

    companion object {
        fun getHelloMono(): Mono<String> {
            return Mono.just("Hello")
        }

        fun getWorldMono(): Mono<String> {
            return Mono.just("World")
        }

        fun getNamesFlux(): Flux<String> {
            return Flux.just("Ana", "Andre", "Augusto", "Marcelo", "Marcia", "Zeus")
        }
    }

}