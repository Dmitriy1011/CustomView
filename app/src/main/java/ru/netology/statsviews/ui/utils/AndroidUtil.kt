package ru.netology.statsviews.ui.utils

import android.content.Context
import kotlin.math.ceil

//перевод dp в px
object AndroidUtil {
   fun dp(context: Context, dp: Int) = ceil(context.resources.displayMetrics.density * dp).toInt()
}