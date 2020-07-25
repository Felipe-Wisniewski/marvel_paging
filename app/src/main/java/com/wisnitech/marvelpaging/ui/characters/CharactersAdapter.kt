package com.wisnitech.marvelpaging.ui.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wisnitech.marvelpaging.databinding.ItemCharacterBinding
import com.wisnitech.marvelpaging.model.CharacterWeb

class CharactersAdapter(private val onItemClick: (character: CharacterWeb) -> Unit) :
    PagedListAdapter<CharacterWeb, CharactersAdapter.VH>(CharacterDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH.from(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val char = getItem(position)
        char?.let { c ->
            holder.bindHolder(c)
            holder.itemView.setOnClickListener { onItemClick(c) }
        }
    }

    class VH private constructor(private val binding: ItemCharacterBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindHolder(char: CharacterWeb) {
            binding.character = char
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): VH {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCharacterBinding.inflate(layoutInflater, parent, false)
                return VH(binding)
            }
        }
    }

    private class CharacterDiff : DiffUtil.ItemCallback<CharacterWeb>() {
        override fun areItemsTheSame(oldItem: CharacterWeb, newItem: CharacterWeb) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CharacterWeb, newItem: CharacterWeb) =
            oldItem == newItem
    }
}