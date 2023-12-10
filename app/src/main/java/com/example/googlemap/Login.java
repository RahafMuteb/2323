package com.example.googlemap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.googlemap.models.Vistors;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText email,password;
    Button loginBtn,gotoRegister,gotoAdmin;
    boolean valid = true;
    FirebaseAuth firebaseAuth;


    // Hardcoded admin credentials
    //private static final String ADMIN_EMAIL = "admin@locker.com";
    //private static final String ADMIN_PASSWORD = "admin123";
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginBtn);
        gotoRegister = findViewById(R.id.gotoRegister);
//        gotoAdmin=findViewById(R.id.gotoAdmin);



        firebaseAuth = FirebaseAuth.getInstance();

        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                finish();

            }
        });
//        gotoAdmin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                if (checkAdminCredentials()) {
//                    // Admin login successful
//                    Toast.makeText(Login.this, "Admin login successful", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(Login.this, AdminPanel.class);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    // Admin login failed
//                    Toast.makeText(Login.this, "Admin login failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

//
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Validate input fields
//                if (TextUtils.isEmpty(email.getText().toString().trim()) || TextUtils.isEmpty(password.getText().toString())) {
//                    Toast.makeText(Login.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
//                } else {
//                    // Log in the user with Firebase
//                    loginUser(email.getText().toString(), password.getText().toString());
//                }
//            }
//        });

//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Check if email and password fields are valid
//                if (checkField(email) && checkField(password)) {
//                    // Log in the user using Firebase
//                    loginUser(email.getText().toString(), password.getText().toString());
//                } else {
//                    Toast.makeText(Login.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if email and password fields are valid
                if (checkField(email) && checkField(password)) {
                    // Attempt login with Firebase
                    String enteredEmail = email.getText().toString();
                    String enteredPassword = password.getText().toString();
                        // User login or invalid credentials
                        Vistors.loginUser(enteredEmail, enteredPassword, Login.this);

                } else {
                    Toast.makeText(Login.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        checkField(email);
        checkField(password);

    }
    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }





//
//    private void loginUser(String email, String password) {
//        firebaseAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Login success, navigate to the main activity or other desired activity
//                            Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(Login.this, HomeActivity.class);
//                            startActivity(intent);
//                            finish(); // Finish the login activity to prevent returning to it using the back button
//
//                            // Add code to navigate to the main activity here
//                        } else {
//                            // If login fails, display an error message
//                            Toast.makeText(Login.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }


}