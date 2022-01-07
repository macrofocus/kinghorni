// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
fun App(simulator: Simulator) {
    var count by remember { mutableStateOf(0L) }

    MaterialTheme {
        Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(5.dp)) {
                Button(onClick = {
                    simulator.start()
                }) {
                    Text("Start")
                }
                Button(onClick = {
                    simulator.step()
                }) {
                    Text("Step")
                }
                Button(onClick = {
                    simulator.stop()
                }) {
                    Text("Stop")
                }
            }
            Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(5.dp)) {
                Text("Step " + count)
            }
        }
    }
}

fun main() = application {
    configureJepLibraryPath()
    configureJep()

    val interpreter = SharedInterpreter().apply {
        runScript("src/main/python/Simulator.py")
    }

    val simulator = Simulator(interpreter)

    Window(onCloseRequest = {
        interpreter.close()
        exitApplication()
    }
    ) {
        App(simulator)
    }
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
