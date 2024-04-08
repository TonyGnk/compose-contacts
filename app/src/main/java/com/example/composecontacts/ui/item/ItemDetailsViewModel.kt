package com.example.composecontacts.ui.item

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
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

    @SuppressLint("StateFlowValueCalledInComposition")
    fun callNumber(context: Context) {
        if (uiState.value.itemDetails.number.length != 10) return

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel: ${uiState.value.itemDetails.number}")
            startActivity(context, intent, bundleOf())
        } else {
            val activity = context as Activity
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.CALL_PHONE), 777
            )
        }


//        //Get context
//
//        //options Bundle?
//        val options = Bundle()
//
//        val intent = Intent(Intent.ACTION_CALL)
//        intent.setData(Uri.parse(uiState.value.itemDetails.number))
//        startActivity(
//            context,
//            intent,
//            options
//        )
    }


    suspend fun deleteItem() {
        itemsRepository.deleteItem(uiState.value.itemDetails.toItem())
    }
}

// UI state for ItemDetailsScreen
data class ItemDetailsUiState(
    val itemDetails: ItemDetails = ItemDetails()
)
