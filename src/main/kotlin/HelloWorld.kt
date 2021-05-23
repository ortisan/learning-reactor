import reactor.core.publisher.Mono

class HelloWorld {
    companion object {
        fun greet(): Mono<String> {
            return Mono.just("Hello World")
        }
    }
}

