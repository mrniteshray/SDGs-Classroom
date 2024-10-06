package com.nitesh.app.sdgsclassroom.auth

import android.net.Uri

class User{

    var name: String? = null
    var email: String? = null
    var pass:String? = null
    var uid:String? = null
    constructor()

    constructor(name:String, email: String,pass:String,uid:String){
        this.name = name
        this.email = email
        this.pass = pass
        this.uid = uid
    }
}

