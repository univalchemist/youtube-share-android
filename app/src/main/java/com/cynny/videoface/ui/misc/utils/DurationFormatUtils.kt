package com.cynny.videoface.ui.misc.utils

import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*


object DurationFormatUtils {
    fun fromMillis(millis: Long): String {
        val cal = Calendar.getInstance()
        cal.timeInMillis = millis - TimeZone.getDefault().rawOffset
        val h = cal.get(Calendar.HOUR)
        val m = cal.get(Calendar.MINUTE)
        val s = cal.get(Calendar.SECOND)
        val sb = StringBuilder("")
        var hasPrev = false
        if (h != 0) {
            sb.append(h)
            sb.append(":")
            hasPrev = true
        }
        if (m != 0 || hasPrev) {
            if (hasPrev) {
                sb.append(String.format("%02d", m))
            } else {
                sb.append(m)
            }
            sb.append(":")
            hasPrev = true
        }
        if (s != 0 || hasPrev) {
            if (hasPrev) {
                sb.append(String.format("%02d", s))
            } else {
                sb.append("0:"+ String.format("%02d", s))
            }
        } else {
            sb.append("0:00")
        }
        return sb.toString()
    }
}