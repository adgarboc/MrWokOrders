package com.adgarboc.mrwok.orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import Model.User;

public class MainActivity extends AppCompatActivity
{
    FirebaseAuth fAuth;
    FirebaseFirestore fDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseFirestore.getInstance();

        checkLogin();
    }

    private void checkLogin()
    {
        if (fAuth.getCurrentUser() != null)
        {
            getUserInfo();
        }
        else
        {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void getUserInfo()
    {
        String uid = fAuth.getCurrentUser().getUid();
        DocumentReference dRef = fDatabase.collection("users").document(uid);
        dRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {
                        User user = new User();
                        user.setUid(document.getId());
                        user.setName(document.get("name").toString());
                        user.setMail(document.get("mail").toString());
                        user.setPassword(document.get("password").toString());
                        user.setUsertype(document.get("usertype").toString());
                        user.setStatus((boolean) document.get("status"));
                        user.setTimeStamp((FieldValue) document.get("timestamp"));
                        toPersonalizeActivity(user);
                    }
                    else 
                    {
                        Toast.makeText(MainActivity.this, "No hay datos", Toast.LENGTH_SHORT).show();
                        logout();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Error al obtener datos " + task.getException(), Toast.LENGTH_SHORT).show();
                    logout();
                }
            }
        });
    }

    private void toPersonalizeActivity(User user)
    {
        if (user.isStatus())
        {
            setTheme(R.style.AppTheme);
            switch (user.getUsertype())
            {
                case "director":
                    Intent director = new Intent(MainActivity.this, DirectorActivity.class);
                    director.addFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(director);
                    finish();
                    break;

                case "chef":
                    Intent chef = new Intent(MainActivity.this, ChefActivity.class);
                    chef.addFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(chef);
                    finish();
                    break;

                case "waiter":
                    Intent waiter = new Intent(MainActivity.this, WaiterActivity.class);
                    waiter.addFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(waiter);
                    finish();
                    break;
            }
        }
        else
        {
            Toast.makeText(this, "Usuario deshabilitado", Toast.LENGTH_SHORT).show();
            logout();
        }
    }

    public void logout()
    {
        if (fAuth.getCurrentUser() != null)
        {
            fAuth.signOut();
        }
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    public void logout(View view)
    {
        if (fAuth.getCurrentUser() != null)
        {
            fAuth.signOut();
        }
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
