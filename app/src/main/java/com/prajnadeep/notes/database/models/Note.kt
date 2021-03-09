package com.prajnadeep.notes.database.models

class Note {
    var noteId:Int = 0
    var noteAccountId:String = ""
    var noteDescription:String = ""

    constructor(noteID:Int , noteAccountID: String, desc:String){
        this.noteId = noteID
        this.noteAccountId = noteAccountID
        this.noteDescription = desc
    }
}