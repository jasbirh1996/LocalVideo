package com.jasbir.localvideo.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.jasbir.localvideo.data.GalleryVideo_Get_Set
import com.jasbir.localvideo.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
        private val savedStateHandle: SavedStateHandle,
        private val repo:Repository
): ViewModel() {
    val getVideoData: MutableLiveData<List<GalleryVideo_Get_Set>> = MutableLiveData()
    var context: Context? = null

   fun invoke(){
       getVideoData.value = repo.getAllVideoPath(context!!)
   }


}