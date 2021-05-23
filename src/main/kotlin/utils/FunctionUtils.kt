package utils

class FunctionUtils {

    companion object {

        val concatLambdaFunction = { x: String, y: String ->
            val concatenation = "${x} ${y}"
            concatenation
        }

        val reduceStringsLambda = { x: Iterable<String> ->
            x.reduce { acc, s -> "${acc}, ${s}" }
        }
    }

}