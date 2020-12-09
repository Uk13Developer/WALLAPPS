package com.example.wallappy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private  var navController:NavController?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//setup nav controll
        navController=Navigation.findNavController(view)

        //check if user already logged
        if (firebaseAuth.currentUser==null)
        {
            //not logged,create new account
              register_text.text ="Creating New Account"
              firebaseAuth.signInAnonymously().addOnCompleteListener{

                  if (it.isSuccessful){

                      //account created
                      register_text.text= "Account Created, Logging in.."
                      navController!!.navigate(R.id.action_registerFragment_to_homeFragment2)
                  }
                  else
                  {
                      //error
                  register_text.text="Error:${it.exception!!.message}"
                  }

              }

        }


       else
        {
               //logged in ,send back to home
            navController!!.navigate(R.id.action_registerFragment_to_homeFragment2)
         }

}}