package com.plcoding.bookpedia.book.presentatinon.book_detail.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.book_cover
import cmp_bookpedia.composeapp.generated.resources.book_error_2
import cmp_bookpedia.composeapp.generated.resources.go_back
import coil3.compose.rememberAsyncImagePainter
import com.plcoding.bookpedia.core.presentation.DarkBlue
import com.plcoding.bookpedia.core.presentation.DesertWhite
import com.plcoding.bookpedia.core.presentation.SandYellow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BlurredImageBackground(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    content: @Composable () -> Unit
){
    var imageLoadResult by remember {
        mutableStateOf<Result<Painter>?>(null)
    }
    val painter = rememberAsyncImagePainter( // IMAGE DRAWABLE !
        model = imageUrl,
        onSuccess = {
            val size = it.painter.intrinsicSize
            imageLoadResult = if (size.width > 1 && size.height > 1){
                Result.success(it.painter)
            } else {
                Result.failure(Exception("Invalid image size"))
            }

        },
    )


    Box{
        Column ( // Background
            modifier = Modifier.fillMaxSize()
        ){
            Box(
                modifier = Modifier.weight(0.3f)
                    .fillMaxWidth()
                    .background(DarkBlue)
            ){
                imageLoadResult?.getOrNull()?.let { painter ->
                    Image(
                        painter = painter,
                        contentDescription = stringResource(Res.string.book_cover),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                            .blur(20.dp)
                    )
                }

            }

            Box(
                modifier = Modifier.weight(0.7f)
                    .fillMaxWidth()
                    .background(DesertWhite)
            )
        }

        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.TopStart)
                .padding(top = 16.dp, start = 16.dp)
                .statusBarsPadding()
        ){
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(Res.string.go_back),
                tint = Color.White
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.15f))

            ElevatedCard(
                modifier = Modifier.height(370.dp)
                    .aspectRatio(2/3f),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = Color.Transparent
                ),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 15.dp
                )
            ){
                AnimatedContent(
                    targetState = imageLoadResult,
                    modifier = Modifier.fillMaxSize()
                ){ result ->
                    when(result){
                        null -> CircularProgressIndicator()
                        else -> {
                            Box {
                                Image(
                                    painter = if(result.isSuccess) painter else painterResource(Res.drawable.book_error_2),
                                    contentDescription = stringResource(Res.string.book_cover),
                                    contentScale = if (result.isSuccess) ContentScale.Crop else ContentScale.Fit ,
                                    modifier = Modifier.fillMaxSize()
                                        .background(Color.Transparent)
                                )

                                IconButton(
                                    onClick = onFavoriteClick,
                                    modifier = Modifier.align(Alignment.BottomEnd)
                                        .padding(bottom = 16.dp, end = 16.dp)
                                    .background(
                                            brush = Brush.radialGradient(
                                                colors = listOf(SandYellow, Color.Transparent),
                                                radius = 70f
                                            )
                                        )
                                ){
                                    Icon(
                                        imageVector = if (isFavorite) Icons.Outlined.Favorite else Icons.Filled.Favorite,
                                        contentDescription = if(isFavorite) "Remove from favorites" else "Add to favorites",
                                        tint = Color.Red,
                                    )
                                }
                            }

                        }
                    }
                }
            }

            content()

        }

    }



}