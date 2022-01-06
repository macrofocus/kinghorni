// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import jep.Interpreter
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
    jepTest()

    SharedInterpreter().use { interp ->
        interp.exec("from java.lang import System")
        interp.exec("s = 'Hello World'")
        interp.exec("System.out.println(s)")
        interp.exec("print(s)")
        interp.exec("print(s[1:-1])")
    }

    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

fun jepTest() {
    val p = Runtime.getRuntime().exec("python3 src/main/python/jep_path.py")
    val `in` = BufferedReader(InputStreamReader(p.inputStream))
    val ret = `in`.readLine()
    println("the jep's built C library is at: $ret")
    MainInterpreter.setJepLibraryPath(ret)
    val interp: Interpreter = SharedInterpreter()
    interp.exec("print('hello world')")
    interp.close()
}
