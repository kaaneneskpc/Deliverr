package com.kaaneneskpc.deliverr

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kaaneneskpc.deliverr.common.DeliverrNavHost
import com.kaaneneskpc.deliverr.data.FoodApi
import com.kaaneneskpc.deliverr.ui.feature.home.DeliveriesScreen
import com.kaaneneskpc.deliverr.ui.feature.orders.details.OrderDetailsScreen
import com.kaaneneskpc.deliverr.ui.feature.orders.list.OrdersListScreen
import com.kaaneneskpc.deliverr.ui.features.auth.AuthScreen
import com.kaaneneskpc.deliverr.ui.features.auth.login.SignInScreen
import com.kaaneneskpc.deliverr.ui.features.auth.signup.SignUpScreen
import com.kaaneneskpc.deliverr.ui.features.notifications.NotificationsList
import com.kaaneneskpc.deliverr.ui.features.notifications.NotificationsViewModel
import com.kaaneneskpc.deliverr.ui.navigation.AddMenu
import com.kaaneneskpc.deliverr.ui.navigation.AuthScreen
import com.kaaneneskpc.deliverr.ui.navigation.Home
import com.kaaneneskpc.deliverr.ui.navigation.ImagePicker
import com.kaaneneskpc.deliverr.ui.navigation.Login
import com.kaaneneskpc.deliverr.ui.navigation.MenuList
import com.kaaneneskpc.deliverr.ui.navigation.NavRoute
import com.kaaneneskpc.deliverr.ui.navigation.Notification
import com.kaaneneskpc.deliverr.ui.navigation.OrderDetails
import com.kaaneneskpc.deliverr.ui.navigation.OrderList
import com.kaaneneskpc.deliverr.ui.navigation.SignUp
import com.kaaneneskpc.deliverr.ui.theme.DeliverrTheme
import com.kaaneneskpc.deliverr.ui.theme.Mustard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseDeliverrActivity() {
    var showSplashScreen = true

    @Inject
    lateinit var foodApi: FoodApi

    @Inject
    lateinit var deliverrSession: DeliverrSession

    sealed class BottomNavItem(val route: NavRoute, val icon: Int) {
        object Home : BottomNavItem(com.kaaneneskpc.deliverr.ui.navigation.Home, R.drawable.home)
        object Notification :
            BottomNavItem(
                com.kaaneneskpc.deliverr.ui.navigation.Notification,
                R.drawable.notification
            )

        object Orders : BottomNavItem(
            OrderList,
            R.drawable.orders
        )
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                showSplashScreen
            }
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.5f,
                    0f
                )
                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.5f,
                    0f
                )
                zoomX.duration = 500
                zoomY.duration = 500
                zoomX.interpolator = OvershootInterpolator()
                zoomY.interpolator = OvershootInterpolator()
                zoomX.doOnEnd {
                    screen.remove()
                }
                zoomY.doOnEnd {
                    screen.remove()
                }
                zoomY.start()
                zoomX.start()
            }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeliverrTheme {

                val shouldShowBottomNav = remember {
                    mutableStateOf(false)
                }
                val navItems = listOf(
                    BottomNavItem.Home,
                    BottomNavItem.Notification,
                    BottomNavItem.Orders
                )
                val navController = rememberNavController()
                val notificationsViewModel: NotificationsViewModel = hiltViewModel()
                val unreadCount = notificationsViewModel.unreadCount.collectAsStateWithLifecycle()

                LaunchedEffect(key1 = true) {
                    viewModel.event.collectLatest {
                        when (it) {
                            is DeliverrViewModel.HomeEvent.NavigateToOrderDetail -> {
                                navController.navigate(OrderDetails(it.orderID))
                            }
                        }
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val currentRoute =
                            navController.currentBackStackEntryAsState().value?.destination
                        AnimatedVisibility(visible = shouldShowBottomNav.value) {
                            NavigationBar(
                                containerColor = Color.White
                            ) {
                                navItems.forEach { item ->
                                    val selected =
                                        currentRoute?.hierarchy?.any { it.route == item.route::class.qualifiedName } == true

                                    NavigationBarItem(
                                        selected = selected,
                                        onClick = {
                                            navController.navigate(item.route)
                                        },
                                        icon = {
                                            Box(modifier = Modifier.size(48.dp)) {
                                                Icon(
                                                    painter = painterResource(id = item.icon),
                                                    contentDescription = null,
                                                    tint = if (selected) MaterialTheme.colorScheme.primary else Color.Gray,
                                                    modifier = Modifier.align(Center)
                                                )

                                                if (item.route == Notification && unreadCount.value > 0) {
                                                    ItemCount(unreadCount.value)
                                                }
                                            }
                                        })
                                }
                            }
                        }
                    }) { innerPadding ->

                    SharedTransitionLayout {
                        DeliverrNavHost (
                            navController = navController,
                            startDestination = if (deliverrSession.getToken() != null) Home else AuthScreen,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable<SignUp> {
                                shouldShowBottomNav.value = false
                                SignUpScreen(navController)
                            }
                            composable<AuthScreen> {
                                shouldShowBottomNav.value = false
                                AuthScreen(navController, false)
                            }
                            composable<Login> {
                                shouldShowBottomNav.value = false
                                SignInScreen(navController, false)
                            }
                            composable<Home> {
                                shouldShowBottomNav.value = true
                                DeliveriesScreen(navController)
                            }
                            composable<Notification> {
                                SideEffect {
                                    shouldShowBottomNav.value = true
                                }
                                NotificationsList(navController, notificationsViewModel)
                            }
                            composable<OrderList> {
                                shouldShowBottomNav.value = true
                                OrdersListScreen(navController)
                            }
                            composable<OrderDetails> {
                                shouldShowBottomNav.value = false
                                val orderID = it.toRoute<OrderDetails>().orderId
                                OrderDetailsScreen(orderID, navController)
                            }
                        }
                    }

                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            delay(3000)
            showSplashScreen = false
        }
    }
}

@Composable
fun BoxScope.ItemCount(count: Int) {
    Box(
        modifier = Modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(Mustard)
            .align(Alignment.TopEnd)
    ) {
        Text(
            text = "$count",
            modifier = Modifier
                .align(Center),
            color = Color.White,
            style = TextStyle(fontSize = 10.sp)
        )
    }
}