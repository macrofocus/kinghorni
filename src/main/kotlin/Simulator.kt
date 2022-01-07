import jep.Interpreter
import org.jetbrains.skia.impl.getPtr

class Simulator(private val interpreter: Interpreter) {
    val count: Long
        get() {
            return interpreter.getValue("count") as Long
        }


    fun start() {

    }

    fun step() {
        interpreter.apply {
            exec("step()")
            println("Kotlin: Step ${count}")
        }
    }

    fun stop() {

    }

}