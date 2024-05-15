package com.example.test.book

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import com.example.test.R
import com.example.test.databinding.BookDetailFragmentBinding
import kotlinx.coroutines.launch

private const val LOG_TAG = "DetailBookFragment"

class DetailBookFragment : Fragment() {
    private lateinit var binding: BookDetailFragmentBinding

    //    Get navGraph scoped viewModel
    private val viewModel: BooksViewModel by navGraphViewModels(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BookDetailFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Get data from shared viewModel
        viewModel.book.observe(viewLifecycleOwner) {
            Log.d(LOG_TAG, "Observe: $it")

            with(binding) {
                titleTV.text = it.title
                writerTv.text = it.writer?.name
                priceTv.text = it.price.toString()
                wishlistCount.text =
                    requireContext().getString(R.string.wishlist_count, it.wishlistCount)
            }
            setWishlistBtnBg((it.wishlist != null))
        }
        binding.wishlistBtn.setOnClickListener {
            viewModel.book.value.let { book ->
                Log.d(LOG_TAG, "on wishlist: $book")
//                Show loading spinner
                binding.spinner.visibility = View.VISIBLE
                binding.mainContent.visibility = View.GONE
                viewModel.setWishlist(book?.id ?: "").invokeOnCompletion {
//                  Update book data after set wishlist
                    viewModel.getBook(book?.id ?: "")
                    lifecycleScope.launch {
//                    Hide loading spinner
                        binding.spinner.visibility = View.GONE
                        binding.mainContent.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setWishlistBtnBg(isWishlist: Boolean) {
        binding.wishlistBtn.setBackgroundColor(
            requireContext().getColor(
                if (isWishlist) R.color.blue else R.color.black
            )
        )
    }
}