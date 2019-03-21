package com.khaled.omdbmoves.data.database.model

import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required

@RealmClass
open class LocalMovie(
    @PrimaryKey
    private var id: Long = 0,
    var title: String = "",
    var releaseDate: String = ""
//    val watchLater: Boolean = false
) : RealmModel