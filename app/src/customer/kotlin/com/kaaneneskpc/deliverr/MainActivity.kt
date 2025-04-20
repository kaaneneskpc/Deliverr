package com.kaaneneskpc.deliverr

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kaaneneskpc.deliverr.data.FoodApi
import com.kaaneneskpc.deliverr.data.models.response.restaurant.FoodItem
import com.kaaneneskpc.deliverr.notification.DeliverrMessagingService
import com.kaaneneskpc.deliverr.ui.features.add_address.AddAddressScreen
import com.kaaneneskpc.deliverr.ui.features.address_list.AddressListScreen
import com.kaaneneskpc.deliverr.ui.features.order_success.OrderSuccess
import com.kaaneneskpc.deliverr.ui.features.auth.AuthScreen
import com.kaaneneskpc.deliverr.ui.features.auth.login.SignInScreen
import com.kaaneneskpc.deliverr.ui.features.auth.signup.SignUpScreen
import com.kaaneneskpc.deliverr.ui.features.cart.CartScreen
import com.kaaneneskpc.deliverr.ui.features.cart.CartViewModel
import com.kaaneneskpc.deliverr.ui.features.food_item.FoodDetailsScreen
import com.kaaneneskpc.deliverr.ui.features.home.HomeScreen
import com.kaaneneskpc.deliverr.ui.features.notifications.NotificationsList
import com.kaaneneskpc.deliverr.ui.features.notifications.NotificationsViewModel
import com.kaaneneskpc.deliverr.ui.features.orders.OrderListScreen
import com.kaaneneskpc.deliverr.ui.features.order_details.OrderDetailsScreen
import com.kaaneneskpc.deliverr.ui.features.restaurant.RestaurantDetailsScreen
import com.kaaneneskpc.deliverr.ui.navigation.AddAddress
import com.kaaneneskpc.deliverr.ui.navigation.AddressList
import com.kaaneneskpc.deliverr.ui.navigation.AuthScreen
import com.kaaneneskpc.deliverr.ui.navigation.Cart
import com.kaaneneskpc.deliverr.ui.navigation.FoodDetails
import com.kaaneneskpc.deliverr.ui.navigation.Home
import com.kaaneneskpc.deliverr.ui.navigation.Login
import com.kaaneneskpc.deliverr.ui.navigation.NavRoute
import com.kaaneneskpc.deliverr.ui.navigation.Notification
import com.kaaneneskpc.deliverr.ui.navigation.OrderDetails
import com.kaaneneskpc.deliverr.ui.navigation.OrderList
import com.kaaneneskpc.deliverr.ui.navigation.OrderSuccess
import com.kaaneneskpc.deliverr.ui.navigation.RestaurantDetails
import com.kaaneneskpc.deliverr.ui.navigation.SignUp
import com.kaaneneskpc.deliverr.ui.navigation.foodItemNavType
import com.kaaneneskpc.deliverr.ui.theme.DeliverrTheme
import com.kaaneneskpc.deliverr.ui.theme.Mustard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@AndroidEntryPoint
class MainActivity : BaseDeliverrActivity() {
    var showSplashScreen = true

    @Inject
    lateinit var foodApi: FoodApi

    @Inject
    lateinit var deliverrSession: DeliverrSession

    sealed class BottomNavItem(val route: NavRoute, val icon: Int) {
        object Home : BottomNavItem(com.kaaneneskpc.deliverr.ui.navigation.Home, R.drawable.home)
        object Cart : BottomNavItem(com.kaaneneskpc.deliverr.ui.navigation.Cart, R.drawable.cart)
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
                    BottomNavItem.Cart,
                    BottomNavItem.Notification,
                    BottomNavItem.Orders
                )
                val navController = rememberNavController()
                val cartViewModel: CartViewModel = hiltViewModel()
                val cartItemSize = cartViewModel.cartItemCount.collectAsStateWithLifecycle()
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

                                                if (item.route == Cart && cartItemSize.value > 0) {
                                                    ItemCount(cartItemSize.value)
                                                }

                                                if(item.route == Notification && unreadCount.value > 0) {
                                                    ItemCount(unreadCount.value)
                                                }
                                            }
                                        })
                                }
                            }
                        }
                    }) { innerPadding ->

                    SharedTransitionLayout {
                        NavHost(
                            navController = navController,
                            startDestination = if (deliverrSession.getToken() != null) Home else AuthScreen,
                            modifier = Modifier.padding(innerPadding),
                            enterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                    animationSpec = tween(300)
                                ) + fadeIn(animationSpec = tween(300))
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                    animationSpec = tween(300)
                                ) + fadeOut(animationSpec = tween(300))
                            },
                            popEnterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                    animationSpec = tween(300)
                                ) + fadeIn(animationSpec = tween(300))
                            },
                            popExitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                    animationSpec = tween(300)
                                ) + fadeOut(animationSpec = tween(300))
                            }
                        ) {
                            composable<SignUp> {
                                shouldShowBottomNav.value = false
                                SignUpScreen(navController)
                            }
                            composable<AuthScreen> {
                                shouldShowBottomNav.value = false
                                AuthScreen(navController, true)
                            }
                            composable<Login> {
                                shouldShowBottomNav.value = false
                                SignInScreen(navController, true)
                            }
                            composable<Home> {
                                shouldShowBottomNav.value = true
                                HomeScreen(navController, this)
                            }
                            composable<RestaurantDetails> {
                                shouldShowBottomNav.value = false
                                val route = it.toRoute<RestaurantDetails>()
                                RestaurantDetailsScreen(
                                    navController,
                                    name = route.restaurantName,
                                    imageUrl = route.restaurantImageUrl,
                                    restaurantID = route.restaurantId,
                                    this
                                )
                            }
                            composable<FoodDetails>(
                                typeMap = mapOf(typeOf<FoodItem>() to foodItemNavType)
                            ) {
                                shouldShowBottomNav.value = false
                                val route = it.toRoute<FoodDetails>()
                                FoodDetailsScreen(
                                    navController,
                                    foodItem = route.foodItem,
                                    this,
                                    onItemAddedToCart = { cartViewModel.getCart() }
                                )
                            }
                            composable<Cart> {
                                shouldShowBottomNav.value = true
                                CartScreen(navController, cartViewModel)
                            }
                            composable<Notification> {
                                SideEffect {
                                    shouldShowBottomNav.value = true
                                }
                                NotificationsList(navController, notificationsViewModel)
                            }
                            composable<AddressList> {
                                shouldShowBottomNav.value = false
                                AddressListScreen(navController)
                            }
                            composable<AddAddress> {
                                shouldShowBottomNav.value = false
                                AddAddressScreen(navController)
                            }
                            composable<OrderSuccess> {
                                shouldShowBottomNav.value = false
                                val orderID = it.toRoute<OrderSuccess>().orderId
                                OrderSuccess(orderID, navController)
                            }
                            composable<OrderList> {
                                shouldShowBottomNav.value = true
                                OrderListScreen(navController)
                            }

                            composable<OrderDetails> {
                                shouldShowBottomNav.value = false
                                val orderID = it.toRoute<OrderDetails>().orderId
                                OrderDetailsScreen(navController, orderID)
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