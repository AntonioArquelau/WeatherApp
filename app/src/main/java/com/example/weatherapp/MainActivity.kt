package com.example.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.model.data.WeatherResponse
import com.example.weatherapp.ui.theme.MenuTitleText
import com.example.weatherapp.ui.theme.MenuValueText
import com.example.weatherapp.ui.theme.SubTitleBold
import com.example.weatherapp.ui.theme.TempStyle
import com.example.weatherapp.ui.theme.TitleBold
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.utils.DataStatus
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.google.gson.Gson

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
                Greeting(viewModel)
            }
        }
        viewModel.getTemp().observe(this){
            when(it){
                is DataStatus.Success ->{

                }
                is DataStatus.Error ->{}
                else ->{}
            }
        }
        Log.d("###", "### appid: " +getString(R.string.appid) )
        viewModel.getWeatherBasedOnLocation( "-3.10719", "-60.0261",getString(R.string.appid))
    }
}

@Composable
fun Greeting(viewModel: WeatherViewModel) {

    val mainSummary: String by viewModel.mainSummary.observeAsState("")
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.lightBlue))
    ) {
        val (row, main, city, temp, tempIcon) = createRefs()
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
            text = "Manaus",
            style = SubTitleBold,
            modifier = Modifier.constrainAs(city) {
                top.linkTo(main.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        Text(
            text = "30º",
            style = TempStyle,
            modifier = Modifier
                .padding(0.dp, 40.dp, 0.dp, 0.dp)
                .constrainAs(temp) {
                    top.linkTo(city.bottom)
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
                    top.linkTo(temp.bottom)
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
            CreateBottomMenuElements("Max", R.drawable.max_temp, "30º")
            CreateBottomMenuElements("Min", R.drawable.min_temp, "29º")
            CreateBottomMenuElements("Wind", R.drawable.wind, "2m/s")
            CreateBottomMenuElements("Humidity", R.drawable.humidity, "6%")
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