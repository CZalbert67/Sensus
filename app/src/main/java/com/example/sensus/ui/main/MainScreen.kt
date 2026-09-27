package com.example.sensus.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sensus.theme.SensusTheme

@Composable
fun MainScreen(
  modifier: Modifier = Modifier
) {
  MainAppContainer(modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
  SensusTheme { MainAppContainer() }
}

