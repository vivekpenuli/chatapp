package com.example.chatappbeta;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.chatappbeta.Model.User;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

ImageView userimg, uplimg;
    ProgressDialog progressDialog;
    Uri uri;
    ImageView logout;
    int flag=-1;

   // StorageReference storageReference;
    FirebaseDatabase database;
    StorageReference storageReference;
    FirebaseAuth auth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
getSupportActionBar().hide();
userimg  = findViewById(R.id.userimg);
uplimg= findViewById(R.id.uploadimg);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Uploading Profile image");
        progressDialog.setMessage("Image uploading");


        storageReference = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();



        database.getReference().child("User").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    User user = snapshot.getValue(User.class);
                    Picasso.get()
                            .load(user.getProfile_photo())
                            .placeholder(R.drawable.profile)
                            .into(userimg);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });










        uplimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(MainActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080,1080)
                        .createIntent(intent -> {
                            startForMediaPickerResult.launch(intent);
                            return null;
                        });

            }
        });


    }
    private final ActivityResultLauncher<Intent> startForMediaPickerResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                Intent data = result.getData();
                if (data != null && result.getResultCode() == Activity.RESULT_OK) {
                    uri = data.getData();

                    if (uri==null)
                    {
                        Toast.makeText(MainActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        progressDialog.show();

                        userimg.setImageURI(uri);

                        StorageReference dref = storageReference.child("profile_photo").child(FirebaseAuth.getInstance().getUid());
                        dref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();

                                Toast.makeText(MainActivity.this, "profile photo Updated", Toast.LENGTH_SHORT).show();
                                dref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        database.getReference().child("User").child(FirebaseAuth.getInstance().getUid()).child("profile_photo").setValue(uri.toString());


                                    }
                                });

                            }
                        });


                    }
                }
            }
            );






}