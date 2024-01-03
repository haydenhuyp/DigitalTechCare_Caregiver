package com.techcare.techcare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UploadSchedule extends AppCompatActivity {
    Uri imageUri;
    StorageReference storageReference;
    ImageView imgUploadSchedule;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_schedule);

        imgUploadSchedule = findViewById(R.id.imgUploadSchedule);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        findViewById(R.id.btnUploadImgUploadSchedule).setVisibility(View.GONE);
        findViewById(R.id.btnSelectImgUploadSchedule).setOnClickListener(v -> {
            selectImage();
        });

        findViewById(R.id.btnUploadImgUploadSchedule).setOnClickListener(v -> {
            uploadImage();
        });

        findViewById(R.id.btnBackUploadSchedule).setOnClickListener(v -> {
            finish();
        });
    }

    private void uploadImage() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        SimpleDateFormat formater = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String fileName = "menu" /* + formater.format(now)*/;
        storageReference = FirebaseStorage.getInstance().getReference("images/" + fileName);
        storageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            imgUploadSchedule.setImageURI(null);
            Toast.makeText(this, "Image uploaded", Toast.LENGTH_SHORT).show();
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }).addOnFailureListener(e -> {
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show();
        });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
        findViewById(R.id.btnUploadImgUploadSchedule).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null){
            imageUri = data.getData();
            imgUploadSchedule.setImageURI(imageUri);
        }
    }
}