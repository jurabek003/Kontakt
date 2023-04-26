package com.jurabek888.kontakt2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jurabek888.kontakt2.databinding.ItemContactBinding
import com.jurabek888.kontakt2.models.User

class MyRvAdabter(val list: List<User>,val call: Call,val sms: Sms): RecyclerView.Adapter<MyRvAdabter.Vh>() {

    inner class Vh(val itemContactBinding: ItemContactBinding): RecyclerView.ViewHolder(itemContactBinding.root){

        fun onBind(user: User,position: Int){
            itemContactBinding.textContact.text=user.name
            itemContactBinding.textNumber.text=user.number
            itemContactBinding.imageCall.setOnClickListener {
                call.calling(user, position)
            }
            itemContactBinding.imageSms.setOnClickListener {
                sms.smsSend(user,position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemContactBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int =list.size

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position],position)
    }

}
interface Call{
    fun calling(user: User,position: Int)
}
interface Sms{
    fun smsSend(user: User,position: Int)
}