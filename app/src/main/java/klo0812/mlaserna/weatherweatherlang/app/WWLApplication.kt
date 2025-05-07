package klo0812.mlaserna.weatherweatherlang.app

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import klo0812.mlaserna.weatherweatherlang.database.AppDataBase

class WWLApplication : Application() {

    companion object {
        val TAG: String? = WWLApplication::class.simpleName

        @Volatile private var firebaseAuth: FirebaseAuth? = null

        fun getFirebaseAuth (): FirebaseAuth {
            return firebaseAuth ?: synchronized(this) {
                firebaseAuth ?: FirebaseAuth.getInstance().also { firebaseAuth = it }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        AppDataBase.getInstance(this)
        firebaseAuth = getFirebaseAuth()
        firebaseAuth!!.addAuthStateListener { firebaseAuth ->
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                Log.d(TAG, "New user has signed in.")
            } else {
                Log.d(TAG, "User has signed out.")
            }
        }
    }

}