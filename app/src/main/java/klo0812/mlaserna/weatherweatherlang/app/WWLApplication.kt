package klo0812.mlaserna.weatherweatherlang.app

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class WWLApplication : Application() {

    companion object {
        const val TAG = "WWLApplication"
    }

    private lateinit var firebaseAuth: FirebaseAuth
    var currentUser: FirebaseUser? = null
        private set

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.addAuthStateListener { firebaseAuth ->
            currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                Log.d(TAG, "New user has signed in.")
            } else {
                Log.d(TAG, "User has signed out.")
            }
        }
    }

}