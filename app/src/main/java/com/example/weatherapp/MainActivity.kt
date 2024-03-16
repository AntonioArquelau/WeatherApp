package com.example.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.weatherapp.model.data.WeatherResponse
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

    val appId by lazy {
        getString(R.string.appid)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                ShowUI(viewModel,"-3.10719", "-60.0261",appId)
            }
        }
        loadWeatherInfo()
    }
    private fun loadWeatherInfo(){
        viewModel.getWeatherBasedOnLocation( "30.10719", "80.0261",appId)
    }
}


@Composable
fun ShowUI(viewModel: WeatherViewModel, lat: String, lon: String, appId: String){
    val dataStatus : DataStatus<WeatherResponse> by viewModel.infoStatus.observeAsState(DataStatus.Loading())
    when(dataStatus){
        is DataStatus.Success ->{
            LoadingUtils.LoadingUI(false)
            CreateUI(viewModel)
        }
        is DataStatus.Error -> {
            LoadingUtils.LoadingUI(false)
            ShowErrorScreen {
                viewModel.getWeatherBasedOnLocation(lat, lon, appId)
            }
        }
        else -> {
            LoadingUtils.LoadingUI(true)
        }
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

    val colorId = if(temp.replace(",", ".").toFloat() < 20){
        R.color.lightBlue
    } else {
        R.color.lightOrange
    }

    val iconId =when(mainSummary){
        "cloud", "clouds" ->{
            R.drawable.cloud
        }
        "rain" ->{
            R.drawable.rain
        }
        else -> {
            R.drawable.sun
        }
    }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = colorId))
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
            painter = painterResource(id = iconId),
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
fun ShowErrorScreen (onClickReload: () -> Unit){
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
            .align(Alignment.Center)
        ) {
            Image(
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp),
                painter = painterResource(id = R.drawable.error),
                contentDescription = null
            )
            Text(

                modifier = Modifier
                    .padding(0.dp, 10.dp, 0.dp, 20.dp),
                style = MenuValueText,
                text = "Sorry Error to processs your request"
            )
        }

        Button(
            onClick = { onClickReload.invoke() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 0.dp, 20.dp, 20.dp)
                .align(Alignment.BottomCenter)
            ) {
            Text("Reload")
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