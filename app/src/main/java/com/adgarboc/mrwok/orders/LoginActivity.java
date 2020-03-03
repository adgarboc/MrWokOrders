package com.adgarboc.mrwok.orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
{
    private EditText etMail;
    private EditText etPassword;
    private Button btLogin;
    private Button btRegister;

    private FirebaseAuth fAuth;

    private String mail = "";
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etMail = (EditText) findViewById(R.id.etxMail);
        etPassword = (EditText) findViewById(R.id.etxPassword);
        btLogin = (Button) findViewById(R.id.btnLogin);
        btRegister = (Button) findViewById(R.id.btnRegister);

        fAuth = FirebaseAuth.getInstance();

    }
    @Override
    protected void onResume()
    {
        super.onResume();
        btLogin.setEnabled(true);
        btRegister.setEnabled(true);
        etMail.setEnabled(true);
        etPassword.setEnabled(true);
    }

    public void btnLoginOnClick(View view)
    {
        btLogin.setEnabled(false);
        btRegister.setEnabled(false);
        etMail.setEnabled(false);
        etPassword.setEnabled(false);

        mail = etMail.getText().toString().trim();
        password = etPassword.getText().toString();
        
        if (!mail.isEmpty())
        {
            if (!password.isEmpty())
            {
                loginUser();
            }
            else
            {
                Toast.makeText(this, "Escribe tu contraseña", Toast.LENGTH_SHORT).show();
                etMail.setEnabled(true);
                etPassword.setEnabled(true);
                btLogin.setEnabled(true);
                btRegister.setEnabled(true);
            }
        }
        else
        {
            Toast.makeText(this, "Escribe tu correo", Toast.LENGTH_SHORT).show();
            etMail.setEnabled(true);
            etPassword.setEnabled(true);
            btLogin.setEnabled(true);
            btRegister.setEnabled(true);
        }
    }

    private void loginUser()
    {
        fAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(LoginActivity.this, "Inicio de sesion correcto", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Correo y/o contraseña invalidos", Toast.LENGTH_SHORT).show();
                    etMail.setEnabled(true);
                    etPassword.setEnabled(true);
                    btLogin.setEnabled(true);
                    btRegister.setEnabled(true);
                }
            }
        });
    }

    public void btnRegisterOnClick(View view)
    {
        btLogin.setEnabled(false);
        btRegister.setEnabled(false);
        etMail.setEnabled(false);
        etPassword.setEnabled(false);
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
