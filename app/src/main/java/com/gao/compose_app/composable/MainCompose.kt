package com.gao.compose_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.statusBarsHeight

/**
 * 主界面
 * [onFinish] 点击两次返回关闭页面
 */

@Composable
fun MainCompose(
    navHostController: NavHostController = rememberNavController(),
    onFinish: () -> Unit
) {

    //返回back堆栈的顶部条目
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    //返回当前route
    val currentRoute = navBackStackEntry?.destination?.route ?: Nav.BottomNavScreen.HomeScreen.route

    //是否为route == "main",主页面的内容
    if (isMainScreen(currentRoute)) {
        Scaffold(
            contentColor = MaterialTheme.colors.background,
            //标题栏
            topBar = {
                Column {
                    //内容不挡住状态栏 如果不设置颜色这里会自己适配，但可能产生闪烁
                    Spacer(
                        modifier = Modifier.background(MaterialTheme.colors.primary)
                            .statusBarsHeight().fillMaxWidth()
                    )

                    MainTopBar(Nav.bottomNavRoute.value, navHostController)
                }
            },
            //底部导航栏
            bottomBar = {
                Column {
                    BottomNavBar(Nav.bottomNavRoute.value, navHostController)
                    //内容不挡住导航栏 如果不设置颜色这里会自己适配，但可能产生闪烁
                    Spacer(
                        modifier = Modifier.background(MaterialTheme.colors.primary)
                            .navigationBarsHeight().fillMaxWidth()
                    )
                }
            },
            //内容
            content = { paddingValues: PaddingValues ->
                //内容嵌套在Scaffold中
                NavigationHost(navHostController, paddingValues, onFinish)

                OnTwoBackContent(navHostController)
            }
        )
    } else
    //独立页面
        NavigationHost(navHostController, onFinish = onFinish)

}

/**
 * 主页面的标题栏
 */
@Composable
private fun MainTopBar(bottomNavScreen: Nav.BottomNavScreen, navHostController: NavHostController) {

}

/**
 * 在主界面点击两次返回按钮,返回到手机桌面,再重新打开app,此时显示退出时的主界面
 */
@Composable
private fun OnTwoBackContent(navHostController: NavHostController) {
    if (Nav.twoBackFinishActivity) {
        LaunchedEffect(Unit) {
            navHostController.navigate(Nav.bottomNavRoute.value.route) {
                popUpTo(navHostController.graph.findStartDestination().id) {
                    saveState = true
                }
                //避免重建
                launchSingleTop = true
                //重新选择以前选择的项目时，恢复状态
                restoreState = true
            }
            Nav.twoBackFinishActivity = false
        }
    }
}

/**
 * 是否是首页的内容
 */
fun isMainScreen(route: String): Boolean = when (route) {
    Nav.BottomNavScreen.HomeScreen.route,
    Nav.BottomNavScreen.ProjectScreen.route,
    Nav.BottomNavScreen.SquareScreen.route,
    Nav.BottomNavScreen.PublicNumScreen.route,
    Nav.BottomNavScreen.MineScreen.route -> true
    else -> false
}