package org.com.hcmurs.ui.screens.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.com.hcmurs.model.BlogResponse
import org.com.hcmurs.repositories.apis.blog.BlogRepository
import javax.inject.Inject

sealed class BlogUiState {
    object Loading : BlogUiState()
    data class Success(val blogs: List<BlogResponse>) : BlogUiState()
    data class Error(val message: String) : BlogUiState()
}

sealed class BlogDetailUiState {
    object Loading : BlogDetailUiState()
    data class Success(val blog: BlogResponse) : BlogDetailUiState()
    data class Error(val message: String) : BlogDetailUiState()
}

@HiltViewModel
class BlogViewModel @Inject constructor(
    private val repository: BlogRepository
) : ViewModel() {

    // Home screen blogs (limited)
    private val _homeBlogsState = MutableStateFlow<BlogUiState>(BlogUiState.Loading)
    val homeBlogsState: StateFlow<BlogUiState> = _homeBlogsState.asStateFlow()

    // Full blog list
    private val _blogsState = MutableStateFlow<BlogUiState>(BlogUiState.Loading)
    val blogsState: StateFlow<BlogUiState> = _blogsState.asStateFlow()

    // Blog detail
    private val _blogDetailState = MutableStateFlow<BlogDetailUiState>(BlogDetailUiState.Loading)
    val blogDetailState: StateFlow<BlogDetailUiState> = _blogDetailState.asStateFlow()

    // Featured blogs
    private val _featuredBlogs = MutableStateFlow<List<BlogResponse>>(emptyList())
    val featuredBlogs: StateFlow<List<BlogResponse>> = _featuredBlogs.asStateFlow()

    // Pagination state
    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _hasMorePages = MutableStateFlow(true)
    val hasMorePages: StateFlow<Boolean> = _hasMorePages.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private val allBlogs = mutableListOf<BlogResponse>()

    // Search functionality
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filteredBlogs = MutableStateFlow<List<BlogResponse>>(emptyList())
    val filteredBlogs: StateFlow<List<BlogResponse>> = _filteredBlogs.asStateFlow()

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()

    init {
        loadHomeBlogs()
    }

    // Load blogs for home screen (first 5)
    fun loadHomeBlogs() {
        viewModelScope.launch {
            _homeBlogsState.value = BlogUiState.Loading
            try {
                val response = repository.getBlogs(page = 0, size = 5, sort = "createdAt,desc")
                if (response.isSuccessful) {
                    val blogs = response.body()?.data?.content ?: emptyList<BlogResponse>()
                    _homeBlogsState.value = BlogUiState.Success(blogs)
                } else {
                    _homeBlogsState.value = BlogUiState.Error("Failed to load blogs: ${response.message()}")
                }
            } catch (e: Exception) {
                _homeBlogsState.value = BlogUiState.Error("Network error: ${e.message}")
            }
        }
    }

    // Load all blogs with pagination
    fun loadAllBlogs(refresh: Boolean = false) {
        viewModelScope.launch {
            if (refresh) {
                _currentPage.value = 0
                allBlogs.clear()
                _hasMorePages.value = true
                _blogsState.value = BlogUiState.Loading
            }

            try {
                val response = repository.getBlogs(
                    page = _currentPage.value,
                    size = 10,
                    sort = "createdAt,desc"
                )

                if (response.isSuccessful) {
                    val pageData = response.body()?.data
                    val blogs = pageData?.content ?: emptyList<BlogResponse>()

                    if (refresh) {
                        allBlogs.clear()
                    }
                    allBlogs.addAll(blogs)

                    _blogsState.value = BlogUiState.Success(allBlogs.toList())
                    _hasMorePages.value = !pageData?.last!! ?: false
                    _currentPage.value += 1
                } else {
                    _blogsState.value = BlogUiState.Error("Failed to load blogs: ${response.message()}")
                }
            } catch (e: Exception) {
                _blogsState.value = BlogUiState.Error("Network error: ${e.message}")
            }
        }
    }

    // Load more blogs (pagination)
    fun loadMoreBlogs() {
        if (_hasMorePages.value && !_isLoadingMore.value) {
            viewModelScope.launch {
                _isLoadingMore.value = true
                try {
                    val response = repository.getBlogs(
                        page = _currentPage.value,
                        size = 10,
                        sort = "createdAt,desc"
                    )

                    if (response.isSuccessful) {
                        val pageData = response.body()?.data
                        val blogs = pageData?.content ?: emptyList<BlogResponse>()

                        allBlogs.addAll(blogs)
                        _blogsState.value = BlogUiState.Success(allBlogs.toList())
                        _hasMorePages.value = !pageData?.last!! ?: false
                        _currentPage.value += 1
                    }
                } catch (e: Exception) {
                    // Handle error silently for load more
                } finally {
                    _isLoadingMore.value = false
                }
            }
        }
    }

    // Load blog detail
    fun loadBlogDetail(blogId: Int) {
        viewModelScope.launch {
            _blogDetailState.value = BlogDetailUiState.Loading
            try {
                val response = repository.getBlogById(blogId)
                if (response.isSuccessful) {
                    val blog = response.body()?.data
                    if (blog != null) {
                        _blogDetailState.value = BlogDetailUiState.Success(blog)
                    } else {
                        _blogDetailState.value = BlogDetailUiState.Error("Blog not found")
                    }
                } else {
                    _blogDetailState.value = BlogDetailUiState.Error("Failed to load blog: ${response.message()}")
                }
            } catch (e: Exception) {
                _blogDetailState.value = BlogDetailUiState.Error("Network error: ${e.message}")
            }
        }
    }

    fun getFeaturedBlogs(quantity: Int){
        viewModelScope.launch {
            try {
                val response = repository.getBlogs(page = 0, size = quantity, sort = "createdAt,desc")
                if (response.isSuccessful) {
                    val blogs = response.body()?.data?.content ?: emptyList<BlogResponse>()
                    _featuredBlogs.value = blogs
                } else {
                    _featuredBlogs.value = emptyList()
                }
            } catch (e: Exception) {
                _featuredBlogs.value = emptyList()
            }
        }
    }

    fun refresh() {
        loadHomeBlogs()
        loadAllBlogs(refresh = true)
    }

    // Search function for client-side filtering
    fun searchBlogs(query: String) {
        _searchQuery.value = query
        _isSearchActive.value = query.isNotBlank()
        
        if (query.isBlank()) {
            _filteredBlogs.value = emptyList()
        } else {
            val filtered = allBlogs.filter { blog ->
                blog.title.contains(query, ignoreCase = true) ||
                blog.content.contains(query, ignoreCase = true) ||
                (blog.excerpt?.contains(query, ignoreCase = true) == true)
            }
            _filteredBlogs.value = filtered
        }
    }

    // Clear search
    fun clearSearch() {
        _searchQuery.value = ""
        _isSearchActive.value = false
        _filteredBlogs.value = emptyList()
    }

    // Get display blogs (either filtered or all blogs)
    fun getDisplayBlogs(): List<BlogResponse> {
        return if (_isSearchActive.value) {
            _filteredBlogs.value
        } else {
            allBlogs.toList()
        }
    }
}