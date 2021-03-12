package com.prajnadeep.notes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.prajnadeep.notes.R
import com.prajnadeep.notes.database.DBHandler
import com.prajnadeep.notes.database.models.User
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.android.synthetic.main.fragment_add_note.view.*

class AddNoteFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_add_note, container, false)
        val dashBoardFragement = DashBoardFragement()

        // Save note button click listener
        view.saveNotebutton.setOnClickListener {
            val title = modifyNotesTitleEditText.text.toString()
            var desc = modifyNotesDescEditText.text.toString()

            if (title.isNotEmpty()){
                if (desc.isEmpty()){ desc =" " }
                val acct = GoogleSignIn.getLastSignedInAccount(context)
                if (acct != null) {
                    val db = activity?.let { DBHandler(it) }
                    //insert into db
                    val user = User(acct.id.toString(),acct.photoUrl.toString(),title,desc)
                    db!!.insertUserData(user)

                    //pop back stack and continue to dashboard fragment
                    val fm = fragmentManager?.popBackStack()
                    val fragmentTransaction = fragmentManager?.beginTransaction()
                    fragmentTransaction?.replace(R.id.dashboardFragment, dashBoardFragement)
                        ?.disallowAddToBackStack()
                        ?.commit()

                }else {
                    Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show()
                }
            } else{
                modifyNotesTitleEditText.error = "Title cannot be empty"
                modifyNotesTitleEditText.requestFocus()
            }
        }

        return view
    }
}