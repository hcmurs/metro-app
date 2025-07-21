package org.com.hcmurs.utils

object TranslationHelper {
    
    fun getLocalizedTicketName(originalName: String, language: String): String {
        return when (language) {
            "en" -> when {
                originalName.contains("Vé đơn") -> "Single Ticket"
                originalName.contains("Vé 1 ngày") -> "1-Day Pass"
                originalName.contains("Vé 3 ngày") -> "3-Day Pass"
                originalName.contains("Vé tuần") -> "Weekly Pass"
                originalName.contains("Vé tháng") -> "Monthly Pass"
                originalName.contains("sinh viên") -> "Student Pass"
                else -> originalName
            }
            else -> originalName
        }
    }

    fun getLocalizedValidity(validity: String, language: String): String {
        return when (language) {
            "en" -> when (validity) {
                "Vé 1 ngày" -> "24 hours from activation"
                "Vé 3 ngày" -> "72 hours from activation"
                "Vé tuần" -> "7 days from activation"
                "Vé tháng" -> "30 days from activation"
                "Vé đơn" -> "Single use only"
                else -> "As per regulations"
            }
            else -> when (validity) {
                "Vé 1 ngày" -> "24h kể từ thời điểm kích hoạt"
                "Vé 3 ngày" -> "72h kể từ thời điểm kích hoạt"
                "Vé tuần" -> "7 ngày kể từ thời điểm kích hoạt"
                "Vé tháng" -> "30 ngày kể từ thời điểm kích hoạt"
                "Vé đơn" -> "Sử dụng một lần"
                else -> "Theo quy định"
            }
        }
    }

    fun getLocalizedNote(ticketName: String, language: String): String {
        return when (language) {
            "en" -> when (ticketName) {
                "Vé 1 ngày", "Vé 3 ngày", "Vé tuần", "Vé tháng" -> "Auto-activates 30 days after purchase."
                "Vé sinh viên" -> "Auto-activates 30 days after purchase. Valid student ID required."
                else -> "Please check details at ticket counter."
            }
            else -> when (ticketName) {
                "Vé 1 ngày", "Vé 3 ngày", "Vé tuần", "Vé tháng" -> "Tự động kích hoạt sau 30 ngày kể từ ngày mua."
                "Vé sinh viên" -> "Tự động kích hoạt sau 30 ngày. Chỉ dành cho HSSV có thẻ hợp lệ."
                else -> "Vui lòng xem chi tiết tại quầy vé."
            }
        }
    }

    fun getLocalizedDescription(ticketName: String, language: String): String {
        return when (language) {
            "en" -> when (ticketName) {
                "Vé 1 ngày" -> "Unlimited access to all Metro lines for 24 hours."
                "Vé 3 ngày" -> "Unlimited access to all Metro lines for 3 days."
                "Vé tuần" -> "Unlimited access to all Metro lines for 7 days."
                "Vé tháng" -> "Unlimited access to all Metro lines for 1 month."
                "Vé sinh viên" -> "Discounted monthly pass for students with valid ID."
                else -> "Detailed ticket information."
            }
            else -> when (ticketName) {
                "Vé 1 ngày" -> "Vé cho phép sử dụng tất cả các tuyến Metro trong 24 giờ."
                "Vé 3 ngày" -> "Vé cho phép sử dụng tất cả các tuyến Metro trong 3 ngày."
                "Vé tuần" -> "Sử dụng không giới hạn tất cả các tuyến Metro trong 7 ngày."
                "Vé tháng" -> "Sử dụng không giới hạn tất cả các tuyến Metro trong 1 tháng."
                "Vé sinh viên" -> "Vé ưu đãi cho học sinh, sinh viên sử dụng trong 1 tháng."
                else -> "Thông tin chi tiết về vé."
            }
        }
    }
}
