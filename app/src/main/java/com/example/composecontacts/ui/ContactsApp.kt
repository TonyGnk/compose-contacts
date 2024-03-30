package com.example.composecontacts.ui

import android.annotation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.input.nestedscroll.*
import androidx.compose.ui.tooling.preview.*
import com.example.composecontacts.ui.screens.*
import com.example.composecontacts.ui.theme.*
import androidx.lifecycle.viewmodel.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsApp() {
	val contactsViewModel : ContactsViewModel = viewModel()
	val contactsUiState = contactsViewModel.uiState.collectAsState().value
	HomeScreen(
		contactsViewModel = contactsViewModel,
		contactsUiState = contactsUiState,
	)
}

// @Composable
// fun MarsTopAppBar(scrollBehavior : TopAppBarScrollBehavior, modifier : Modifier = Modifier) {
// 	CenterAlignedTopAppBar(
// 		scrollBehavior = scrollBehavior,
// 		title = {
// 			Text(
// 				text = stringResource(R.string.app_name),
// 				style = MaterialTheme.typography.headlineSmall,
// 			)
// 		},
// 		modifier = modifier
// 	)
// }

@Preview(showBackground = true)
@Composable
fun ResultScreenPreviewSuccess() {
	ComposeContactsTheme {
		ContactsApp()
	}
}
