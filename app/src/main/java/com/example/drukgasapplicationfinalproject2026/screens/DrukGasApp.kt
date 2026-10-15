package com.example.drukgasapplicationfinalproject2026.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.drukgasapplicationfinalproject2026.data.FuelPriceViewModel
import com.example.drukgasapplicationfinalproject2026.data.Outlet
import com.example.drukgasapplicationfinalproject2026.data.ReportViewModel
import com.example.drukgasapplicationfinalproject2026.data.WeatherViewModel
import com.example.drukgasapplicationfinalproject2026.ui.theme.ThemeBlack
import com.example.drukgasapplicationfinalproject2026.ui.theme.ThemeBlue
import com.example.drukgasapplicationfinalproject2026.ui.theme.ThemePowderBlue
import com.example.drukgasapplicationfinalproject2026.ui.theme.ThemeSageYellow
import com.example.drukgasapplicationfinalproject2026.ui.theme.ThemeSkyBlue

@Composable
fun DrukGasApp() {

    var showWelcomeScreen by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableStateOf(0) }
    var selectedOutlet by remember { mutableStateOf<Outlet?>(null) }
    var showSettings by remember { mutableStateOf(false) }

    var defaultLocation by remember { mutableStateOf("Thimphu") }
    var defaultResource by remember { mutableStateOf("LPG") }

    val reportViewModel: ReportViewModel = viewModel()
    val weatherViewModel: WeatherViewModel = viewModel()

    val weather by weatherViewModel.weather.collectAsState()
    val weatherLoading by weatherViewModel.isLoading.collectAsState()
    val weatherError by weatherViewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        weatherViewModel.loadWeather()
    }

    val submittedReports = reportViewModel.reports.collectAsState().value
    val latestReports = reportViewModel.latestReports.collectAsState().value

    val fuelPriceViewModel: FuelPriceViewModel = viewModel()
    val fuelPrices by fuelPriceViewModel.fuelPrices.collectAsState()
    val fuelPricesLoading by fuelPriceViewModel.loading.collectAsState()
    val fuelPricesError by fuelPriceViewModel.error.collectAsState()

    if (showWelcomeScreen) {
        WelcomeScreen(
            onGetStarted = {
                showWelcomeScreen = false
            }
        )
    } else {
        Scaffold(
            bottomBar = {
                if (!showSettings) {
                NavigationBar(
                    containerColor = Color.White,
                    contentColor = ThemeBlack
                ) {

                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = {
                            selectedTab = 0
                            selectedOutlet = null
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home"
                            )
                        },
                        label = {
                            Text(
                                text = "Home",
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ThemeBlue,
                            selectedTextColor = ThemeBlue,
                            indicatorColor = Color.Transparent,
                            unselectedIconColor = ThemeBlack,
                            unselectedTextColor = ThemeBlack
                        )
                    )

                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = {
                            selectedTab = 1
                            selectedOutlet = null
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.RateReview,
                                contentDescription = "Report"
                            )
                        },
                        label = {
                            Text(
                                text = "Report",
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ThemeBlue,
                            selectedTextColor = ThemeBlue,
                            indicatorColor = Color.Transparent,
                            unselectedIconColor = ThemeBlack,
                            unselectedTextColor = ThemeBlack
                        )
                    )

                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = {
                            selectedTab = 2
                            selectedOutlet = null
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Ask AI"
                            )
                        },
                        label = {
                            Text(
                                text = "Ask AI",
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ThemeBlue,
                            selectedTextColor = ThemeBlue,
                            indicatorColor = Color.Transparent,
                            unselectedIconColor = ThemeBlack,
                            unselectedTextColor = ThemeBlack
                        )
                    )

                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = {
                            selectedTab = 3
                            selectedOutlet = null
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile"
                            )
                        },
                        label = {
                            Text(
                                text = "Profile",
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ThemeBlue,
                            selectedTextColor = ThemeBlue,
                            indicatorColor = Color.Transparent,
                            unselectedIconColor = ThemeBlack,
                            unselectedTextColor = ThemeBlack
                        )
                    )
                }
            }
        }
    ) { innerPadding ->

        if (showSettings) {
            SettingsScreen(
                modifier = Modifier.padding(innerPadding),
                defaultLocation = defaultLocation,
                defaultResource = defaultResource,
                onUpdateDefaultLocation = { location -> defaultLocation = location },
                onUpdateDefaultResource = { resource -> defaultResource = resource },
                onBack = { showSettings = false }
            )
        } else {
            when (selectedTab) {

                0 -> {
                    if (selectedOutlet == null) {
                        DrukGasHomeScreen(
                            modifier = Modifier.padding(innerPadding),
                            reports = submittedReports,
                            latestReports = latestReports,
                            onOutletClick = { outlet ->
                                selectedOutlet = outlet
                            },
                            onViewReports = {
                                selectedTab = 1
                            },
                            weather = weather,
                            weatherLoading = weatherLoading,
                            weatherError = weatherError,
                            fuelPrices = fuelPrices,
                            fuelPricesLoading = fuelPricesLoading,
                            fuelPricesError = fuelPricesError,
                            defaultLocation = defaultLocation,
                            defaultResource = defaultResource,
                            onOpenSettings = { showSettings = true }
                        )
                    } else {
                        OutletDetailsScreen(
                            modifier = Modifier.padding(innerPadding),
                            outlet = selectedOutlet!!,
                            reports = submittedReports,
                            onBack = {
                                selectedOutlet = null
                            }
                        )
                    }
                }

                1 -> {
                    ReportsScreen(
                        modifier = Modifier.padding(innerPadding),
                        reports = submittedReports,
                        onReportSubmitted = { newReport ->
                            reportViewModel.addReport(newReport)
                        }
                    )
                }

                2 -> {
                    AskDrukGasScreen(
                        modifier = Modifier.padding(innerPadding),
                        reports = submittedReports
                    )
                }

                3 -> {
                    ProfileScreen(
                        modifier = Modifier.padding(innerPadding),
                        reports = submittedReports,
                        onDeleteReport = { reportId ->
                            reportViewModel.deleteReport(reportId)
                        },
                        onUpvoteReport = { reportId ->
                            reportViewModel.upvoteReport(reportId)
                        },
                        onAddHelpfulScore = { reportId ->
                            reportViewModel.addHelpfulScore(reportId)
                        }
                    )
                }
            }
        }
    }
}
}
