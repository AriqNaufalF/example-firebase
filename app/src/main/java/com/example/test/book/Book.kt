package com.example.test.book

import com.google.firebase.firestore.Exclude

data class Book(
    @get:Exclude val id: String? = "",
    val title: String? = "",
    @get:Exclude val writer: Writer? = Writer(),
    val price: Int? = 0
)
