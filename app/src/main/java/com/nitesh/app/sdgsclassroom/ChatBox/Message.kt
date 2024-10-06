package com.nitesh.app.sdgsclassroom.ChatBox

class Message {
    var senderName : String?= null
    var message : String? = null
    var senderid : String? = null

    constructor()

    constructor(message: String,senderid : String,senderName : String){
        this.message = message
        this.senderid = senderid
        this.senderName = senderName
    }
}
