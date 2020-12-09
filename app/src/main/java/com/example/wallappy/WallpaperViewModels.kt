package com.example.wallappy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot

class WallpaperViewModels:ViewModel(){
  private val firebaseRepository:FirebaseRepository= FirebaseRepository()
    private  val wallpapersList:MutableLiveData<List<WallpaperModels>>by lazy {

        MutableLiveData<List<WallpaperModels>>().also{
            loadwallpapersData()
        }
    }

    fun getWallpapersList() : LiveData<List<WallpaperModels>>{
        return wallpapersList
    }

    fun loadwallpapersData(){
    //query data for repo
    firebaseRepository.queryWallpapers().addOnCompleteListener{
        if (it.isSuccessful){

            val result=it.result
            if (result!!.isEmpty){




            }else {
                //result are ready to  load
                         if (wallpapersList.value==null){

                    //load first page
                    wallpapersList.value = result.toObjects(WallpaperModels::class.java)

                         }else{
                             //load next page

                              wallpapersList.value = wallpapersList.value!!.plus(result.toObjects(WallpaperModels::class.java))



                         }




                //get the last doc

                val lastItem:DocumentSnapshot=result.documents[result.size()-1]
                firebaseRepository.lastVisible=lastItem
            } }
        else
        {
           Log.d("View_Model_Log","Error: ${it.exception!!.message}   ")
        }

    }
    }
}