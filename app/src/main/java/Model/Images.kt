package Model

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth


import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myprojectfinancia.R


@Composable
fun Logoimage(){
    Box(Modifier.fillMaxWidth()){
        Image(
        painterResource(id = R.drawable.logo),
        contentDescription = "LogoApp",
        modifier = Modifier.padding(top = 30.dp).size(150.dp).clip(CircleShape).align(Alignment.Center),

        )}

}