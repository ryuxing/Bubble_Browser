package com.ryuxing.bubblebrowser.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarkViewModel(private val repository :BookmarkRepository) :ViewModel(){
    val bookmarks:LiveData<List<Bookmark>> = repository.getAllVM.asLiveData()
    fun add(bookmark:Bookmark){
        viewModelScope.launch(Dispatchers.IO){
            repository.add(bookmark)
        }
    }

    fun update(bookmark: Bookmark) {
        viewModelScope.launch(Dispatchers.IO){
            repository.update(bookmark)
        }

    }

    fun delete(bookmark: Bookmark) {
        viewModelScope.launch(Dispatchers.IO){
            repository.delete(bookmark)
        }
    }
    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }
}