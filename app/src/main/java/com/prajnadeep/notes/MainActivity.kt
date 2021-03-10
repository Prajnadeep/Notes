package com.prajnadeep.notes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.prajnadeep.notes.fragments.DashBoardFragement
import com.prajnadeep.notes.fragments.SignInFragment

class MainActivity : AppCompatActivity() {


    private val fragmentManager = supportFragmentManager

    private val signInFragment: Fragment = SignInFragment()
    private val dashBoardFragment: Fragment = DashBoardFragement()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (isSignedIn()) {
            supportFragmentManager.beginTransaction().replace(
                R.id.containerMainActivity,
                dashBoardFragment
            ).commit()
            val acct = GoogleSignIn.getLastSignedInAccount(applicationContext)
            if (acct != null) {
                //Toast.makeText(this, "${acct.displayName}", Toast.LENGTH_SHORT).show()
            }
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerMainActivity, signInFragment).commit()
        }

    }

    private fun isSignedIn(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(applicationContext) != null
    }

}