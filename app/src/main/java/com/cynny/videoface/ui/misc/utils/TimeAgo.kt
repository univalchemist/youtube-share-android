package com.cynny.videoface.ui.misc.utils

import android.content.Context
import java.util.*
import java.util.concurrent.TimeUnit
import com.cynny.videoface.R


object TimeAgo {

    val times: MutableMap<String, Long> = LinkedHashMap()
    val strings: MutableMap<String, Pair<Int, Int>> = LinkedHashMap()

    init {
        times["year"] = TimeUnit.DAYS.toMillis(365)
        times["month"] = TimeUnit.DAYS.toMillis(30)
        times["week"] = TimeUnit.DAYS.toMillis(7)
        times["day"] = TimeUnit.DAYS.toMillis(1)
        times["hour"] = TimeUnit.HOURS.toMillis(1)
        times["minute"] = TimeUnit.MINUTES.toMillis(1)
        times["second"] = TimeUnit.SECONDS.toMillis(1)

        strings["year"] = Pair(R.string.year, R.string.years)
        strings["month"] = Pair(R.string.month, R.string.months)
        strings["week"] = Pair(R.string.week, R.string.weeks)
        strings["day"] = Pair(R.string.day, R.string.days)
        strings["hour"] = Pair(R.string.hour, R.string.hours)
        strings["minute"] = Pair(R.string.minute, R.string.minutes)
        strings["second"] = Pair(R.string.second, R.string.seconds)
    }

    private fun toRelative(context: Context, duration: Long, maxLevel: Int = times.size): String {
        var d = duration
        val res = StringBuilder()
        var level = 0
        for ((key, value) in times) {
            val timeDelta = d / value
            if (timeDelta > 0) {
                res.append(timeDelta)
                        .append(" ")
                        .append(if (timeDelta > 1) context.getString(strings[key]?.second!!) else context.getString(strings[key]?.first!!))
                        .append(", ")
                d -= value * timeDelta
                level++
            }
            if (level == maxLevel) {
                break
            }
        }
        return if ("" == res.toString()) {
            context.getString(R.string.now)
        } else {
            res.setLength(res.length - 2)
            res.append(" " + context.getString(R.string.ago))
            res.toString()
        }
    }

    fun toRelative(context: Context, start: Date, end: Date): String {
        return toRelative(context, end.time - start.time)
    }

    fun toRelative(context: Context, start: Date, end: Date, level: Int): String {
        return toRelative(context, end.time - start.time, level)
    }
}