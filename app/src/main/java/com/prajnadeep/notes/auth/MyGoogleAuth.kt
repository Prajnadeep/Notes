package com.prajnadeep.notes.auth

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class MyGoogleAuth {

    private lateinit var gso:GoogleSignInOptions
    private lateinit var mGoogleSignInClient:GoogleSignInClient


    fun initGoogleSignInOption(){
        this.gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
    }

    fun initGoogleSignInClient(context: Context){
        this.mGoogleSignInClient = GoogleSignIn.getClient(context,gso)
    }

    fun isSignedIn(context: Context): Boolean {
        return GoogleSignIn.getLastSignedInAccount(context) != null
    }

    fun getAccountID(context: Context): String {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        return account?.id.toString()
    }
}