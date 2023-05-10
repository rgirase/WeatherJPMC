package com.rng.weatherappjpmc.utils

import java.text.DecimalFormat

object Utils {

    /**
     * Converts a temperature value from Kelvin to Celsius.
     *
     * @param temperature The temperature value in Kelvin.
     * @return The converted temperature value in Celsius, rounded to two decimal places.
     */
    fun convertTemperatureToCelsius(temperature: Double): Double {
        val result = temperature - 273.15
        val decimalFormat = DecimalFormat("#.##")
        return decimalFormat.format(result).toDouble()
    }

    /**
     * Converts a temperature value from Kelvin to Fahrenheit.
     *
     * @param temperature The temperature value in Kelvin.
     * @return The converted temperature value in Fahrenheit, rounded to two decimal places.
     */
    fun convertToDecimal(value: Int): Double {
        return value.toDouble() / 1000
    }
}
