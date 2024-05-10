package com.example.test.book

import com.google.firebase.firestore.Exclude

data class Writer(@get:Exclude val id: String? = "", val name: String? = "")
