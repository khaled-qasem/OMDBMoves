package com.khaled.omdbmoves.data.database.model

import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Movie(
    @PrimaryKey
    var id: Long = 0,
    var title: String = "",
    var adult: Boolean = false,
    var backdropPath: String? = "",
    var originalLanguage: String? = "",
    var originalTitle: String? = "",
    var overview: String? = "",
    var popularity: Double? = 0.0,
    var posterPath: String? = "",
    var releaseDate: String? = "",
    var video: Boolean? = false,
    var voteAverage: Int? = 0,
    var voteCount: Int? = 0
) : RealmModel