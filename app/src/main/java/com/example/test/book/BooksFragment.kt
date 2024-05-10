package com.example.test.book

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.test.databinding.BookFragmentBinding

private const val LOG_TAG = "BooksFragment"

class BooksFragment : Fragment() {
    private lateinit var binding: BookFragmentBinding
    private lateinit var booksAdapter: BooksAdapter
    private val viewModel: BooksViewModel by viewModels { BooksViewModel.Factory }

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
        booksAdapter = BooksAdapter()
        with(binding.booksRv) {
            adapter = booksAdapter
            setHasFixedSize(true)
        }
//        Insert data from viewmodel to adapter
        viewModel.books.observe(viewLifecycleOwner) {
            Log.d(LOG_TAG, "Books: $it")
            booksAdapter.submitList(it)
        }
//        Load data
        viewModel.loadData()

//        Sandle submit
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