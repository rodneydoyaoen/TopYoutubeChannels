package com.example.topyoutubechannels

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.topyoutubechannels.handlers.channelTableHandler
import com.example.topyoutubechannels.models.Channels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    lateinit var titleET: EditText
    lateinit var linkET: EditText
    lateinit var rankingET: EditText
    lateinit var descET: EditText
    lateinit var addToListBtn: Button
    lateinit var channelTableHandler: channelTableHandler
    lateinit var channels: ArrayList<Channels>
    lateinit var channelsListView: ListView
    lateinit var editChannel: Channels

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        titleET = findViewById(R.id.titleET)
        linkET = findViewById(R.id.linkET)
        rankingET = findViewById(R.id.rankingET)
        descET = findViewById(R.id.descET)
        addToListBtn = findViewById(R.id.addToListBtn)
        channelTableHandler = channelTableHandler()

        channels = ArrayList()
        channelsListView = findViewById(R.id.channelsListView)

        addToListBtn.setOnClickListener {
            val channel_name = titleET.text.toString()
            val link = linkET.text.toString()
            val rank = rankingET.text.toString().toInt()
            val desc = descET.text.toString()
            if (addToListBtn.text.toString()=="Add") {
                val channels =
                    Channels(channel = channel_name, link = link, rank = rank, desc = desc)
                if (channelTableHandler.create(channels)) {
                    Toast.makeText(applicationContext, "Channel added", Toast.LENGTH_LONG).show()
                    clearFields()
                }
            }
            else if(addToListBtn.text.toString() == "Update"){
                val channel = Channels(id = editChannel.id, channel = channel_name,link = link, rank = rank, desc = desc)
                if (channelTableHandler.update(channel)){
                    Toast.makeText(applicationContext, "Channel updated", Toast.LENGTH_LONG).show()
                    clearFields()
                }
            }
        }
        registerForContextMenu(channelsListView)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        val inflater = menuInflater
        inflater.inflate(R.menu.channel_options,menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when(item.itemId){
            R.id.edit_channel ->{
                editChannel = channels[info.position]
                titleET.setText(editChannel.channel)
                linkET.setText(editChannel.link)
                rankingET.setText(editChannel.rank.toString())
                descET.setText(editChannel.desc)
                addToListBtn.setText("Update")
                true
            }
            R.id.delete_channel ->{
                if(channelTableHandler.delete(channels[info.position])){
                    Toast.makeText(applicationContext, "Channel deleted.", Toast.LENGTH_LONG)
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
    override fun onStart(){
        super.onStart()

        channelTableHandler.channelRef.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //TODO("Not yet implemented")
            }
            override fun onDataChange(p0: DataSnapshot) {
                //TODO("Not yet implemented")
                channels.clear()
                p0.children.forEach{
                        it ->  val channel = it.getValue(Channels::class.java)
                    channels.add(channel!!)
                }
                channels.sort()
                val adapter = ArrayAdapter<Channels>(applicationContext, android.R.layout.simple_list_item_1, channels)
                channelsListView.adapter = adapter
            }

        })
    }
    fun clearFields(){
        titleET.text.clear()
        linkET.text.clear()
        rankingET.text.clear()
        descET.text.clear()
        addToListBtn.setText("Add")
    }
}