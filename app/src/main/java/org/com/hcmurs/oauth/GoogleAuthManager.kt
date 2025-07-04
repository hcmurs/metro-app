package org.com.hcmurs.oauth

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleAuthManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        // Replace with your web client ID from Google Cloud Console
        private const val WEB_CLIENT_ID = "642093341387-4ts23qfc08ukhfkifq7k2d2a2nr928t5.apps.googleusercontent.com"
       // private const val WEB_CLIENT_ID = "634675406739-vsrau9upvljnlemslcrubdad6jrngfli.apps.googleusercontent.com"

    }

    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(WEB_CLIENT_ID)
            .requestEmail()
            .build()

        GoogleSignIn.getClient(context, gso)
    }

    // Create an IntentSender for sign-in
    suspend fun signIn(): Result<Intent> = withContext(Dispatchers.IO) {
        try {
            val intent = googleSignInClient.signInIntent
            Result.success(intent)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    // Extract the ID token from sign-in result
    suspend fun getGoogleIdToken(data: Intent?): Result<String> = withContext(Dispatchers.IO) {
        try {
            if (data == null) {
                Log.e("GoogleSignIn", "Sign-in intent result was null")
                return@withContext Result.failure(Exception("Sign-in intent result was null"))
            }
            Log.d("GoogleSignIn", "Processing sign-in result")

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.await()
            Log.d("GoogleSignIn", "Got account: ${account.email}")

            val idToken = account.idToken
            Log.d("GoogleSignIn", "ID token is ${if (idToken == null) "NULL" else "present (length: ${idToken.length})"}")

            if (idToken != null) {
                Result.success(idToken)
            } else {
                Result.failure(Exception("No ID token found in the sign-in result"))
            }
        } catch (e: ApiException) {
            Result.failure(Exception("Google Sign-In failed with code ${e.statusCode}: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Sign out from Google
    suspend fun signOut() = withContext(Dispatchers.IO) {
        try {
            googleSignInClient.signOut().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get sign-in intent directly (for use with ActivityResultLauncher)
    fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }
}