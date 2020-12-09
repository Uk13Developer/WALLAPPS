package com.example.wallappy

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), (WallpaperModels) -> Unit {

    private val firebaseRepository=FirebaseRepository()
    private  var navController: NavController?=null
    private var wallpaperList: List<WallpaperModels> = ArrayList()
    private val wallpaperListAdapter:WallpaperListAdapter= WallpaperListAdapter(wallpaperList,this)
    private var isLoading: Boolean=true
    private val wallpaperViewModels:WallpaperViewModels by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(main_toolbar)

  val actionBar=(activity as AppCompatActivity).supportActionBar
        actionBar!!.title = "SHINWALLY"

        //Initialize the new controller
        navController=Navigation.findNavController(view)
        //check user login
        if (firebaseRepository.getUser()==null)
        {
            //user not logged in go register
            navController!!.navigate(R.id.action_homeFragment_to_registerFragment)


        }
        wallpapers_list_view.setHasFixedSize(true)
        wallpapers_list_view.layoutManager=GridLayoutManager(context,3)
        wallpapers_list_view.adapter=wallpaperListAdapter
        //reached bottom
        wallpapers_list_view.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                 if (!recyclerView.canScrollVertically(1)&& newState == RecyclerView.SCROLL_STATE_IDLE)
                 {
                     //Reached at bottom  and  not scrolling anymore
                     if (!isLoading){
                    //load next page
                    wallpaperViewModels.loadwallpapersData()
                    isLoading=true
                    Log.d("Home fragment","Reached at last")
                     }
                 }
            }})
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        wallpaperViewModels.getWallpapersList().observe(viewLifecycleOwner,Observer {
            wallpaperList=it
            wallpaperListAdapter.wallpaperList=wallpaperList
            wallpaperListAdapter.notifyDataSetChanged()
            //Loading complete
            isLoading=false
        }) }

    override fun invoke(wallpaper: WallpaperModels) {
        //clicked on wallpaper to navigate to detail
        val action=HomeFragmentDirections.actionHomeFragmentToDetailFragment(wallpaper.image)
        navController!!.navigate(action)
    } }