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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.weatherapp.api.WeatherApi
import com.example.weatherapp.model.data.WeatherResponse
import com.example.weatherapp.ui.theme.MenuTitleText
import com.example.weatherapp.ui.theme.MenuValueText
import com.example.weatherapp.ui.theme.SubTitleBold
import com.example.weatherapp.ui.theme.TempStyle
import com.example.weatherapp.ui.theme.TitleBold
import com.example.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val client by lazy {
        OkHttpClient.Builder()
            .build()
    }

    private val weatherApi: WeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                Greeting(name = "teste")
            }
        }



        MainScope().launch {
            withContext(Dispatchers.IO) {
                var call = weatherApi.get("-3.10719", "-60.026", getString(R.string.appid))

                call.enqueue(object : Callback<WeatherResponse> {
                    override fun onResponse(
                        call: Call<WeatherResponse>,
                        response: Response<WeatherResponse>
                    ) {
                        Log.d(TAG, "Success: ${response.body()}")
                    }

                    override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                        t.message?.let { Log.e(TAG, "Failure: $it") }
                    }

                })
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.lightBlue))
    ) {
        val (row, main, city, temp, tempIcon) = createRefs()
        Text(
            text = "Sunny",
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
                .padding(20.dp, 0.dp,20.dp, 0.dp)
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
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(80.dp).height(140.dp)) {
        Text(modifier = Modifier.padding(0.dp, 20.dp, 0.dp, 10.dp), style = MenuTitleText, text = title)
        Icon(modifier = Modifier.width(30.dp).height(30.dp), tint = colorResource(id = R.color.lightBlack), painter = painterResource(id = icon), contentDescription = null)
        Text(modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 20.dp), style = MenuValueText, text = value)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
        Greeting("Android")
    }
}