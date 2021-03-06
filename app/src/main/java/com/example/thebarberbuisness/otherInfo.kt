package com.example.thebarberbuisness

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.internal.Objects
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_other_info.*
import java.text.SimpleDateFormat
import java.util.*


class otherInfo : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)



        var arr:Array<String> = arrayOf("Gents","Ladies","Unisex")
        var ad:ArrayAdapter<String> = ArrayAdapter(this@otherInfo,android.R.layout.simple_spinner_dropdown_item,arr)
        sptp.adapter=ad

        var sp=getSharedPreferences("MySp",Activity.MODE_PRIVATE)
        var unm:String?=sp.getString("unm",null)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Shop")
        var myRef1 = myRef.child("${unm.toString()}")
        var email =""
        var mobile =""
        var password=""
        var uname=""
        var flag=0



        myRef1.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) { // This method is called once with the initial value and again
// whenever data at this location is updated.
                uname = dataSnapshot.child("userName").value.toString()
                email = dataSnapshot.child("email").value.toString()
                mobile = dataSnapshot.child("mobile").value.toString()
                password = dataSnapshot.child("password").value.toString()

                var value=dataSnapshot.child("oname").value.toString()
                Log.d("long",value)
                if(value == " "){
                    flag=1
                }
                else{
                    startActivity(Intent(this@otherInfo,Dashboard::class.java))
                    finish()
                }
                Log.d("long",flag.toString())

            }

            override fun onCancelled(error: DatabaseError) { // Failed to read value
                //Log.w(FragmentActivity.TAG, "Failed to read value.", error.toException())
            }
        })



        btnsave.setOnClickListener {
            var imgurl=intent.getStringExtra("imgurl")

            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("TAG", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result?.token

                    // Log and toast
                    val msg =token
                    var shop= token?.let { it1 ->
                        ShopData(unm.toString(),email,mobile,txtshopnm.text.toString(),txtct.text.toString(),password,sptp.selectedItem.toString(),txtoname.text.toString(),"Close",lbloptime.text.toString(),txtaddress.text.toString(),lblcltime.text.toString(),imgurl,
                            it1
                        )
                    }
                    myRef.child(unm.toString()).setValue(shop).addOnCompleteListener {
                        Toast.makeText(this@otherInfo,"Successfully Save",Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@otherInfo,Login::class.java))
                        finish()
                    }
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                })



        }
        lbloptime.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)


                lbloptime.text = SimpleDateFormat("HH:mm").format(cal.time)

            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()

        }
        lblcltime.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)


                lblcltime.text = SimpleDateFormat("HH:mm").format(cal.time)

            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()

        }
    }
}
