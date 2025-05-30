package org.com.hcmurs.model

data class SocialLink(
    val type: String,
    val url: String,
    val iconUrl: String
)

data class UserProfile(
    val name: String,
    val avatarUrl: String,
    val githubAccount: String,
    val role: String,
    val socialLinks: List<SocialLink>
)