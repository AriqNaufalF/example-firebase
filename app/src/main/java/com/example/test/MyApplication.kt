package com.example.test

import android.app.Application
import com.example.test.book.BooksRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyApplication : Application() {
    val booksRepository: BooksRepository
        get() = BooksRepository(Firebase.firestore)
}