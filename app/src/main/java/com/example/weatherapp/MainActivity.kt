package com.example.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import java.text.DecimalFormat
import java.text.NumberFormat

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

    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    val formatter: NumberFormat = DecimalFormat("#0.00")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                ShowUI(viewModel){onClickReload()}
            }
        }
        getLocation()
    }

    private fun onClickReload(){
         if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
             ActivityCompat.requestPermissions(
                 this,
                 arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                 locationPermissionCode
             )
            return
        }
        val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        viewModel.getWeatherBasedOnLocation(
            location?.latitude.toString(),
            location?.longitude.toString(),
            appId
        )
    }

    private fun getLocation() {
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
        else{
            val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            viewModel.getWeatherBasedOnLocation(
                location?.latitude.toString(),
                location?.longitude.toString(),
                appId
            )
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                viewModel.getWeatherBasedOnLocation(location?.latitude.toString(), location?.longitude.toString(), appId)
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


@Composable
fun ShowUI(viewModel: WeatherViewModel, onClickReload: () -> Unit){
    val dataStatus : DataStatus<WeatherResponse> by viewModel.infoStatus.observeAsState(DataStatus.Loading())
    when(dataStatus){
        is DataStatus.Success ->{
            LoadingUtils.LoadingUI(false)
            CreateUI(viewModel)
        }
        is DataStatus.Error -> {
            LoadingUtils.LoadingUI(false)
            ShowErrorScreen {
                onClickReload
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

    val colorId = when(mainSummary){
        "Cloud", "Clouds" ->{
            R.color.lightBlue
        }
        "Rain" ->{
            R.color.lightBlue
        }
        else -> {
            R.color.lightOrange
        }
    }

    val iconId = when(mainSummary){
        "Cloud", "Clouds" ->{
            R.drawable.cloud
        }
        "Rain" ->{
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
                .padding(
                    dimensionResource(id = R.dimen.default_dimen),
                    dimensionResource(id = R.dimen.main_summary_padding_top),
                    dimensionResource(id = R.dimen.default_dimen),
                    dimensionResource(id = R.dimen.default_dimen)
                )
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
                .padding(
                    dimensionResource(id = R.dimen.default_dimen),
                    dimensionResource(id = R.dimen.temp_padding_top),
                    dimensionResource(id = R.dimen.default_dimen),
                    dimensionResource(id = R.dimen.default_dimen),
                )
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
                .padding(
                    dimensionResource(id = R.dimen.default_dimen),
                    dimensionResource(id = R.dimen.main_image_padding_top),
                    dimensionResource(id = R.dimen.default_dimen),
                    dimensionResource(id = R.dimen.default_dimen)
                )
                .height(dimensionResource(id = R.dimen.main_image_size))
                .width(dimensionResource(id = R.dimen.main_image_size))
                .constrainAs(tempIcon) {
                    top.linkTo(tempRef.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.lightGray))
                .padding(
                    dimensionResource(id = R.dimen.bottom_row_padding_start),
                    dimensionResource(id = R.dimen.default_dimen),
                    dimensionResource(id = R.dimen.bottom_row_padding_end),
                    dimensionResource(id = R.dimen.default_dimen)
                )
                .constrainAs(row) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            CreateBottomMenuElements(stringResource(R.string.max), R.drawable.max_temp, "$maxTemp º")
            CreateBottomMenuElements(stringResource(R.string.min), R.drawable.min_temp, "$minTemp º")
            CreateBottomMenuElements(stringResource(R.string.wind), R.drawable.wind, "$wind m/s")
            CreateBottomMenuElements(stringResource(R.string.humidity), R.drawable.humidity, "$humidity %")
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
                    .width(dimensionResource(id = R.dimen.error_image_icon_size))
                    .height(dimensionResource(id = R.dimen.error_image_icon_size)),
                painter = painterResource(id = R.drawable.error),
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .padding(
                        dimensionResource(id = R.dimen.default_dimen),
                        dimensionResource(id = R.dimen.error_text_padding_top),
                        dimensionResource(id = R.dimen.default_dimen),
                        dimensionResource(id = R.dimen.error_text_padding_bottom)
                    ),
                style = MenuValueText,
                text = stringResource(R.string.error_message)
            )
        }

        Button(
            onClick = { onClickReload.invoke() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    dimensionResource(id = R.dimen.bottom_reload_padding_start),
                    dimensionResource(id = R.dimen.default_dimen),
                    dimensionResource(id = R.dimen.bottom_reload_padding_end),
                    dimensionResource(id = R.dimen.bottom_reload_padding_bottom)
                )
                .align(Alignment.BottomCenter)
            ) {
            Text(stringResource(R.string.reload))
        }
    }
}

@Composable
fun CreateBottomMenuElements(title: String, icon: Int, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(dimensionResource(id = R.dimen.column_bottom_info_width))
            .height(dimensionResource(id = R.dimen.column_bottom_info_height))
    ) {
        Text(
            modifier = Modifier.padding(
                dimensionResource(id = R.dimen.default_dimen),
                dimensionResource(id = R.dimen.label_text_bottom_item_padding_top),
                dimensionResource(id = R.dimen.default_dimen),
                dimensionResource(id = R.dimen.label_text_bottom_item_padding_bottom)
            ),
            style = MenuTitleText,
            text = title
        )
        Icon(
            modifier = Modifier
                .width(dimensionResource(id = R.dimen.label_icon_bottom_item_size))
                .height(dimensionResource(id = R.dimen.label_icon_bottom_item_size)),
            tint = colorResource(id = R.color.lightBlack),
            painter = painterResource(id = icon),
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(
                dimensionResource(id = R.dimen.default_dimen),
                dimensionResource(id = R.dimen.label_text_bottom_item_padding_top),
                dimensionResource(id = R.dimen.default_dimen),
                dimensionResource(id = R.dimen.label_text_bottom_item_padding_bottom)
            ),
            style = MenuValueText,
            text = value
        )
    }
}