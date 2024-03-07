package com.example.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.weatherapp.api.WeatherApi
import com.example.weatherapp.model.data.WeatherResponse
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

    companion object{
       private const val TAG = "MainActivity"
    }

    private val client by lazy {
        OkHttpClient.Builder()
            .build()
    }

    private val weatherApi : WeatherApi by lazy{
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
                var call = weatherApi.get( "-3.10719", "-60.026", getString(R.string.appid))

                call.enqueue(object : Callback<WeatherResponse>{
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
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (button, text) = createRefs()
        Text(
            text = "Hello $name!",
            modifier = Modifier.constrainAs(text){
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
            }
        )
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
        Greeting("Android")
    }
}