package com.tuhoc.dreamtunes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tuhoc.dreamtunes.data.pojo.Type
import com.tuhoc.dreamtunes.databinding.TypeCardBinding

class TypeAdapter: RecyclerView.Adapter<TypeAdapter.TypeViewHolder>() {
    private var typeList = mutableListOf<Type>()

    private lateinit var onItemClick: OnItemClick

    fun onItemClicked(onItemClick: OnItemClick) {
        this.onItemClick = onItemClick
    }

    fun setTypeList(typeList: List<Type>) {
        this.typeList = typeList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        val binding = TypeCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TypeAdapter.TypeViewHolder, position: Int) {
        val currentItem = typeList[position]
        holder.binding.apply {
            Glide.with(holder.itemView)
                .load(currentItem.image)
                .into(imgType)

            tvType.text = currentItem.typeName
        }

        holder.itemView.setOnClickListener {
            onItemClick.onClickListener(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return typeList.size
    }

    class TypeViewHolder(val binding: TypeCardBinding): RecyclerView.ViewHolder(binding.root)

    interface OnItemClick {
        fun onClickListener(type: Type)
    }
}