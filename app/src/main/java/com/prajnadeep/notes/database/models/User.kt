package com.prajnadeep.notes.database.models

class User {
    var id:Int = 0
    var accountId:String = ""
    var email:String = ""

    constructor(accountID: String, mail :String){
        this.accountId = accountID
        this.email = mail
    }

    constructor(){

    }
}