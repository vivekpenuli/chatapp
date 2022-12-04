package com.example.chatappbeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatappbeta.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
EditText Rname, Remail,Rpass;
Button Register;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    FirebaseDatabase database;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // code here
        Rname = findViewById(R.id.Rusername);
        Remail = findViewById(R.id.Remail);
        Rpass = findViewById(R.id.Rpassword);
        Register = findViewById(R.id.register);

        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We'r creating your account");

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateField())
                {
                    Toast.makeText(RegisterActivity.this, "Please fill all field", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog.show();

                    auth.createUserWithEmailAndPassword(Remail.getText().toString().trim(), Rpass.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();

                                    if (task.isSuccessful()) {
                                        // SignupData signupData = new SignupData(binding.usernamesign.getText().toString(),
                                        //       binding.profeesionsign.getText().toString(), binding.emailsign.getText().toString(), binding.passwordsign.getText().toString());

                                        User user = new User(
                                                Rname.getText().toString(),
                                                Remail.getText().toString(),
                                                Rpass.getText().toString()
                                        );



                                        String id = task.getResult().getUser().getUid();
                                        database.getReference().child("User").child(id).setValue(user);


                                        Toast.makeText(RegisterActivity.this, "Detail uploaded", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                       finish();


                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Already Register Account", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });



                }


            }
        });


    }

    private boolean validateField() {
        int flag=0;
        if (Rname.getText().toString().isEmpty())
        {
            //  Toast.makeText(signup.this, "name is" + binding.usernamesign.getText().toString(), Toast.LENGTH_SHORT).show();
            Rname.setError("Enter Name");
            flag =1;
        }
        if (Remail.getText().toString().isEmpty())
        {
            Remail.setError("Enter Email");
            flag=1;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(Remail.getText().toString()).matches())
        {
            Remail.setError("Enter Valid Email");
            flag=1;
        }

        if (Rpass.getText().toString().isEmpty())
        {
            Rpass.setError("Enter Password");
            flag=1;
        }
        else if (Rpass.getText().toString().length() <6 )
        {
            Rpass.setError("Password Lenght greater then 6");
            flag=1;
        }

        if (flag==1)
        {
            return true;
        }
        else
        {
            return false;
        }

    }
}