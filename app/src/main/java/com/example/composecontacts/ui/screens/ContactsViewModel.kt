package com.example.composecontacts.ui.screens

import androidx.lifecycle.*
import kotlinx.coroutines.flow.*

class ContactsViewModel() : ViewModel() {
	private val _uiState = MutableStateFlow(ContactsUiState())
	val uiState : StateFlow<ContactsUiState> = _uiState

	init {
	}

	fun onSearchTextChange(text : String) {
		_uiState.update {
			it.copy(
				searchText = text
			)
		}
	}

	fun onToogleSearch() {
		_uiState.update {
			it.copy(
				_isSearching = !it._isSearching
			)
		}
		if (!_uiState.value._isSearching) {
			onSearchTextChange("")
		}
	}
}
