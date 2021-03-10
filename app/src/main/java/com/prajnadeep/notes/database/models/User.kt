package com.prajnadeep.notes.database.models

class User {
    var id:Int = 0
    var accountId:String = ""
    var photoUrl:String = ""
    var noteTitle:String = ""
    var noteDesc:String = ""

    constructor(accountID: String, url :String, title:String, desc:String){
        this.accountId = accountID
        this.photoUrl = url
        this.noteTitle = title
        this.noteDesc = desc
    }

    constructor(){
    }
}