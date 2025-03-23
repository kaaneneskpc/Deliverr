package com.kaaneneskpc.deliverr.data.auth

import android.content.Context
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.codewithfk.foodhub.GoogleServerClientID
import com.kaaneneskpc.deliverr.data.models.GoogleAccount
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class GoogleAuthUiProvider {
    suspend fun signIn(
        activityContext: Context,
        credentialManager: CredentialManager
    ): GoogleAccount {
        val creds = credentialManager.getCredential(
            activityContext,
            getCredentialRequest()
        ).credential
        return handleCredentials(creds)
    }

    fun handleCredentials(creds: Credential): GoogleAccount {
        when {
            creds is CustomCredential && creds.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                val googleIdTokenCredential = creds as GoogleIdTokenCredential
                Log.d("GoogleAuthUiProvider", "GoogleIdTokenCredential: $googleIdTokenCredential")
                return GoogleAccount(
                    token = googleIdTokenCredential.idToken,
                    displayName = googleIdTokenCredential.displayName ?: "",
                    profileImageUrl = googleIdTokenCredential.profilePictureUri.toString()
                )
            }

            else -> {
                throw IllegalStateException("Invalid credential type")
            }
        }
    }

    private fun getCredentialRequest(): GetCredentialRequest {
        return GetCredentialRequest.Builder()
            .addCredentialOption(
                GetSignInWithGoogleOption.Builder(
                    GoogleServerClientID
                ).build()
            )
            .build()
    }

}