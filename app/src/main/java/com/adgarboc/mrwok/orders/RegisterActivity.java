package com.adgarboc.mrwok.orders;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import Model.User;

public class RegisterActivity extends AppCompatActivity
{
    private EditText etName;
    private EditText etMail;
    private EditText etPassword;
    private CheckBox cbImChef;
    private Button btRegister;

    private FirebaseAuth fAuth;
    private FirebaseFirestore fDatabase;

    private String name;
    private String mail;
    private String password;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = (EditText) findViewById(R.id.etxName);
        etMail = (EditText) findViewById(R.id.etxMail);
        etPassword = (EditText) findViewById(R.id.etxPassword);
        cbImChef = (CheckBox) findViewById(R.id.cbxImChef);
        btRegister = (Button) findViewById(R.id.btnRegister);

        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseFirestore.getInstance();

        userType = "waiter";
    }

    public void btnRegisterOnClick(View view)
    {
        btRegister.setEnabled(false);
        etName.setEnabled(false);
        etMail.setEnabled(false);
        etPassword.setEnabled(false);
        cbImChef.setEnabled(false);

        name = etName.getText().toString().trim();
        mail = etMail.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if (!name.isEmpty())
        {
            if (!mail.isEmpty())
            {
                if (isValidEmail(etMail.getText().toString().trim()))
                {
                    if (!password.isEmpty())
                    {
                        if(password.length() >= 6)
                        {
                            userRegister();
                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this, "El password debe contener 6 o mas carateres", Toast.LENGTH_SHORT).show();
                            btRegister.setEnabled(true);
                            etName.setEnabled(true);
                            etMail.setEnabled(true);
                            etPassword.setEnabled(true);
                            cbImChef.setEnabled(true);
                        }
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, "Agrega una contraseña", Toast.LENGTH_SHORT).show();
                        btRegister.setEnabled(true);
                        etName.setEnabled(true);
                        etMail.setEnabled(true);
                        etPassword.setEnabled(true);
                        cbImChef.setEnabled(true);

                    }
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Correo invalido", Toast.LENGTH_SHORT).show();
                    btRegister.setEnabled(true);
                    etName.setEnabled(true);
                    etMail.setEnabled(true);
                    etPassword.setEnabled(true);
                    cbImChef.setEnabled(true);
                }
            }
            else
            {
                Toast.makeText(RegisterActivity.this, "Agrega tu correo", Toast.LENGTH_SHORT).show();
                btRegister.setEnabled(true);
                etName.setEnabled(true);
                etMail.setEnabled(true);
                etPassword.setEnabled(true);
                cbImChef.setEnabled(true);
            }
        }
        else
        {
            Toast.makeText(RegisterActivity.this, "Agrega tu nombre", Toast.LENGTH_SHORT).show();
            btRegister.setEnabled(true);
            etName.setEnabled(true);
            etMail.setEnabled(true);
            etPassword.setEnabled(true);
            cbImChef.setEnabled(true);
        }
    }

    private void userRegister()
    {
        if (cbImChef.isChecked())
        {
            userType = "chef";
        }
        fAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    User user = new User();
                    user.setName(name);
                    user.setMail(mail);
                    user.setPassword(password);
                    user.setUsertype(userType);
                    user.setStatus(true);
                    user.setTimeStamp(FieldValue.serverTimestamp());
                    user.setUid(fAuth.getUid());

                    CollectionReference cRef = fDatabase.collection("users");
                    cRef.document(user.getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2)
                        {
                            if (task2.isSuccessful())
                            {
                                Toast.makeText(RegisterActivity.this, "Registro correcto", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.addFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this, "Error al guardar datos (intentar con otro correo)", Toast.LENGTH_SHORT).show();
                                btRegister.setEnabled(true);
                                etName.setEnabled(true);
                                etMail.setEnabled(true);
                                etPassword.setEnabled(true);
                                cbImChef.setEnabled(true);
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Error al registrarse", Toast.LENGTH_SHORT).show();
                    btRegister.setEnabled(true);
                    etName.setEnabled(true);
                    etMail.setEnabled(true);
                    etPassword.setEnabled(true);
                    cbImChef.setEnabled(true);
                }
            }
        });
    }
    private boolean isValidEmail(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

}
