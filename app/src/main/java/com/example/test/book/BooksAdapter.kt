package com.example.test.book

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.test.databinding.BookListBinding

class BooksAdapter : ListAdapter<Book, BooksAdapter.ViewHolder>(Companion) {
    companion object : DiffUtil.ItemCallback<Book>() {
            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.id == newItem.id
            }
    }

    inner class ViewHolder(private val binding: BookListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) = with(binding) {
            titleTV.text = book.title
            writerTv.text = book.writer?.name
            priceTv.text =  book.price.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BookListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
