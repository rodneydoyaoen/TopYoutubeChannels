package com.example.topyoutubechannels.handlers

import com.example.topyoutubechannels.models.Channels
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class channelTableHandler {
    var database : FirebaseDatabase
    var channelRef: DatabaseReference

    init{
        database = FirebaseDatabase.getInstance()
        channelRef = database.getReference( "channels")


    }
    fun create (channel: Channels):Boolean{
        val id = channelRef.push().key
        channel.id = id

        channelRef.child(id!!).setValue(channel)

        return true
    }
    fun update(channel: Channels): Boolean{
        channelRef.child(channel.id!!).setValue(channel)
        return true
    }
    fun delete(channel: Channels): Boolean{
        channelRef.child(channel.id!!).removeValue()
        return true
    }
}