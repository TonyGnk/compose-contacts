package com.example.composecontacts.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.composecontacts.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    contactsViewModel: ContactsViewModel,
    contactsUiState: ContactsUiState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                shape = FloatingActionButtonDefaults.extendedFabShape,
                // Create a new contact
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add"
                    )
                },
                text = {
                    Text(stringResource(R.string.add_contact))
                },
                onClick = { },
            )
        },
        /*content of the screen. The lambda receives a PaddingValues that should be applied to the content root via Modifier.padding and Modifier.consumeWindowInsets to properly offset top and bottom bars. If using Modifier.verticalScroll, apply this modifier to the child of the scroll, and not on the scroll itself.*/

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            SearchTopBar(
                contactsUiState = contactsUiState,
                onSearchTextChange = contactsViewModel::onSearchTextChange,
                onSearch = contactsViewModel::onSearchTextChange,
                onActiveChange = { contactsViewModel::onToogleSearch },
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    contactsUiState: ContactsUiState,
    onSearchTextChange: (String) -> Unit,
    onSearch: (String) -> Unit = {},
    onActiveChange: (Boolean) -> Unit,
) {
    SearchBar(
        active = contactsUiState._isSearching,
        onActiveChange = onActiveChange,
        onSearch = onSearch,
        query = contactsUiState.searchText,
        onQueryChange = onSearchTextChange,
        placeholder = { Text(stringResource(R.string.search_placeholder)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search"
            )
        },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyColumn {
            val countriesList = listOf("India", "USA", "UK", "Canada", "Australia")
            items(countriesList) { country ->
                Text(
                    text = country,
                    modifier = Modifier.padding(
                        start = 8.dp,
                        top = 4.dp,
                        end = 8.dp,
                        bottom = 4.dp
                    )
                )
            }
        }
    }
}





