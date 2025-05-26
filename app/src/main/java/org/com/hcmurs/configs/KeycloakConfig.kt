package org.com.hcmurs.configs

object KeycloakAuthConfig {
    const val AUTH_ENDPOINT = "https://kc.lch.id.vn/realms/shoppe/protocol/openid-connect/auth"
    const val TOKEN_ENDPOINT = "https://kc.lch.id.vn/realms/shoppe/protocol/openid-connect/token"
    const val LOGOUT_ENDPOINT = "https://kc.lch.id.vn/realms/shoppe/protocol/openid-connect/logout"
    const val CLIENT_ID = "react-app"
    const val REDIRECT_URI = "org.com.hcmurs://callback"
    const val SCOPE = "openid profile email"
}
