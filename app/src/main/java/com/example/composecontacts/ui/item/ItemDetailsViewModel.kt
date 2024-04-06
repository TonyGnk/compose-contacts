package com.example.composecontacts.ui.item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composecontacts.data.ItemsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

// ViewModel to retrieve, update and delete an item from the [ItemsRepository]'s data source.
class ItemDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle[ItemDetailsDestination.itemIdArg])
    val uiState: StateFlow<ItemDetailsUiState> = itemsRepository.getItemStream(itemId)
        .filterNotNull()
        .map {
            ItemDetailsUiState(itemDetails = it.toItemDetails())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ItemDetailsUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

//    fun reduceQuantityByOne() {
//        viewModelScope.launch {
//            val currentItem = uiState.value.itemDetails.toItem()
//            if (currentItem.quantity > 0) {
//                itemsRepository.updateItem(currentItem.copy(quantity = currentItem.quantity - 1))
//            }
//        }
//    }

    suspend fun deleteItem() {
        itemsRepository.deleteItem(uiState.value.itemDetails.toItem())
    }
}

// UI state for ItemDetailsScreen
data class ItemDetailsUiState(
    val itemDetails: ItemDetails = ItemDetails()
)
