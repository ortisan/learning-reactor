import org.junit.jupiter.api.Test
import reactor.test.StepVerifier

internal class OperatorsTest {

    @Test
    fun testCollectList() {

        val collectList = Operators.collectList()

        StepVerifier
            .create(collectList)
            .expectNext(mutableListOf("Ana", "Andre", "Augusto", "Marcelo", "Marcia", "Zeus"))
            .expectComplete()
            .verify()

    }

    @Test
    fun testConcatWith() {

        val concatWith = Operators.concatWith()

        StepVerifier
            .create(concatWith)
            .expectNext("Hello")
            .expectNext("World")
            .expectComplete()
            .verify()

    }

    @Test
    fun testBufferFluxString() {

        val bufferFluxString = Operators.bufferFluxString()

        StepVerifier
            .create(bufferFluxString)
            .expectNextMatches({ it.get(0) == "Hello" && it.get(1) == "World" })
            .expectComplete()
            .verify()

    }

    @Test
    fun testFlatMap() {

        val flatMap = Operators.flatMap()

        StepVerifier
            .create(flatMap)
            .expectNext("Hello, World")
            .expectComplete()
            .verify()
    }

    @Test
    fun testZipInline() {
        val zipInline = Operators.zipWithInline()

        StepVerifier
            .create(zipInline)
            .expectNext("Hello World")
            .expectComplete()
            .verify()
    }

    @Test
    fun testZipWithLambda() {
        val zipLambda = Operators.zipwithLambda()

        StepVerifier
            .create(zipLambda)
            .expectNext("Hello World")
            .expectComplete()
            .verify()

    }

    @Test
    fun groupByAndFlatMap() {
        val groupByAndFlatMap = Operators.groupByAndFlatMap()

        val namesIndex = NamesIndex("A", listOf("Ana", "Andre", "Augusto"))

        StepVerifier
            .create(groupByAndFlatMap)
            .expectNext(namesIndex)
            .expectNextCount(2)
            .expectComplete()
            .verify()
    }

    @Test
    fun testGroupByReduce() {
        val groupByReduce = Operators.groupByReduce()

        StepVerifier
            .create(groupByReduce)
            .expectNext("Initial: A Names: Ana Andre Augusto")
            .expectNext("Initial: Z Names: Zeus")
            .expectNext("Initial: M Names: Marcelo Marcia")
            .expectComplete()
            .verify()
    }

    @Test
    fun testOnErrorResume() {
        val onErrorResume = Operators.onErrorResume()

        StepVerifier
            .create(onErrorResume)
            .expectNext("Error occured. We must continue...")
            .expectComplete()
            .verify()
    }

    @Test
    fun testOnErrorResumeRetry() {
        val onErrorResumeRetry = Operators.onErrorResumeRetry()

        StepVerifier
            .create(onErrorResumeRetry)
            .expectNext("Tried 2x, but error persists...")
            .expectComplete()
            .verify()
    }

    @Test
    fun testOnError() {
        val onError = Operators.onErrorError()

        StepVerifier
            .create(onError)
            .expectError()
            .verify()
    }

    @Test
    fun testFirstWithSignal() {
        val firstWithSignal = Operators.firstWithSignal()

        StepVerifier
            .create(firstWithSignal)
            .expectNext("World")
            .expectComplete()
            .verify()
    }

    @Test
    fun testWithContext() {
        val withContext = Operators.withContext()
        StepVerifier
            .create(withContext)
            .expectNextMatches { message: String -> message.contains("Trace Id") }
            .expectComplete()
            .verify()
    }

}