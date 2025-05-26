package org.com.hcmurs.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.com.hcmurs.common.enum.LoadStatus
import org.com.hcmurs.model.NoteItem
import org.com.hcmurs.repositories.Api
import org.com.hcmurs.repositories.MainLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val notes: List<NoteItem> = emptyList(),
    val status: LoadStatus = LoadStatus.Init(),
    val selectedIndex: Int = -1
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val log: MainLog?,
    private val api: Api?
) : ViewModel() {
    val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    fun loadNotes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = LoadStatus.Loading())
            try {
                if (api != null) {
                    val loadNotes = api.loadNotes()
                    _uiState.value =
                        _uiState.value.copy(notes = loadNotes, status = LoadStatus.Success())
                }
            } catch (ex: Exception) {
                _uiState.value =
                    _uiState.value.copy(status = LoadStatus.Error(ex.message.toString()))
            }
        }
    }

    fun reset() {
        _uiState.value = _uiState.value.copy(status = LoadStatus.Init())
    }

    fun deleteNote(dt: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = LoadStatus.Loading())
            try {
                if (api != null) {
                    api.deleteNote(dt)
                    val loadNotes = api.loadNotes()
                    _uiState.value =
                        _uiState.value.copy(notes = loadNotes, status = LoadStatus.Success())
                }
            } catch (ex: Exception) {
                _uiState.value =
                    _uiState.value.copy(status = LoadStatus.Error(ex.message.toString()))
            }
        }
    }

    fun selectNote(index: Int) {
        _uiState.value = _uiState.value.copy(selectedIndex = index)
    }

}