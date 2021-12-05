import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowSize
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import kotlin.math.pow

val numericSystems: IntArray = intArrayOf(2, 4, 8)

@Composable
fun app() {
    var fromNumber: Long by remember { mutableStateOf(0) }
    var base: Long by remember { mutableStateOf(2) }
    var baseDropdownExpanded: Boolean by remember { mutableStateOf(false) }
    var result: Long by remember { mutableStateOf(0) }

    val convert = {
        var localResult: Long = 0

        var index = 0
        for (i in fromNumber.toString().reversed()) {
            // FIXME
            localResult += (i.code.toLong() - 48L) * (base.toDouble().pow(index.toDouble())).toLong()
            index++
        }

        result = localResult
    }

    DesktopMaterialTheme {
        Box {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TextField(
                    value = fromNumber.toString(),
                    onValueChange = { num ->
                        fromNumber = try {
                            num.toLong()
                        } catch (e: NumberFormatException) {
                            fromNumber
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    label = { Text(text = "From") },
                    placeholder = { Text(text = "Enter a number") },
                )

                Row(modifier = Modifier.clickable { baseDropdownExpanded = !baseDropdownExpanded }) {
                    Text(text = "Base: $base", textAlign = TextAlign.Center, modifier = Modifier.width(100.dp).padding(8.dp))
                    DropdownMenu(
                        expanded = baseDropdownExpanded,
                        onDismissRequest = { baseDropdownExpanded = false },
                        modifier = Modifier.width(75.dp),
                    ) {
                        for (system in numericSystems) {
                            Text(
                                text = system.toString(),
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 10.dp).clickable {
                                    base = system.toLong()
                                    baseDropdownExpanded = false
                                }.width(75.dp),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }

                Button(onClick = convert) {
                    Text(text = "Convert", modifier = Modifier.padding(8.dp))
                }

                if (result != 0L) Text(text = "Result: $result", modifier = Modifier.padding(8.dp)) else Box{}
            }
        }
    }
}

fun main() = application {
    Window(
        title = "Numeric System Converter",
        state = WindowState(
            size = WindowSize(width = 400.dp, height = 400.dp)
        ),
        resizable = false,
        onCloseRequest = { this.exitApplication() }
    ) {
        app()
    }
}
