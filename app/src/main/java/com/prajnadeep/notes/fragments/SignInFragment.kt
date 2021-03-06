package com.prajnadeep.notes.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.prajnadeep.notes.R
import com.prajnadeep.notes.database.DBHandler
import com.prajnadeep.notes.database.models.User


class SignInFragment : Fragment() {
    private lateinit var signInButton: SignInButton
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var RC_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_sign_in, container, false)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
            .build()

        mGoogleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }!!
        val account = GoogleSignIn.getLastSignedInAccount(context)

        //Google SignIn Button
        signInButton = view.findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_WIDE)
        signInButton.setOnClickListener(){ signIn() }

        return view
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        super.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

            val bundle: Bundle? = data?.extras
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            val acct = GoogleSignIn.getLastSignedInAccount(context)
            if (acct != null) {
                insertIntoDB(acct)
            }

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            Log.w("ERROR SIGNIN", "signInResult:failed code=" + e.statusCode)
            Toast.makeText(context,"${e.message}",Toast.LENGTH_LONG).show()

        }
    }

    private fun insertIntoDB(acct: GoogleSignInAccount) {

        val db = activity?.let { DBHandler(it) }

        //insert user data into db
        val user = User(acct.id.toString(),acct.photoUrl.toString(),"temp","temp")
        db!!.insertUserData(user)

        //Goto Dashboard Fragment
        val fragmentManager: FragmentManager? = fragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.signIN_Fragment, DashBoardFragement())
        fragmentTransaction.disallowAddToBackStack()
        fragmentTransaction.commit()
    }

}