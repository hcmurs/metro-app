/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.screens.metro.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeScreenUiState(
    val isLoading: Boolean = true,
    val isBannerLoading: Boolean = true,
    val isQuickActionsLoading: Boolean = true,
    val isFeaturedBlogsLoading: Boolean = true,
    val isBlogSectionLoading: Boolean = true,
)

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    init {
        loadHomeContent()
    }

    private fun loadHomeContent() {
        viewModelScope.launch {
            // Simulate initial loading
            _uiState.value = HomeScreenUiState(
                isLoading = true,
                isBannerLoading = true,
                isQuickActionsLoading = true,
                isFeaturedBlogsLoading = true,
                isBlogSectionLoading = true,
            )

            // Simulate banner loading (show shimmer for 800ms)
            delay(800)
            _uiState.value = _uiState.value.copy(
                isBannerLoading = false,
            )

            // Simulate quick actions loading (show shimmer for additional 400ms)
            delay(400)
            _uiState.value = _uiState.value.copy(
                isQuickActionsLoading = false,
            )

            // Blog sections loading - show shimmer for at least 2 seconds
            // This ensures users see smooth loading animation even if API fails quickly
            delay(2000)
            _uiState.value = _uiState.value.copy(
                isFeaturedBlogsLoading = false,
                isBlogSectionLoading = false,
                isLoading = false,
            )
        }
    }

    fun refresh() {
        loadHomeContent()
    }
}
