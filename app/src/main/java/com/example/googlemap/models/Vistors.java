package com.example.googlemap.models;

import android.content.Intent;
import android.widget.Toast;

import com.example.googlemap.HomeActivity;
import com.example.googlemap.Login;
import com.example.googlemap.Register;
import com.example.googlemap.utils.Globals;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Vistors {
    String email, password;

    private String name;
    private String phone;


    public Vistors()
    {
        
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static void loginUser(String email, String password, Login context) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseDatabase.getInstance().getReference()
                                    .child("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Globals.user = snapshot.getValue(Vistors.class);
                                            // Login success, navigate to the main activity or other desired activity
                                            Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(context, HomeActivity.class);
                                            context.startActivity(intent);
                                            ((AppCompatActivity) context).finish();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });// Finish the login activity to prevent returning to it using the back button

                            // Add code to navigate to the main activity here
                        } else {
                            // If login fails, display an error message
                            Toast.makeText(context, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public static void registerUser(String email, String password, String fullName, final Register activity) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration success, navigate to the login activity
                            Vistors vistors = new Vistors();
                            vistors.setEmail(email);
                            vistors.setName(fullName);
                            vistors.setPassword(password);

                            FirebaseDatabase.getInstance().getReference()
                                    .child("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(vistors);

                            Globals.user = vistors;

                            Toast.makeText(activity, "Registration successful", Toast.LENGTH_SHORT).show();
                            activity.startActivity(new Intent(activity, Login.class));
                            activity.finish(); // Finish the registration activity
                        } else {
                            // If registration fails, display an error message
                            Toast.makeText(activity, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
