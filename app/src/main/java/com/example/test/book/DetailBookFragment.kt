package com.example.test.book

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.example.test.R
import com.example.test.databinding.BookDetailFragmentBinding

private const val LOG_TAG = "DetailBookFragment"
class DetailBookFragment : Fragment() {
    private lateinit var binding: BookDetailFragmentBinding
//    Get navGraph scoped viewModel
    private val viewModel: BooksViewModel by navGraphViewModels(R.id.navigation)
    private var book: Book = Book()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BookDetailFragmentBinding.inflate(layoutInflater, container, false)
//        Get data from navigation args
        book = arguments?.getSerializable("book") as Book
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Get data from shared viewModel
        Log.i(LOG_TAG, viewModel.book.toString())
        with(binding) {
            titleTV.text = book.title
            writerTv.text = book.writer?.name
            priceTv.text = book.price.toString()
        }
    }
}