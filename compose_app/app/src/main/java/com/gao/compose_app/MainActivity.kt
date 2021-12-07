package com.gao.compose_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.gao.compose_app.data.Article
import com.gao.compose_app.ui.theme.Compose_appTheme
import com.google.accompanist.insets.ProvideWindowInsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //设置为沉浸式状态栏
            WindowCompat.setDecorFitsSystemWindows(window, false)
            Compose_appTheme {
                // A surface container using the 'background' color from the theme
//                Surface(color = MaterialTheme.colors.background) {
//                    DefaultPreview()
//                }
                //可以获取状态栏高度
                ProvideWindowInsets {
                    //主界面
                    MainCompose(onFinish = { finish() })
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val aticleList = mutableListOf<Article>()
    for (i in 0..20) {
        val msg = Article(
            "开始学习Compose",
            "2021.12.6",
            "Jetpack Compose 作为一款用于构建原生 Android UI 的现代工具包，Jetpack Compose 用更少的代码、更强大的工具和更直观的 Kotlin API 简化并加速 Android 上的 UI 开发。\n\n" +
                    "在过去的两年里，我们一直在开发 Compose，并得到了 Android 社区的反馈和参与。直到我们发布 1.0 版本时，Google Play 中已经有超过 2000 个应用在使用 Compose 了。事实上，Google Play 应用本身也在使用 Compose！" +
                    "但这还不是全部，我们一直在与一些顶级应用的开发人员进行合作，他们的反馈和支持帮助我们使 1.0 版本更加强大。例如，Square 告诉我们，通过使用 Compose，他们可以更加专注于UI基础设施所特有的东西，" +
                    "而不是解决构建声明式UI框架这一更广泛的问题。Monzo 说，Compose 让他们能够 \"更快构建更高质量的UI效果\"。而 Twitter则很好地总结了这一点。\"我们喜欢它! \""
        )
        aticleList.add(msg)
    }
    ShowDatas(aticleList = aticleList)
}

@Composable
fun ItemView(msg: Article) {
    Compose_appTheme {
        Row(modifier = Modifier.padding(all = 8.dp)) {
            Surface(
                modifier = Modifier.size(60.dp),
                shape = CircleShape
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.icon_compose),
                    contentDescription = "good",
                    modifier = Modifier
                        .size(50.dp)
                        .scale(1.0F),
                    contentScale = ContentScale.FillBounds
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            //创建一个开关值赋值为FALSE，委托remeber去记住这个开关值
            var isExpand by remember {
                mutableStateOf(false)
            }
            Column(modifier = Modifier.clickable { isExpand = !isExpand }) {
                Text(text = "标题： ${msg.title}!", fontSize = 14.sp)
                Text(text = "时间：${msg.time}", fontSize = 14.sp)
                Text(
                    text = "描述：${msg.desc}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.secondaryVariant,
                    maxLines = if (isExpand) Int.MAX_VALUE else 1,//如果开关值为TRUE展开全部，否则只显示一行
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Composable
fun ShowDatas(aticleList: MutableList<Article>) {
    LazyColumn {
        items(aticleList) { article ->
            ItemView(msg = article)
        }
    }
}

