package com.example.weatherapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R

// Set of Material typography styles to start with
val Typography = Typography(
        bodyLarge = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp
        )

        /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val TitleBold = TextStyle(
        fontFamily = FontFamily.Serif,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 38.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
)

val SubTitleBold = TextStyle(
        fontFamily = FontFamily.SansSerif,
        color = Color.White,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
)

val MenuTitleText = TextStyle(
        fontFamily = FontFamily.SansSerif,
        color = LightBlack,
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal,
)

val MenuValueText = TextStyle(
        fontFamily = FontFamily.SansSerif,
        color = LightBlack,
        fontSize = 20.sp,
        fontWeight = FontWeight.Normal,
)

val TempStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        color = Color.White,
        fontWeight = FontWeight.Normal,
        fontSize = 100.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
)