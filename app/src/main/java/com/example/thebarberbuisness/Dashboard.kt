package com.example.thebarberbuisness

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_dashboard.*


class Dashboard : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("Shop")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        var sp = getSharedPreferences("MySp",Activity.MODE_PRIVATE)
        var unm = sp.getString("unm","null")


        swstatus.setOnCheckedChangeListener { buttonView, isChecked ->
            var msg = if(isChecked) "Open" else "Close"



           myRef.child(unm.toString()).child("status").setValue(msg).addOnCompleteListener {
               Toast.makeText(this@Dashboard,"Your Shop  is "+msg,Toast.LENGTH_LONG).show()
           }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mymenu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menuaddcat->{
                startActivity(Intent(this@Dashboard,AddCategory::class.java))
                finish()
                return true
            }
            R.id.menuviewcat->{
                return true
            }
            R.id.menulg->{
                startActivity(Intent(this@Dashboard,Login::class.java))
                finish()
                return true
            }
            else->super.onOptionsItemSelected(item)
        }

    }
}