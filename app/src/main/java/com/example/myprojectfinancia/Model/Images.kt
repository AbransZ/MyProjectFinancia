package com.example.myprojectfinancia.Model

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth


import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myprojectfinancia.R

@Preview
@Composable
fun Logoimage(){
    Box(Modifier.fillMaxWidth()){
        Image(
        painterResource(id = R.drawable.logo),
        contentDescription = "LogoApp",
        modifier = Modifier.padding(top = 30.dp).size(150.dp).clip(RoundedCornerShape(50.dp)).align(Alignment.Center),

        )}

}