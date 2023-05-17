package com.ryuxing.bubblebrowser.bookmark

import kotlinx.coroutines.flow.Flow

class BookmarkRepository(val dao: BookmarkDao) {
    val getAllVM: Flow<List<Bookmark>> = dao.getAllVM()
    suspend fun add(bookmark :Bookmark) = dao.add(bookmark)
    suspend fun update(bookmark: Bookmark) = dao.update(bookmark)
    suspend fun delete(bookmark: Bookmark) = dao.delete(bookmark)
    suspend fun deleteAll() = dao.deleteAll()


}