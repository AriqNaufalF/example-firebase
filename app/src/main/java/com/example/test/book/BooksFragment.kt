package com.example.test.book

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.test.R
import com.example.test.databinding.BookFragmentBinding
import com.google.android.material.snackbar.Snackbar

private const val LOG_TAG = "BooksFragment"

class BooksFragment : Fragment() {
    private lateinit var binding: BookFragmentBinding
    private lateinit var booksAdapter: BooksAdapter

    //    Init shared viewModel scope in navGraph
    private val viewModel: BooksViewModel by navGraphViewModels(R.id.navigation) { BooksViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        Init layout binding
        binding = BookFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        Init adapter
        booksAdapter = BooksAdapter(onClick = { book, _ ->
//            Send data to another fragment with viewModel
            viewModel.setBook(book)

            findNavController().navigate(R.id.booksFragmentToDetailBookFragment)
        }, onLongClick = { book, _ ->
            Log.i(LOG_TAG, "Book ${book.title} is on hold")
            Snackbar.make(binding.booksRv, "Book ${book.title} hold", Snackbar.LENGTH_LONG)
                .show()
            true
        }, onWishlist = { book, _ ->
//            Set wishlist
            viewModel.setWishlist(book.id ?: "")
            Log.d(LOG_TAG, "Wishlist: ${book.title} - ${book.wishlist}")
        })
        binding.booksRv.adapter = booksAdapter
//        Insert data from viewmodel to adapter
        viewModel.books.observe(viewLifecycleOwner) {
            Log.d(LOG_TAG, "Books: $it")
            booksAdapter.submitList(it)
        }
//        Load data
        viewModel.loadData()

//        Handle submit
        binding.submitButton.setOnClickListener {
            val title = binding.titleField.text.toString().trim()
            val price = binding.priceField.text.toString().trim()

//            Check if form is not empty
            if (title != "" && price != "") {
                Log.i(LOG_TAG, "title $title, price $price")

                viewModel.uploadBook(title, price.toInt())
//                Clear form
                binding.titleField.text?.clear()
                binding.priceField.text?.clear()
            }
        }

    }
}