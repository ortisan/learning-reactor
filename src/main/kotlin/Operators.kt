import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import utils.FunctionUtils
import utils.MonoFluxUtils

class Operators {

    companion object {

        fun collectList(): Mono<MutableList<String>> {
            return MonoFluxUtils.getNamesFlux().collectList()
        }

        fun concatWith(): Flux<String> {
            return MonoFluxUtils.getHelloMono().concatWith(MonoFluxUtils.getWorldMono())
        }

        fun bufferFluxString(): Flux<List<String>> {
            return concatWith().buffer()
        }

        fun flatMap(): Flux<String> {
            return bufferFluxString().flatMap {
                val reducedString = FunctionUtils.reduceStringsLambda(it)
                Mono.just(reducedString)
            }
        }

        fun zipWithInline(): Mono<String> {
            return MonoFluxUtils.getHelloMono()
                .zipWith(
                    MonoFluxUtils.getWorldMono(),
                    { helloStr: String, worldStr: String -> "${helloStr} ${worldStr}" })
        }

        fun zipwithLambda(): Mono<String> {
            return MonoFluxUtils.getHelloMono()
                .zipWith(MonoFluxUtils.getWorldMono(), FunctionUtils.concatLambdaFunction)
        }

        fun groupByReduce(): Flux<String> {
            return MonoFluxUtils.getNamesFlux().groupBy { name -> "${name[0]}" }.flatMap {
                it.reduce("Initial: ${it.key()} Names:", { x, y -> "${x} ${y}" })
            }
        }

        fun groupByAndFlatMap(): Flux<NamesIndex>? {
            return MonoFluxUtils.getNamesFlux().groupBy { name -> "${name[0]}" }
                .flatMap { g -> g.collectList().map { NamesIndex(g.key(), it) } }
        }

        fun onErrorResume(): Mono<String> {
            return Mono.fromCallable({ -> if (true) throw RuntimeException("Error") else "Test" })
                .onErrorResume { exc: Throwable ->
                    Mono.just("Error occured. We must continue...")
                }
        }

        fun onErrorResumeRetry(): Mono<String> {
            return Mono.fromCallable({ -> if (true) throw RuntimeException("Error") else "Test" }).retry(2).log()
                .onErrorResume { Mono.just("Tried 2x, but error persists...") }
        }

        fun onErrorError(): Mono<String> {
            return Mono.fromCallable({ -> if (true) throw RuntimeException("Error") else "Test" })
        }
    }
}
