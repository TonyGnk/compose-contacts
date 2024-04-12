package com.example.composecontacts.ui.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.composecontacts.ui.home.HomeDestination
import com.example.composecontacts.ui.home.HomeScreen
import com.example.composecontacts.ui.item.ItemDetailsDestination
import com.example.composecontacts.ui.item.ItemDetailsScreen
import com.example.composecontacts.ui.item.ItemEditDestination
import com.example.composecontacts.ui.item.ItemEditScreen
import com.example.composecontacts.ui.item.ItemEntryDestination
import com.example.composecontacts.ui.item.ItemEntryScreen

@Composable
fun ContactsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val enterAnimation =
        slideInHorizontally(
//            animationSpec = tween(
//                250, easing = FastOutSlowInEasing
//            ),
            initialOffsetX = { 100 },
        ) + fadeIn(
            animationSpec = tween(
                250, easing = FastOutSlowInEasing
            )
        )

    val exitAnimation = slideOutHorizontally(
        targetOffsetX = { 100 },
//        animationSpec = tween(
//            250, easing = FastOutSlowInEasing
//        )
    ) + fadeOut(
        animationSpec = tween(
            250, easing = FastOutSlowInEasing
        )
    )


    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToItemEntry = { navController.navigate(ItemEntryDestination.route) },
                navigateToItemUpdate = {
                    navController.navigate("${ItemDetailsDestination.route}/${it}")
                }
            )
        }
        composable(
            route = ItemEntryDestination.route,
            enterTransition = { enterAnimation },
            exitTransition = { exitAnimation }
        ) {
            ItemEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = ItemDetailsDestination.routeWithArgs,
            enterTransition = { enterAnimation },
            exitTransition = { exitAnimation },
            arguments = listOf(navArgument(ItemDetailsDestination.itemIdArg) {
                type = NavType.IntType
            }),
        ) {
            ItemDetailsScreen(
                navigateToEditItem = { navController.navigate("${ItemEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = ItemEditDestination.routeWithArgs,
            enterTransition = {
                enterAnimation
            },
            exitTransition = { exitAnimation },
            arguments = listOf(navArgument(ItemEditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}

