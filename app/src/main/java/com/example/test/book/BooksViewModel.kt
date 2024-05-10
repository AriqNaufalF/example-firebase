package com.example.test.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.test.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BooksViewModel(private val booksRepository: BooksRepository) : ViewModel() {
    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>>
        get() = _books

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val booksRepository = (this[APPLICATION_KEY] as MyApplication).booksRepository
                BooksViewModel(booksRepository)
            }
        }
    }

    fun loadData() = viewModelScope.launch {
        withContext(Dispatchers.Main) {
            booksRepository.getBooks().collect {
                _books.value = it
            }
        }
    }

    fun uploadBook(title: String, price: Int) = viewModelScope.launch {
        withContext(Dispatchers.Main) {
//            Just for simplification, writerId set to static value
            booksRepository.addBook(title, price, "8cU0LiSYHHwxqJNCfVy6")
        }
    }
}