package com.jurabek888.kontakt2

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.github.florent37.runtimepermission.RuntimePermission.askPermission
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.jurabek888.kontakt2.adapter.Call
import com.jurabek888.kontakt2.adapter.MyRvAdabter
import com.jurabek888.kontakt2.adapter.Sms
import com.jurabek888.kontakt2.databinding.ActivityMainBinding
import com.jurabek888.kontakt2.models.User

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    @SuppressLint("MissingInflatedId")
    private lateinit var myRvAdabter: MyRvAdabter
    private lateinit var list:ArrayList<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var text = findViewById<TextView>(R.id.text_contact)



         askPermission(Manifest.permission.READ_CONTACTS){
                     //all permissions already granted or just granted
            readContact()
             myRvAdabter = MyRvAdabter(list,object :Call{
                 override fun calling(user: User, position: Int) {
                     telefonQilish(position)
                 }

             },object :Sms{
                 override fun smsSend(user: User, position: Int) {
                     val intent=Intent(this@MainActivity,SmsActivity::class.java)
                     intent.putExtra("key",list[position].number)
                     intent.putExtra("key2",list[position].name)
                     startActivity(intent)
                 }

             })
             binding.rv.adapter=myRvAdabter

                 }.onDeclined { e ->
                     if (e.hasDenied()) {

                         AlertDialog.Builder(this)
                             .setMessage("Please accept our permissions")
                             .setPositiveButton("yes") { dialog, which ->
                                 e.askAgain();
                             } //ask again
                             .setNegativeButton("no") { dialog, which ->
                                 dialog.dismiss();
                             }

                             .show();
                     }

                     if(e.hasForeverDenied()) {
                         //the list of forever denied permissions, user has check 'never ask again'

                         // you need to open setting manually if you really need it
                         e.goToSettings();
                     }
             }
    }

    @SuppressLint("Range")
    private fun readContact():ArrayList<User>{
        list=ArrayList()

        val contacts=contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null)
        while (contacts!!.moveToNext()){
            val user=User(
                contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            )
            list.add(user)
        }
        return list
    }



    private fun telefonQilish(position:Int) {

        askPermission(Manifest.permission.CALL_PHONE){
                    //all permissions already granted or just granted

                    val phonNumber = list[position].number
                    val intent = Intent(Intent(Intent.ACTION_CALL))
                    intent.data = Uri.parse("tel:$phonNumber")
                    startActivity(intent)
                }.onDeclined { e ->
                    if (e.hasDenied()) {

                        AlertDialog.Builder(this)
                            .setMessage("Ruxsat bermasangiz ilova ishlay olmaydi ruxsat bering...")
                            .setPositiveButton("yes") { dialog, which ->
                                e.askAgain();
                            } //ask again
                            .setNegativeButton("no") { dialog, which ->
                                dialog.dismiss();
                            }
                            .show();
                    }

                    if(e.hasForeverDenied()) {
                        //the list of forever denied permissions, user has check 'never ask again'

                        // you need to open setting manually if you really need it
                        e.goToSettings();
                    }
                }
    }



}