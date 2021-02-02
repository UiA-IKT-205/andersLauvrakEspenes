package no.uia.ikt205.pomodoro.util

import android.util.Log

fun millisecondsToDescriptiveTime(ms:Long):String {
    val seconds = (ms / 1000)  % 60
    val minutes = (ms / (1000 * 60) % 60)
    val hours = (ms / (1000 * 60 * 60) % 24)

    return String.format("%02d:%02d:%02d", hours, minutes, seconds);
}

fun descriptiveTimeToMilliseconds(str:String):Long {
    val parts = str.split(":")
    val seconds = parts[0].toLong() * 60 * 60 + parts[1].toLong() * 60 + parts[2].toLong() // Convert to seconds
    return seconds * 1000 // Convert to milliseconds
}

fun minuteToMilliseconds(min:Int):Long {
    return (min * 60 * 1000).toLong()
}