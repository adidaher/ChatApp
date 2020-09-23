package com.example.chatapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;


import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText username , email , password;
    Button btn_register;

    FirebaseAuth auth;
    DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);

        auth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username,txt_email,txt_password;
                txt_username = username.getText().toString();
                txt_email = email.getText().toString();
                txt_password = password.getText().toString();

                if(txt_username.equals("") || txt_email.equals("") || txt_password.equals("")){

                    Toast.makeText(RegisterActivity.this,"All fields are required",Toast.LENGTH_SHORT).show();

                }else if(txt_password.length() < 6) {
                    Toast.makeText(RegisterActivity.this,"password mut be at least 6 characters",Toast.LENGTH_SHORT).show();

                }else {
                    register(txt_username,txt_email,txt_password);
                }
            }
        });

    }

    private void register(final String username , String email , String password){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    assert firebaseUser != null;
                    String userid = firebaseUser.getUid();


                    //creating user's database
                    //user's/userid(currentuser)/..
                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("id",userid);
                    hashMap.put("username",username);
                    hashMap.put("imageURL","default");
                    hashMap.put("status","offline");

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Intent intent = new Intent(RegisterActivity.this, StartActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            }else {
                                Toast.makeText(RegisterActivity.this,"you can't register with this email or password",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                  /*  reference.child("id").setValue(userid);
                    reference.child("username").setValue(username);
                    reference.child("imageURL").setValue("default");
                    Intent intent = new Intent(RegisterActivity.this,StartActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();*/
                }
            }
        });

    }
}