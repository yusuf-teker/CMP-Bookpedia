package com.plcoding.bookpedia.book.presentatinon.book_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.rating
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.presentatinon.book_detail.components.BlurredImageBackground
import com.plcoding.bookpedia.book.presentatinon.book_detail.components.BookChip
import com.plcoding.bookpedia.book.presentatinon.book_detail.components.TitledContent
import com.plcoding.bookpedia.core.presentation.SandYellow
import org.jetbrains.compose.resources.stringResource
import kotlin.math.round


@Composable
fun BookDetailScreenRoot(
    viewModel: BookDetailViewModel,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
){
    val state by viewModel.state.collectAsStateWithLifecycle()
    BookDetailScreen(
        state = state,
        onAction = {
            when(it){
                is BookDetailAction.OnBackClick -> onBackClick()
                else -> {}
            }
            viewModel.onAction(it)
        },
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookDetailScreen(
    state: BookDetailState,
    onAction: (BookDetailAction) -> Unit,
) {
    BlurredImageBackground(
        imageUrl = state.book?.imageUrl,
        isFavorite = state.isFavorite,
        onFavoriteClick = {
            onAction(BookDetailAction.OnFavoriteClick)
        },
        onBackClick = {
            onAction(BookDetailAction.OnBackClick)
        },
    ){
        if (state.book != null) {
            Column(
                modifier = Modifier.widthIn(max = 700.dp).fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = state.book.title,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = state.book.authors.joinToString(),
                    style = MaterialTheme.typography.titleMedium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    state.book.averageRating?.let { rating ->
                        TitledContent(
                            title = stringResource(Res.string.rating)
                        ){
                            BookChip {

                                    Text(
                                        text = round(rating * 10).toString(),
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = SandYellow
                                    )

                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))

                    state.book.numPages?.let { pages ->
                        TitledContent(
                            title = "Pages"
                        ){
                            BookChip {
                                Text(
                                    text = pages.toString(),
                                )
                            }
                        }
                    }
                }

                if(state.book.languages.isNotEmpty()){
                    TitledContent(
                        title = "Languages"
                    ){
                        FlowRow(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.wrapContentSize(Alignment.Center)
                                .padding(top = 8.dp),

                        ) {
                            state.book.languages.forEach { language ->
                                BookChip(
                                    modifier = Modifier.padding(2.dp),
                                ) {
                                    Text(
                                        text = language.uppercase(),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }

                Text(
                    text = "Synopsis",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Start)
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 8.dp)
                )

                if (state.isLoading){
                    CircularProgressIndicator()
                }else{
                    Text(
                        text = if (!state.book.description.isNullOrBlank()){
                            state.book.description
                        } else {
                            "No Description Available"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify,
                        color = if (!state.book.description.isNullOrBlank()){
                            Color.Black
                        }else{
                            Color.Black.copy(alpha = 0.4f)
                        },
                        modifier = Modifier.padding(8.dp)
                    )

                }


            }
        }
    }
}
