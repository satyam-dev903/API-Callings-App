package com.satyam15.apicallings.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.satyam15.apicallings.data.User
import com.satyam15.apicallings.databinding.ItemUserBinding

class UserAdapter(
    private val onDeleteClick:(User)->Unit,
    private val onEditClick:(User)->Unit
)
    : ListAdapter<User,UserAdapter.UserViewHolder>(DIFF_CALLBACK)
{
    class UserViewHolder(val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        val binding= ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: UserViewHolder,
        position: Int
    ) {
        val currUser=getItem(position)
        holder.binding.tvName.text=currUser.name
        holder.binding.tvEmail.text = currUser.email
        holder.binding.btnDel.setOnClickListener {
            onDeleteClick(currUser)
        }
        holder.binding.btnEdit.setOnClickListener {
            onEditClick(currUser)
        }
    }
    companion object{
        private val DIFF_CALLBACK=object: DiffUtil.ItemCallback<User>(){
            override fun areItemsTheSame(
                oldItem: User,
                newItem: User
            ): Boolean {
                return oldItem.id==newItem.id
            }

            override fun areContentsTheSame(
                oldItem: User,
                newItem: User
            ): Boolean {
                return oldItem==newItem

            }
        }
    }
}