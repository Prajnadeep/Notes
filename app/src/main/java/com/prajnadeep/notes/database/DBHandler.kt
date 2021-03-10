package com.prajnadeep.notes.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.prajnadeep.notes.database.models.User


const val DATABASE_NAME = "NotesDB"

//User Table
const val TABLE_NAME_USERS = "Users"
const val COL_USER_ID = "User_ID"
const val COL_USER_ACCOUNT_ID = "User_Account_ID"
const val COL_USER_IMG_URL = "User_img_url"
const val COL_USER_NOTE_TITLE = "User_noteTitle"
const val COL_USER_NOTE_DESC = "User_noteDesc"


class DBHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,null,1) {

    override fun onCreate(db: SQLiteDatabase?) {

        val createUserTable = "CREATE TABLE " + TABLE_NAME_USERS +" (" +
                COL_USER_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_USER_ACCOUNT_ID + " VARCHAR(256)," +
                COL_USER_IMG_URL + " VARCHAR(256)," +
                COL_USER_NOTE_TITLE + " VARCHAR(256)," +
                COL_USER_NOTE_DESC + " VARCHAR(256))"
                //COL_USER_EMAIL + " VARCHAR(256))"

                db?.execSQL(createUserTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun insertUserData(user : User){
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_USER_ACCOUNT_ID,user.accountId)
        cv.put(COL_USER_IMG_URL,user.photoUrl)
        cv.put(COL_USER_NOTE_TITLE,user.noteTitle)
        cv.put(COL_USER_NOTE_DESC,user.noteDesc)
        var result = db.insert(TABLE_NAME_USERS,null,cv)
    }

    fun readUserData() : MutableList<User>{
        var list : MutableList<User> = ArrayList()

        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME_USERS
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do {
                val user = User()
                user.id = result.getString(result.getColumnIndex(COL_USER_ID)).toInt()
                user.accountId = result.getString(result.getColumnIndex(COL_USER_ACCOUNT_ID)).toString()
                user.photoUrl = result.getString(result.getColumnIndex(COL_USER_IMG_URL)).toString()
                user.noteTitle = result.getString(result.getColumnIndex(COL_USER_NOTE_TITLE)).toString()
                user.noteDesc = result.getString(result.getColumnIndex(COL_USER_NOTE_DESC)).toString()
                list.add(user)
            }while (result.moveToNext())
        }

        result.close()
        db.close()
        return list
    }

    fun getUserNotes(AccountID:String) : MutableList<User>{
        val accID:String = "'$AccountID'"
        var list : MutableList<User> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME_USERS + " WHERE " + COL_USER_ACCOUNT_ID +" = " +accID
        val result = db.rawQuery(query,null)

        if(result.moveToFirst()){
            do {
                val user = User()
                user.id = result.getString(result.getColumnIndex(COL_USER_ID)).toInt()
                user.accountId = result.getString(result.getColumnIndex(COL_USER_ACCOUNT_ID)).toString()
                user.photoUrl = result.getString(result.getColumnIndex(COL_USER_IMG_URL)).toString()
                user.noteTitle = result.getString(result.getColumnIndex(COL_USER_NOTE_TITLE)).toString()
                user.noteDesc = result.getString(result.getColumnIndex(COL_USER_NOTE_DESC)).toString()
                list.add(user)
            }while (result.moveToNext())
        }

        println("List from db = $list")
        result.close()
        db.close()

        return list
    }

}