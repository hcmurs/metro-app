package org.com.hcmurs.model

enum class BlogCategory {
    SERVICE_UPDATE,        // Cập nhật dịch vụ
    SAFETY_GUIDELINE,      // Hướng dẫn an toàn
    RIDER_TIPS,            // Mẹo cho hành khách
    TECH_BEHIND_METRO,     // Công nghệ trong hệ thống metro
    SCHEDULE_INFO,         // Thông tin lịch trình
    PROMOTION,             // Khuyến mãi / ưu đãi
    PUBLIC_ANNOUNCEMENT    // Thông báo chung
}

enum class BlogTag {
    STATION_GUIDE,         // Hướng dẫn nhà ga
    ELECTRONIC_TICKETING,  // Hướng dẫn dùng vé điện tử
    MAP_UPDATE,            // Cập nhật bản đồ tuyến
    DISCOUNT,              // Ưu đãi vé
    MAINTENANCE_NOTICE,    // Thông báo bảo trì
    PEAK_HOUR_TIPS,        // Mẹo đi tàu giờ cao điểm
    NEW_LINE_OPENING,      // Tuyến mới khai trương
    MOBILE_APP,            // Hướng dẫn dùng app mobile
    LOST_AND_FOUND,        // Thất lạc đồ
    PUBLIC_ANNOUNCEMENT,   // Thông báo chung
    ACCESSIBILITY          // Hỗ trợ người khuyết tật
}

data class BlogResponse(
    val id: Int? = null,
    val category: BlogCategory,
    val title: String,
    val author: String,
    val date: String? = null,
    val comments: Int? = null,
    val image: String,
    val content: String,
    val tags: List<BlogTag> = emptyList(),
    val readTime: String? = null,
    val excerpt: String? = null,
    val views: Int? = null,
    val createdAt: String,
    val updatedAt: String
)

data class BlogPageData(
    val content: List<BlogResponse>,
    val pageable: Pageable,
    val totalElements: Int,
    val totalPages: Int,
    val last: Boolean,
    val first: Boolean,
    val size: Int,
    val number: Int,
    val sort: Sort,
    val numberOfElements: Int,
    val empty: Boolean
)

data class Pageable(
    val pageNumber: Int,
    val pageSize: Int,
    val sort: Sort,
    val offset: Long,
    val paged: Boolean,
    val unpaged: Boolean
)

data class Sort(
    val sorted: Boolean,
    val unsorted: Boolean,
    val empty: Boolean
)