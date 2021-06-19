import reactor.core.publisher.Flux
import reactor.core.publisher.GroupedFlux
import reactor.core.publisher.Mono
import reactor.util.context.Context
import reactor.util.context.ContextView
import utils.FunctionUtils
import utils.MonoFluxUtils
import java.time.Duration
import java.util.*


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

        fun collectMultimap(): Mono<MutableMap<String, MutableCollection<String>>>? {
            return MonoFluxUtils.getNamesFlux().collectMultimap { name -> "${name[0]}" }
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

        fun firstWithSignal(): Flux<String>? {
            val a: Flux<String> = Flux.just("Hello").delaySubscription(Duration.ofMillis(450))
            val b: Flux<String> = Flux.just("World").delaySubscription(Duration.ofMillis(400))
            return Flux.firstWithSignal(a, b)
        }

        // https://github.com/reactor/reactor-core/blob/main/docs/asciidoc/advancedFeatures.adoc#context
        fun withContext(): Mono<String>? {
            val context = Context.of("trace_id", UUID.randomUUID().toString()).readOnly()
            return MonoFluxUtils.getHelloMono()
                .zipWith(MonoFluxUtils.getWorldMono(), FunctionUtils.concatLambdaFunction).flatMap {
                    Mono.deferContextual({ ctx: ContextView ->
                        val concatenationWithTraceInformation =
                            "Trace Id: ${ctx.get<String>("trace_id")}, Message: ${it}"
                        Mono.just(concatenationWithTraceInformation)
                    })
                }.contextWrite(context)
        }
    }
}
