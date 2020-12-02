package com.example.topyoutubechannels.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Channels(var id:String? = "", var channel: String? = "", var link:String? = "", var rank:Int = 0, var desc:String? = ""): Comparable<Channels>{
    override fun toString(): String {
        return "$channel , Link: $link, Rank: $rank , Description: $desc"
    }

    override fun compareTo(other: Channels): Int {
        return if(this.rank != other.rank){
            this.rank - other.rank
        }else{
            0
        }
    }

}