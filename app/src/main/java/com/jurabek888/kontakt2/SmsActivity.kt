package com.jurabek888.kontakt2

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.github.florent37.runtimepermission.kotlin.coroutines.experimental.askPermission
import com.jurabek888.kontakt2.databinding.ActivitySmsBinding

class SmsActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySmsBinding.inflate(layoutInflater) }
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val contactName=intent.getStringExtra("key")
        val contactNumber=intent.getStringExtra("key2")

        binding.nameContact.text=contactNumber.toString()
        binding.numberContact.text=contactName.toString()

        binding.btnSend.setOnClickListener {
            askPermission(android.Manifest.permission.SEND_SMS){
                try {

                val matn =binding.edtMatn.text.toString()
                val obj=SmsManager.getDefault()

                    obj.sendTextMessage(contactName,null,matn,null,null)

                Toast.makeText(this,"Send Message 📤 " ,Toast.LENGTH_SHORT).show()
                }catch (e:IllegalArgumentException){
                    Toast.makeText(this,"habar yozmadingiz yoki boshqa hatolik yuzberdi 😔",Toast.LENGTH_LONG).show()
                }

            }.onDeclined { e ->
                if (e.hasDenied()) {
                    AlertDialog.Builder(this)
                        .setMessage("Ruxsat bermasangiz ilova ishlay olmaydi ruxsat bering 🤫...")
                        .setPositiveButton("yes 😉") { dialog, which ->
                            e.askAgain();
                        } //ask again
                        .setNegativeButton("no 😡") { dialog, which ->
                            dialog.dismiss();
                        }
                        .show();
                }
                if (e.hasForeverDenied()) {
                    //the list of forever denied permissions, user has check 'never ask again'

                    // you need to open setting manually if you really need it
                    e.goToSettings();
                }
            }

            }


        }

    }