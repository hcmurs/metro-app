package org.com.hcmurs.model

data class CooperationLink(
    val id: String,
    val title: String,
    val iconRes: Int,
    val tapUrl: String = "https://metro-fe.vercel.app"
)