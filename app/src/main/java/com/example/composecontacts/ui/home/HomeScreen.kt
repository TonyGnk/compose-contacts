package com.example.composecontacts.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composecontacts.R
import com.example.composecontacts.data.Item
import com.example.composecontacts.ui.AppViewModelProvider
import com.example.composecontacts.ui.item.convertData
import com.example.composecontacts.ui.navigation.NavigationDestination
import com.example.composecontacts.ui.theme.ComposeContactsTheme
import com.example.inventory.ui.home.HomeSearchState
import com.example.inventory.ui.home.HomeViewModel

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigateToItemEntry: () -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val homeSearchUiState by viewModel.searchState.collectAsState()

    Scaffold(
        topBar = {
            HomeSearchBar(
                uiState = homeSearchUiState,
                model = viewModel,
                onItemClick = navigateToItemUpdate,
            )

        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(stringResource(R.string.item_entry_title)) },
                icon = {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = stringResource(R.string.item_entry_title)
                    )
                },
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = navigateToItemEntry
            )
        },
        floatingActionButtonPosition = FabPosition.EndOverlay,
    ) { innerPadding ->
        HomeBody(
            itemList = homeUiState.itemList,
            onItemClick = navigateToItemUpdate,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeSearchBar(
    uiState: HomeSearchState,
    model: HomeViewModel,
    onItemClick: (Int) -> Unit,
) {
    DockedSearchBar(
        query = uiState.searchQuery,
        onQueryChange = { model.onQueryChange(it) },
        onSearch = {},
        active = uiState.isSearching,
        onActiveChange = { model.onSearch() },
        placeholder = {
            Text(
                stringResource(R.string.search_placeholder)
            )
        },
        leadingIcon = {
            //if isSearching back icon. If not search
            if (uiState.isSearching) {
                IconButton(onClick = { model.onSearch(true) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Close",
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                )
            }
        },
        trailingIcon = {
            if (uiState.isSearching) {
                IconButton(onClick = { model.onSearch() }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                    )
                }
            }
        },
        //Add space left and right
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 7.dp)
            .statusBarsPadding()
    ) {
        ContactsList(
            itemList = model.getSearchItems(),
            onItemClick = { onItemClick(it.id) },
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
        )
    }
}

@Composable
private fun HomeBody(
    itemList: List<Item>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (itemList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        )
        {
            Text(
                text = stringResource(R.string.no_item_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                //modifier at the center of the screen
            )
        }

    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            ContactsList(
                itemList = itemList,
                onItemClick = { onItemClick(it.id) },
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun ContactsList(
    itemList: List<Item>,
    onItemClick: (Item) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = itemList, key = { it.id }) { item ->
            ContactItem(
                item = item,
                onItemClick = { onItemClick(item) },
            )
        }
    }
}

@Composable
private fun ContactItem(
    item: Item,
    onItemClick: () -> Unit,
) {
    ListItem(
        modifier = Modifier
            .clickable(onClick = onItemClick),
        leadingContent = {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(43.dp)
                    .background(color = MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                if (item.imageData != null) {
                    Image(
                        bitmap = convertData(item.imageData),
                        contentDescription = "An Image",
                        contentScale = ContentScale.Crop
                    )
                }
                //Display the first letter of the name if there is no image
                else {
                    Text(
                        text = item.name.first().toString(),
                        fontSize = 26.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.W500,
                        modifier = Modifier.wrapContentSize(Alignment.Center)
                    )
                }
            }
        },
        headlineContent = {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)

            )
        },
    )
}

@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
    ComposeContactsTheme {
        HomeBody(listOf(
            Item(1, "Γιαννακοβίτης Μανώλης", 23105555),
            Item(2, "Στογιάννου Πόππη", 23105555),
            Item(3, "Καρυτόπουλος Γιάννης", 23105555)
        ), onItemClick = {})
    }
}