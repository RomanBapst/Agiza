package com.example.agiza
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.agiza.components.home.HomeScreen
import com.example.agiza.components.login.Login
import com.example.agiza.components.login.LoginScreen
import com.example.agiza.ui.theme.AgizaTheme
import kotlinx.serialization.Serializable


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgizaTheme {
                    MainScreen()
            }
        }
    }
}

data class TopLevelRoute<T : Any>(val name: String, val route: T, val icon: ImageVector)

val toplevelRoutes = listOf(
    TopLevelRoute("Home", HomeScreen, Icons.Filled.Home)
)

@Composable
fun MainScreen() {
   val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()



    Scaffold(bottomBar =

    if (navBackStackEntry?.destination?.hierarchy?.any{ it.hasRoute(HomeScreen::class) } == true) {
        {
            NavigationBar {
                val currentDestination = navBackStackEntry?.destination
                toplevelRoutes.forEach { toplevelRoute ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                toplevelRoute.icon,
                                contentDescription = toplevelRoute.name
                            )
                        },
                        selected = currentDestination?.hierarchy?.any { it.hasRoute(toplevelRoute.route::class) } == true,
                        onClick = {}
                    )


                }
            }
        }
    } else {
        {
        }
    }) { innerPadding ->

        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController = navController, startDestination = Login) {
                composable<Login> {
                    LoginScreen(onNavigateToHome = {
                        navController.navigate(HomeScreen) {
                            popUpTo<Login>() {
                                inclusive = true
                            }
                        }
                    })
                }
                composable<HomeScreen> { HomeScreen() }
            }
        }
    }
}

