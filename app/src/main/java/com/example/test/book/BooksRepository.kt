package com.example.test.book

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
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
    suspend fun getBooks(userId: String): Flow<List<Book>> {
        return try {
            firestore.collection("books").orderBy("timestamp", Query.Direction.DESCENDING)
                .snapshots().map { snapshot ->
                    snapshot.map {
//                        Get writer data
                        val writerId = it.data["writer"] as String? ?: ""
                        val writer = firestore
                            .collection("users")
                            .document(writerId).get()
                            .await().toObject(Writer::class.java)?.copy(id = writerId)

//                        Get wishlist data
                        val wishlist = firestore.collection("wishlist")
                            .document("${userId}_${it.id}")
                            .get().await().toObject(Wishlist::class.java)

                        it.toObject(Book::class.java)
                            .copy(
                                id = it.id,
                                writer = writer,
                                wishlist = wishlist
                            )
                    }
                }
        } catch (e: FirebaseFirestoreException) {
            Log.e(LOG_TAG, "Fail to get book data", e)
            emptyFlow()
        }
    }

    suspend fun getBook(bookId: String): Book? {
        return try {
            val book = firestore.collection("books").document(bookId)
                .get().await()
                .let {
                    val writerId = it.get("writer") as String? ?: ""
                    val writer = firestore.collection("users")
                        .document(writerId).get().await()
                        .toObject(Writer::class.java)?.copy(id = writerId)
                    val wishlist = firestore.collection("wishlist")
                        .document("${writerId}_${it.id}").get().await()
                        .toObject(Wishlist::class.java)

                    it.toObject(Book::class.java)
                        ?.copy(id = it.id, writer = writer, wishlist = wishlist)
                }
            Log.i(LOG_TAG, "Book $book")
            book
        } catch (e: FirebaseFirestoreException) {
            Log.e(LOG_TAG, "Fail to get book data", e)
            null
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
            Log.e(LOG_TAG, "Fail to add book data", e)
        }
    }

    suspend fun setWishlist(userId: String, bookId: String) {
        try {
            val docId = "${userId}_${bookId}"
            val wishlistRef = firestore.collection("wishlist")
                .document(docId)
            val bookRef = firestore.collection("books").document(bookId)
            if (!wishlistRef.get().await().exists()) {
//                  if user not wishlist the book
                bookRef.update("wishlistCount", FieldValue.increment(1)).await()
                wishlistRef.set(mapOf("id" to docId)).await()
            } else {
//                if user is wishlist the book
                bookRef.update("wishlistCount", FieldValue.increment(-1)).await()
                wishlistRef.delete().await()

            }
        } catch (e: FirebaseFirestoreException) {
            Log.e(LOG_TAG, "Fail to set wishlist", e)
        }
    }
}