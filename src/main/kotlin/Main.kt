// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import jep.Interpreter
import jep.JepConfig
import jep.MainInterpreter
import jep.SharedInterpreter
import java.io.BufferedReader
import java.io.InputStreamReader


@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Button(onClick = {
            text = "Hello, Desktop!"
        }) {
            Text(text)
        }
    }
}

fun main() = application {
    configureJepLibraryPath()
    configureJep()

    val interpreter = SharedInterpreter().apply {
        runScript("src/main/python/Simulator.py")
        exec("step()")
        val count = getValue("count") as Long
        println("Kotlin: Step ${count}")
    }
    interpreter.close()

    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

fun jepTest() {
    val interp: Interpreter = SharedInterpreter()
    interp.exec("print('hello world')")
    interp.close()
}

private fun configureJep() {
    val config = JepConfig()
        .redirectStdout(System.out)
        .redirectStdErr(System.err)
    SharedInterpreter.setConfig(config)
}

private fun configureJepLibraryPath() {
    val ret = findJepLibraryPath()
    println("the jep's built C library is at: $ret")
    MainInterpreter.setJepLibraryPath(ret)
}

private fun findJepLibraryPath(): String? {
    val p = Runtime.getRuntime().exec("python3 src/main/python/jep_path.py")
    val `in` = BufferedReader(InputStreamReader(p.inputStream))
    val ret = `in`.readLine()
    return ret
}
