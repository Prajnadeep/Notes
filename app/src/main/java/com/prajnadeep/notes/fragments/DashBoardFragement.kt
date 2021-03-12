package com.prajnadeep.notes.fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.prajnadeep.notes.R
import com.prajnadeep.notes.adapter.NotesAdapter
import com.prajnadeep.notes.auth.MyGoogleAuth
import com.prajnadeep.notes.database.DBHandler
import kotlinx.android.synthetic.main.edit_note_popup.view.*
import kotlinx.android.synthetic.main.fragment_dash_board_fragement.*
import kotlinx.android.synthetic.main.fragment_dash_board_fragement.view.*


class DashBoardFragement : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Google Auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient = context?.let { it1 -> GoogleSignIn.getClient(it1, gso) }

        //Fragment Manager
        val view =  inflater.inflate(R.layout.fragment_dash_board_fragement, container, false)
        val addNoteFragment: Fragment = AddNoteFragment()
        val signInFragment : Fragment = SignInFragment()
        val dashBoardFragement: Fragment = DashBoardFragement()
        val fragmentManager = activity!!.supportFragmentManager


        //Get Google AccountID
        val accountID: String? = context?.let { MyGoogleAuth().getAccountID(it) }
        println("Account ID = $accountID")

        //Get data from SQLite
        var db = activity?.let { DBHandler(it) }
        var data = accountID?.let { db!!.getUserNotes(it) }

        //Set RecyclerView Adapter
        var adapter = data?.let { NotesAdapter(it) }
        view.dashboardRecyclerView.adapter = adapter


        //Adapter on item-click
        adapter?.onItemClick = { it ->
            val user = it
            val  mDailog = LayoutInflater.from(context).inflate(R.layout.edit_note_popup, null)
            val passwordResetDialog = context?.let { it1 -> AlertDialog.Builder(it1).setView(mDailog) }
            var title : EditText = mDailog.findViewById(R.id.popupTitle)
            var desc : EditText= mDailog.findViewById(R.id.popupDesc)
            val  mAlertDialog = passwordResetDialog?.show()

            title.setText(user.noteTitle)
            desc.setText(user.noteDesc)


            //Modify Data button click listener
            mDailog.modifyNotebutton.setOnClickListener{
                var newTitle :String = title.text.toString()
                var newDesc : String = desc.text.toString()

                if (newTitle.isEmpty()) {
                    title.error = "Title cannot be empty"
                    title.requestFocus()
                }
                else {
                    //Update data function
                    db?.updateData(user.id,newTitle,newDesc)

                    //Refresh Data
                    data?.clear()
                    data = accountID?.let { db!!.getUserNotes(it) }
                    adapter = data?.let { it1 -> NotesAdapter(it1) }
                    adapter?.notifyDataSetChanged()

                    //Refresh Fragment with updated data
                    fragmentManager.beginTransaction()
                            .replace(R.id.dashboardFragment, dashBoardFragement)
                            .disallowAddToBackStack()
                            .commit()

                    mAlertDialog?.dismiss()
                }

            }

            //Delete Data button click listener
            mDailog.deleteNotebutton.setOnClickListener {
                //delete note function
                db?.deleteOneNote(user.noteTitle)

                //Refresh Data
                data?.clear()
                data = accountID?.let { db!!.getUserNotes(it) }
                adapter = data?.let { it1 -> NotesAdapter(it1) }
                adapter?.notifyDataSetChanged()

                //Refresh Fragment with updated data
                fragmentManager.beginTransaction()
                    .replace(R.id.dashboardFragment, dashBoardFragement)
                    .disallowAddToBackStack()
                    .commit()

                mAlertDialog?.dismiss()
            }

        }

        //Set RecyclerView Layout manager
        view.dashboardRecyclerView.layoutManager = LinearLayoutManager(context)

        //Add notes Fragment Button click listener
        view.dashboardAddButton.setOnClickListener {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction
                .replace(R.id.dashboardFragment, addNoteFragment)
                .addToBackStack(null)
                .commit()
        }

        //Sign OUT Button click listener
        view.dashboardLogoutButton.setOnClickListener {
            val fragmentTransaction = fragmentManager.beginTransaction()
            mGoogleSignInClient?.signOut()?.addOnCompleteListener {
                Toast.makeText(context, "Successfully signed out!", Toast.LENGTH_SHORT).show()
                fragmentTransaction
                    .replace(R.id.dashboardFragment, signInFragment)
                    .disallowAddToBackStack()
                    .commit()
            }
        }

        return view
    }
}