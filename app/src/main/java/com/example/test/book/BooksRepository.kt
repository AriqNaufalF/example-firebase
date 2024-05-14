package com.example.test.book

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

private const val LOG_TAG = "BooksRepository"

class BooksRepository(private val firestore: FirebaseFirestore) {
    suspend fun getBooks(): Flow<List<Book>> {
        return try {
            firestore.collection("books").orderBy("timestamp", Query.Direction.DESCENDING)
                .snapshots().map { snapshot ->
                    snapshot.map {
                        val writerId = it.data["writer"] as String? ?: ""
                        val writer = firestore
                            .collection("users")
                            .document(writerId).get()
                            .await().toObject(Writer::class.java)?.copy(id = writerId)

                        it.toObject(Book::class.java).copy(id = it.id, writer = writer)
                    }
                }
        } catch (e: FirebaseFirestoreException) {
            Log.e(LOG_TAG, "Fail to get book data", e)
            emptyFlow()
        }
    }

    suspend fun addBook(title: String, price: Int, writerId: String) {
        try {
            val book = mapOf(
                "title" to title,
                "price" to price,
                "writer" to writerId,
                "timestamp" to Timestamp.now()
            )
            val newBook = firestore.collection("books").add(book).await()

            Log.i(LOG_TAG, "Book uploaded with ID: ${newBook.id}")
        } catch (e: FirebaseFirestoreException) {
            Log.e(LOG_TAG, "Fail to get book data", e)
        }
    }
}