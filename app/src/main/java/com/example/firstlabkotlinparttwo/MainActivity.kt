package com.example.firstlabkotlinparttwo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState


import com.example.firstlabkotlinparttwo.ui.theme.FirstLabKotlinPartTwoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirstLabKotlinPartTwoTheme {
                FuelCalculatorApp()
            }
        }
    }
}

@Composable
fun FuelCalculatorApp() {
    var H_value by remember { mutableStateOf("") }
    var C_value by remember { mutableStateOf("") }
    var S_value by remember { mutableStateOf("") }
    var V_value by remember { mutableStateOf("") }
    var O_value by remember { mutableStateOf("") }
    var A_value by remember { mutableStateOf("") }
    var W_value by remember { mutableStateOf("") }
    var lowerHeatingValue by remember { mutableStateOf("") }

    var results by remember { mutableStateOf<Map<String, Any>?>(null) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text("Калькулятор")

        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = C_value,
            onValueChange = { C_value = it },
            label = { Text("Вуглець (C%)") }
        )

        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = H_value,
            onValueChange = { H_value = it },
            label = { Text("Водень (H%)") }
        )

        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = O_value,
            onValueChange = { O_value = it },
            label = { Text("Кисень (O%)") }
        )

        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = S_value,
            onValueChange = { S_value = it },
            label = { Text("Сірка (S%)") }
        )

        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = A_value,
            onValueChange = { A_value = it },
            label = { Text("Зола (A%)") }
        )

        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = V_value,
            onValueChange = { V_value = it },
            label = { Text("Вміст ванадію (V мг/кг)") }
        )

        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = W_value,
            onValueChange = { W_value = it },
            label = { Text("Вологість робочої маси палива (W%)") }
        )

        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = lowerHeatingValue,
            onValueChange = { lowerHeatingValue = it },
            label = { Text("Нижча теплота згоряння горючої маси мазуту (МДж/кг)") }
        )

        Button(
            onClick = {
                val c = C_value.toDoubleOrNull() ?: 0.0
                val h = H_value.toDoubleOrNull() ?: 0.0
                val o = O_value.toDoubleOrNull() ?: 0.0
                val s = S_value.toDoubleOrNull() ?: 0.0
                val a = A_value.toDoubleOrNull() ?: 0.0
                val v = V_value.toDoubleOrNull() ?: 0.0
                val w = W_value.toDoubleOrNull() ?: 0.0
                val lh_value = lowerHeatingValue.toDoubleOrNull() ?: 0.0

                results = calculateFuelPropertiesForMazut(c, h, o, s, a, v, w, lh_value)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Розрахувати")
        }
        results?.let {
            Spacer(modifier = Modifier.height(16.dp))
            CalculationResults(it)
        }
    }
}


fun calculateFuelPropertiesForMazut(
    c: Double, h: Double, o: Double, s: Double, a: Double, v: Double, w: Double, lh_value: Double
): Map<String, Any> {

    val c_work = c * (100 - w - a) / 100
    val h_work = h * (100 - w - a) / 100
    val o_work = o * (100 - w - a) / 100
    val s_work = s * (100 - w - a) / 100
    val a_work = a * (100 - w) / 100
    val v_work = v * (100 - w) / 100

    val lh_value_work = lh_value * (100 - w - a) / 100 - 0.025 * w

    return mapOf(
        "Вуглець" to c_work,
        "Водень" to h_work,
        "Кисень" to o_work,
        "Сірка" to s_work,
        "Зола" to a_work,
        "Ванадій" to v_work,
        "Нижча теплота згоряння" to lh_value_work
    )
}

@Composable
fun CalculationResults(results: Map<String, Any>) {
    Column(modifier = Modifier.padding(16.dp)) {

        Text("Склад робочої маси мазуту")
        (results as Map<String, Double>).forEach { (component, value) ->
            Text("$component: ${String.format("%.2f", value)}%")
        }
    }
}

