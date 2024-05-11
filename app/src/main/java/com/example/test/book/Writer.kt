package com.example.test.book

import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class Writer(@get:Exclude val id: String? = "", val name: String? = "") : Serializable
