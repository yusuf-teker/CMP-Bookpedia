package com.plcoding.bookpedia.book.data.database

import androidx.room.RoomDatabaseConstructor

//Room arka planda oluşturuyor implementation
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object BookDatabaseConstructor: RoomDatabaseConstructor<FavoriteBookDatabase> {
    override fun initialize(): FavoriteBookDatabase
}