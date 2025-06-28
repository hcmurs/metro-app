//package org.com.hcmurs.ui.screens.metro.account
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import org.com.hcmurs.repositories.apis.auth.AuthRepository
//import org.com.hcmurs.repositories.apis.auth.UserProfileData // Import UserProfileData
//import javax.inject.Inject
//
//@HiltViewModel
//class AccountViewModel @Inject constructor(
//    private val authRepository: AuthRepository // Cần AuthRepository để gọi API
//) : ViewModel() {
//
//    private val _userProfile = MutableStateFlow<UserProfileData?>(null)
//    val userProfile: StateFlow<UserProfileData?> = _userProfile
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading
//
//    private val _errorMessage = MutableStateFlow<String?>(null)
//    val errorMessage: StateFlow<String?> = _errorMessage
//
//    init {
//        // Tải profile khi ViewModel được khởi tạo
//        // Điều này đảm bảo dữ liệu được tải ngay khi màn hình tài khoản hiển thị
//        refreshUserProfile()
//    }
//
//    fun refreshUserProfile() {
//        viewModelScope.launch {
//            _isLoading.value = true
//            _errorMessage.value = null
//            try {
//                // Lấy token từ AuthRepository (nếu bạn đã lưu nó sau login)
//                // Hoặc bạn có thể truyền token qua navigate arguments nếu muốn
//                val token = authRepository.getToken() // Giả định bạn có hàm getToken() trong AuthRepository
//
//                if (token != null) {
//                    val response = authRepository.getUserProfile("Bearer $token") // Gọi API
//                    if (response.status == 200 && response.data != null) { //
//                        _userProfile.value = response.data
//                    } else {
//                        _errorMessage.value = response.message ?: "Failed to load user profile"
//                    }
//                } else {
//                    _errorMessage.value = "No token found. Please log in again."
//                }
//            } catch (e: Exception) {
//                _errorMessage.value = "Error loading profile: ${e.message}"
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//
//    fun logout() {
//        viewModelScope.launch {
//            _isLoading.value = true
//            _errorMessage.value = null
//            try {
//                authRepository.logout() // Gọi hàm logout từ AuthRepository
//                // Sau khi logout thành công, xóa profile và chuyển trạng thái authenticated
//                _userProfile.value = null
//                // Có thể cần một cách để thông báo cho navigation rằng người dùng đã logout
//                // Ví dụ: bắn ra một sự kiện hoặc sử dụng cờ
//            } catch (e: Exception) {
//                _errorMessage.value = "Error during logout: ${e.message}"
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//
//}