package com.example.inventory.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composecontacts.data.Item
import com.example.composecontacts.data.ItemsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

/**
 * ViewModel to retrieve all items in the Room database.
 */
class HomeViewModel(itemsRepository: ItemsRepository) : ViewModel() {

    private val _searchState = MutableStateFlow(HomeSearchState())
    val searchState: StateFlow<HomeSearchState> = _searchState

    val homeUiState: StateFlow<HomeUiState> =
        itemsRepository.getAllItemsStream().map { HomeUiState(it) }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun onSearch(force: Boolean = false) {
        if (searchState.value.searchQuery.isNotEmpty() && !force) {
            _searchState.update {
                it.copy(
                    searchQuery = "",
                )
            }
        } else {
            _searchState.update {
                it.copy(
                    isSearching = false,
                    searchQuery = "",
                )
            }
        }
    }

    fun onQueryChange(query: String) {
        _searchState.update {
            it.copy(
                searchQuery = query,
                isSearching = if (query.isNotEmpty()) true else it.isSearching
            )
        }
    }

    fun getSearchItems(): List<Item> {
        val searchQuery = searchState.value.searchQuery
        //If empty return empty list
        return if (searchQuery.isEmpty()) listOf()
        else homeUiState.value.itemList.filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }

    }
}

/**
 * Ui State for HomeScreen
 */
data class HomeUiState(
    val itemList: List<Item> = listOf(),
)

data class HomeSearchState(
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val searchItemList: List<Item> = listOf(),
)
