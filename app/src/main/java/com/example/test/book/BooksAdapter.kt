package com.example.test.book

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.databinding.BookListBinding

class BooksAdapter(
    val onClick: (Book, View) -> Unit,
    val onLongClick: (Book, View) -> Boolean,
    val onWishlist: (Book, View) -> Boolean
) : ListAdapter<Book, BooksAdapter.ViewHolder>(Companion) {
    private lateinit var context: Context

    companion object : DiffUtil.ItemCallback<Book>() {
        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.id == newItem.id
        }
    }

    inner class ViewHolder(private val binding: BookListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) = with(binding) {
            titleTV.text = book.title
            writerTv.text = book.writer?.name
            priceTv.text = book.price.toString()
            bookCard.setOnClickListener {
                onClick(book, it)
            }
            wishlistCount.text = context.getString(R.string.wishlist_count, book.wishlistCount)
//            Change wishlist color
            wishlistBtn.setBackgroundColor(
                context.getColor(
                    if (book.wishlist != null) R.color.blue else R.color.black
                )
            )


            bookCard.setOnLongClickListener {
                onLongClick(book, it)
            }
            wishlistBtn.setOnClickListener {
                val isWished = onWishlist(book, it)
//                Change color base on is wishlisted
                wishlistBtn.setBackgroundColor(
                    context.getColor(
                        if (isWished) R.color.blue else R.color.black
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context

        val binding = BookListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
