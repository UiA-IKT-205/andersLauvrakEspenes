package com.example.piano.services
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileOutputStream


class FirebaseService : Service() {

    private val Tag: String = "Firebaseservice"

    private var onSave: ((file: Uri) -> Unit)? = null

    private var auth: FirebaseAuth

    init {
        Log.d(Tag, "Firebaseservice is running...")
        auth = Firebase.auth
        signInAnonymously()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val mutableScore = intent?.getStringExtra("SCORE")
        val fileName = intent?.getStringExtra("FILENAME")
            mutableScore?.let {
                Log.d(Tag, mutableScore)
                    onSave("$fileName-${auth.currentUser?.uid}.txt", mutableScore)
            }



        return START_REDELIVER_INTENT     // Set to restart and and call last intent
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Service destroyed", Toast.LENGTH_LONG).show()
    }

    private fun onSave(fileName: String, content: String) {
        val path = this.getExternalFilesDir(null)
        if (path != null) {
            val file = File(path, fileName)
            FileOutputStream(file, false).bufferedWriter().use { writer ->
                Log.i(Tag, "Filepath: $path.toString()")
                writer.write(content)
            }
            this.onSave?.invoke(file.toUri())
            upload(file.toUri())
        } else {
            Toast.makeText(this, "Couldn't save to file", Toast.LENGTH_LONG).show()
            Log.e(Tag, "Filepath not found")
        }
    }

    private fun upload(file: Uri) {
        val ref =
            FirebaseStorage.getInstance().reference.child("Score/${auth.currentUser?.uid}/${file.lastPathSegment}")
        val uploadTask = ref.putFile(file)

        uploadTask.addOnSuccessListener {
            Log.d(Tag, "Saved file to FireBase: $it")
        }.addOnFailureListener {
            Log.e(Tag, "Error saving file to FireBase", it)
        }
    }

    private fun signInAnonymously() {
        auth.signInAnonymously().addOnSuccessListener {
            Log.d(Tag, "Login successfully ${it.user}")
        }.addOnFailureListener {
            Log.e(Tag, "Login failed", it)
        }
    }

}