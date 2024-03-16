package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.weatherapp.ui.theme.MenuTitleText
import com.example.weatherapp.ui.theme.MenuValueText
import com.example.weatherapp.ui.theme.SubTitleBold
import com.example.weatherapp.ui.theme.TempStyle
import com.example.weatherapp.ui.theme.TitleBold
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.utils.DataStatus
import com.example.weatherapp.utils.LoadingUtils
import com.example.weatherapp.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val viewModel: WeatherViewModel by lazy {
        WeatherViewModel()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                ShowUI(viewModel)
            }
        }
        viewModel.getWeatherBasedOnLocation( "-3.10719", "-60.0261",getString(R.string.appid))
    }
}

@Composable
fun ShowUI(viewModel: WeatherViewModel){
    val isActive : Boolean by viewModel.loading.observeAsState(true)
    if(isActive){
        LoadingUtils.LoadingUI(isActive)
    }
    else {
        CreateUI(viewModel)
    }
}

@Composable
fun CreateUI(viewModel: WeatherViewModel) {

    val mainSummary: String by viewModel.mainSummary.observeAsState("")
    val city: String by viewModel.city.observeAsState("")
    val temp: String by viewModel.temp.observeAsState("")
    val maxTemp: String by viewModel.maxTemp.observeAsState("")
    val minTemp: String by viewModel.minTemp.observeAsState("")
    val humidity: String by viewModel.humidity.observeAsState("")
    val wind: String by viewModel.wind.observeAsState("")
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.lightBlue))
    ) {
        val (row, main, cityRef, tempRef, tempIcon) = createRefs()
        Text(
            text = mainSummary,
            style = TitleBold,
            modifier = Modifier
                .padding(0.dp, 80.dp, 0.dp, 0.dp)
                .constrainAs(main) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Text(
            text = city,
            style = SubTitleBold,
            modifier = Modifier.constrainAs(cityRef) {
                top.linkTo(main.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        Text(
            text = "$temp º",
            style = TempStyle,
            modifier = Modifier
                .padding(0.dp, 40.dp, 0.dp, 0.dp)
                .constrainAs(tempRef) {
                    top.linkTo(cityRef.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Image(
            painter = painterResource(id = R.drawable.cloud),
            contentDescription = null,
            modifier = Modifier
                .padding(0.dp, 40.dp, 0.dp, 0.dp)
                .height(200.dp)
                .width(200.dp)
                .constrainAs(tempIcon) {
                    top.linkTo(tempRef.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.lightGray))
                .padding(20.dp, 0.dp, 20.dp, 0.dp)
                .constrainAs(row) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            CreateBottomMenuElements("Max", R.drawable.max_temp, "$maxTemp º")
            CreateBottomMenuElements("Min", R.drawable.min_temp, "$minTemp º")
            CreateBottomMenuElements("Wind", R.drawable.wind, "$wind m/s")
            CreateBottomMenuElements("Humidity", R.drawable.humidity, "$humidity %")
        }
    }
}

@Composable
fun CreateBottomMenuElements(title: String, icon: Int, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(80.dp)
            .height(140.dp)
    ) {
        Text(
            modifier = Modifier.padding(0.dp, 20.dp, 0.dp, 10.dp),
            style = MenuTitleText,
            text = title
        )
        Icon(
            modifier = Modifier
                .width(30.dp)
                .height(30.dp),
            tint = colorResource(id = R.color.lightBlack),
            painter = painterResource(id = icon),
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 20.dp),
            style = MenuValueText,
            text = value
        )
    }
}

@Composable
fun Indicator(
    size: Dp = 32.dp, // indicator size
    sweepAngle: Float = 90f, // angle (lenght) of indicator arc
    color: Color = Color.Blue, // color of indicator arc line
    strokeWidth: Dp = 50.dp //width of cicle and ar lines
) {
    ////// animation //////

    // docs recomend use transition animation for infinite loops
    // https://developer.android.com/jetpack/compose/animation
    val transition = rememberInfiniteTransition(label = "teste1")

    // define the changing value from 0 to 360.
    // This is the angle of the beginning of indicator arc
    // this value will change over time from 0 to 360 and repeat indefinitely.
    // it changes starting position of the indicator arc and the animation is obtained
    val currentArcStartAngle by transition.animateValue(
        0,
        360,
        Int.VectorConverter,
        infiniteRepeatable(
            animation = tween(
                durationMillis = 1100,
                easing = LinearEasing
            )
        ), label = "teste"
    )

    ////// draw /////

    // define stroke with given width and arc ends type considering device DPI
    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Square)
    }

    // draw on canvas
    Canvas(
        Modifier
            .progressSemantics() // (optional) for Accessibility services
            .size(size) // canvas size
            .padding(strokeWidth / 2) //padding. otherwise, not the whole circle will fit in the canvas
    ) {
        // draw "background" (gray) circle with defined stroke.
        // without explicit center and radius it fit canvas bounds
        drawCircle(Color.LightGray, style = stroke)

        // draw arc with the same stroke
        drawArc(
            color,
            // arc start angle
            // -90 shifts the start position towards the y-axis
            startAngle = currentArcStartAngle.toFloat() - 90,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = stroke
        )
    }
}