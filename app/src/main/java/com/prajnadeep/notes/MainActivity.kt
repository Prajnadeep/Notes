package com.prajnadeep.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.prajnadeep.notes.fragments.SignInFragment

class MainActivity : AppCompatActivity() {

    private val fragmentManager = supportFragmentManager

    private val signInFragment: Fragment = SignInFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (isSignedIn()){
            Toast.makeText(this, "ALREADY SIGNED IN", Toast.LENGTH_SHORT).show()
            val acct = GoogleSignIn.getLastSignedInAccount(applicationContext)
            if (acct != null) {
                Toast.makeText(this, "${acct.displayName}", Toast.LENGTH_SHORT).show()
            }
        }


        supportFragmentManager.beginTransaction().replace(R.id.mainActivity, signInFragment).commit()
    }

    private fun isSignedIn() : Boolean {
        return GoogleSignIn.getLastSignedInAccount(applicationContext)!=null
    }
}