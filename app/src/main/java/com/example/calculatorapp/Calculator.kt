package com.example.calculatorapp

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow

class Calculator {
    val calculateFlow = MutableStateFlow(CalculatorModel())

    fun enterNumber(number: String) {
        val currentCalculate: CalculatorModel = calculateFlow.value

        if (currentCalculate.operation.isEmpty()) {
            val newModel: CalculatorModel = currentCalculate.copy(
                numberFirst = currentCalculate.numberFirst + number
            )
            calculateFlow.tryEmit(newModel)
            return
        }
        val newModel: CalculatorModel = currentCalculate.copy(
            numberSecond = currentCalculate.numberSecond + number
        )
        calculateFlow.tryEmit(newModel)

    }

    fun enterOperation(operation: String) {
        val currentCalculate: CalculatorModel = calculateFlow.value
        if (currentCalculate.operation.isEmpty() &&
            currentCalculate.numberFirst.lastOrNull() != '.'
        ) {
            val newModel: CalculatorModel = currentCalculate.copy(
                operation = operation
            )
            calculateFlow.tryEmit(newModel)
        }
    }

    fun enterCalculate() {
        val currentCalculator: CalculatorModel = calculateFlow.value

        if (currentCalculator.numberSecond.lastOrNull() == '.') return
        val numberFirst: Double = currentCalculator.numberFirst.toDoubleOrNull() ?: return
        val numberSecond: Double = currentCalculator.numberSecond.toDoubleOrNull() ?: return

        val result: Double = when (currentCalculator.operation) {
            "+" -> numberFirst + numberSecond
            "-" -> numberFirst - numberSecond
            "x" -> numberFirst * numberSecond
            "/" -> numberFirst / numberSecond
            else -> 0.0
        }

        calculateFlow.tryEmit(
            currentCalculator.copy(
                numberFirst = result.toString(),
                numberSecond = "",
                operation = ""
            )
        )
    }

    fun enterDelete() {
        val currentCalculate: CalculatorModel = calculateFlow.value
        when {
            currentCalculate.numberSecond.isNotEmpty() -> {
                val numberSecond: String = currentCalculate.numberSecond.dropLast(1)
                calculateFlow.tryEmit(
                    currentCalculate.copy(
                        numberSecond = numberSecond
                    )
                )
            }

            currentCalculate.operation.isNotEmpty() -> {
                calculateFlow.tryEmit(
                    currentCalculate.copy(
                        operation = ""
                    )
                )
            }

            currentCalculate.numberFirst.isNotEmpty() -> {
                val numberFirst: String = currentCalculate.numberFirst.dropLast(1)
                calculateFlow.tryEmit(
                    currentCalculate.copy(
                        numberFirst = numberFirst
                    )
                )
            }
        }
    }

    fun enterDecimal() {
        val currentCalculator: CalculatorModel = calculateFlow.value

        if (currentCalculator.operation.isEmpty()
            && !currentCalculator.numberFirst.contains(".")
            && !currentCalculator.numberFirst.isNotEmpty()
        ) {
            calculateFlow.tryEmit(
                currentCalculator.copy(
                    numberFirst = currentCalculator.numberFirst + "."
                )
            )
            return

        }
        if (!currentCalculator.numberSecond.contains(".")
            && !currentCalculator.numberSecond.isNotEmpty()
        ) {
            calculateFlow.tryEmit(
                currentCalculator.copy(
                    numberFirst = currentCalculator.numberFirst + "."
                )
            )
        }

    }

    fun enterClear() {
        calculateFlow.tryEmit(CalculatorModel())
    }
}



