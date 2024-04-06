package com.example.composecontacts.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.composecontacts.data.Item
import com.example.composecontacts.data.ItemsRepository

//ViewModel to validate and insert items in the Room database.
class ItemEntryViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {


    var itemUiState by mutableStateOf(ItemUiState())
        private set


    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && number.toString().isNotBlank()
        }
    }

    suspend fun saveItem() {
        if (validateInput()) {
            println("The number is ${itemUiState.itemDetails.number}")
            itemsRepository.insertItem(itemUiState.itemDetails.toItem())
        }
    }
}


data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

data class ItemDetails(
    val id: Int = 0,
    val name: String = "",
    val number: String = "",
)


/**
 * Extension function to convert [ItemDetails] to [Item]. If the value of [ItemDetails.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemDetails.quantity] is not a valid [Int], then the quantity will be set to 0
 */
fun ItemDetails.toItem(): Item = Item(
    id = id,
    name = name,
    number = number.toLongOrNull() ?: 0,
)

//fun Item.formatedPrice(): String {
//    return NumberFormat.getCurrencyInstance().format(price)
//}


// Extension function to convert [Item] to [ItemUiState]

fun Item.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)


// Extension function to convert [Item] to [ItemDetails]
fun Item.toItemDetails(): ItemDetails = ItemDetails(
    id = id,
    name = name,
    number = number.toString(),
)
