package com.khaled.omdbmoves.data.database.model

import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Movie(
    @PrimaryKey
    var id: Long = 0,
    var title: String = "",
    var releaseDate: String? = "",
    var posterPath: String? = ""
) : RealmModel