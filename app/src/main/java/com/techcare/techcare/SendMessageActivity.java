package com.techcare.techcare;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SendMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        // onClick
        findViewById(R.id.btnSend).setOnClickListener(v -> {
            // Get the message
            String message = ((EditText) findViewById(R.id.txtMessage)).getText().toString();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("messages").document("message");
            docRef.update("content", message);
            // update isShown to false
            docRef.update("isShown", false);
            Toast.makeText(SendMessageActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
        });

        // btnBack goes back to the previous activity
        findViewById(R.id.btnBack).setOnClickListener(v -> {
            finish();
        });
    }
}