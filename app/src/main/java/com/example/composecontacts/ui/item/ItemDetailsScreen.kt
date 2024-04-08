package com.example.composecontacts.ui.item

import android.graphics.BitmapFactory
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composecontacts.R
import com.example.composecontacts.data.Item
import com.example.composecontacts.ui.AppViewModelProvider
import com.example.composecontacts.ui.navigation.NavigationDestination
import com.example.composecontacts.ui.theme.ComposeContactsTheme
import kotlinx.coroutines.launch


object ItemDetailsDestination : NavigationDestination {
    override val route = "item_details"
    override val titleRes = R.string.item_detail_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailsScreen(
    navigateToEditItem: (Int) -> Unit,
    navigateBack: () -> Unit,
    viewModel: ItemDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = { navigateToEditItem(uiState.value.itemDetails.id) },
//                shape = MaterialTheme.shapes.medium,
//                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
//
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Edit,
//                    contentDescription = stringResource(R.string.edit_item_title),
//                )
//            }
//        },
        modifier = modifier
    ) { innerPadding ->
        ItemDetailsBody(
            itemDetailsUiState = uiState.value,
            onCall = { viewModel.callNumber(context) },
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteItem()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
private fun ItemDetailsBody(
    itemDetailsUiState: ItemDetailsUiState,
    onCall: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
        ItemDetails(
            itemDetailsUiState = itemDetailsUiState,
            item = itemDetailsUiState.itemDetails.toItem(),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onCall,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
        ) {
            Text(stringResource(R.string.call))
        }
        OutlinedButton(
            onClick = { deleteConfirmationRequired = true },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.delete))
        }
        // if (deleteConfirmationRequired) {

        DeleteConfirmationDialog(
            onDeleteConfirm = {
                deleteConfirmationRequired = false
                onDelete()
            },
            onDeleteCancel = { deleteConfirmationRequired = false },
            showDialog = deleteConfirmationRequired,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        )

    }
}

@Composable
fun ItemDetails(
    itemDetailsUiState: ItemDetailsUiState,
    item: Item, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(id = R.dimen.padding_medium)
            )
        ) {
            //Image 80x80
            if (itemDetailsUiState.itemDetails.imageData != null) {
                Image(
                    bitmap = convertData(itemDetailsUiState.itemDetails.imageData),
                    contentDescription = "An Image",
                    modifier = Modifier.size(80.dp)
                )
            }

            ItemDetailsRow(
                name = item.name,
                number = item.number,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
        }
    }
}

fun convertData(byteArray: ByteArray?): ImageBitmap {
    if (byteArray == null) {
        return ImageBitmap(1, 1)
    }

    //Covert the byteArray to ImageBitmap
    val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    return bitmap.asImageBitmap()
}

@Composable
private fun ItemDetailsRow(
    name: String, number: Long, modifier: Modifier = Modifier
) {
    //convert the long to string properly
    //For every digit in the number, add a character to the string
    val number2 = number.toString()

    Row(modifier = modifier) {
        Text(name)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = number2, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    showDialog: Boolean,
    modifier: Modifier = Modifier
) {

    AnimatedVisibility(
        visible = showDialog,
        enter = slideInVertically(initialOffsetY = { 12 })
    ) {
        AlertDialog(
            onDismissRequest = { /* Do nothing */ },
            title = { Text(stringResource(R.string.attention)) },
            text = { Text(stringResource(R.string.delete_question)) },
            modifier = modifier,
            dismissButton = {
                TextButton(onClick = onDeleteCancel) {
                    Text(stringResource(R.string.no))
                }
            },
            confirmButton = {
                TextButton(onClick = onDeleteConfirm) {
                    Text(stringResource(R.string.yes))
                }
            }
        )
    }

}


@Preview(showBackground = true)
@Composable
fun ItemDetailsScreenPreview() {
    ComposeContactsTheme {
        ItemDetailsBody(
            ItemDetailsUiState(
                itemDetails = ItemDetails(1, null, "Pen", "23105555")
            ),
            onCall = {},
            onDelete = {},
        )
    }
}
