import org.junit.jupiter.api.Test
import reactor.test.StepVerifier


internal class HelloWorldTest {

    @Test
    fun testGreet() {

        val monoGreet = HelloWorld.greet()

        StepVerifier
            .create<Any>(monoGreet)
            .expectNext("Hello World")
            .expectComplete()
            .verify()
    }

}