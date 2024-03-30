package com.example.composecontacts.ui.screens

data class ContactsUiState(
	val isShowingHomepage : Boolean = true,
	val searchText : String = "",
	val _isSearching : Boolean = false
)
