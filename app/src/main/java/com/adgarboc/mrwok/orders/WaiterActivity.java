package com.adgarboc.mrwok.orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import Model.Base;
import Model.Order;
import Model.Protein;

public class WaiterActivity extends AppCompatActivity
{
    private EditText edName;
    private RadioGroup rgBase;
    private RadioGroup rgProtein;
    private Button btSendOrder;

    private String name;
    private String base;
    private String protein;

    FirebaseAuth fAuth;
    FirebaseFirestore fDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter);

        edName = (EditText) findViewById(R.id.etxName);
        rgBase = (RadioGroup) findViewById(R.id.rgpBase);
        rgProtein = (RadioGroup) findViewById(R.id.rgpProtein);
        btSendOrder = (Button) findViewById(R.id.btnSendOrder);

        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseFirestore.getInstance();
    }

    public void logout(View view)
    {
        if (fAuth.getCurrentUser() != null)
        {
            fAuth.signOut();
        }
        Intent intent = new Intent(WaiterActivity.this, LoginActivity.class);
        intent.addFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void SendOrderOnClick(View view)
    {
        edName.setEnabled(false);
        rgBase.setEnabled(false);
        rgProtein.setEnabled(false);
        btSendOrder.setEnabled(false);

        name = edName.getText().toString().trim();
        if (name.isEmpty())
        {
            Toast.makeText(this, "Input name client", Toast.LENGTH_SHORT).show();
            edName.setEnabled(true);
            rgBase.setEnabled(true);
            rgProtein.setEnabled(true);
            btSendOrder.setEnabled(true);
            return;
        }
        switch (rgBase.getCheckedRadioButtonId())
        {
            case R.id.rbnRice:
                base = "rice";
                break;
            case R.id.rbnPasta:
                base = "pasta";
                break;
            case R.id.rbnMix:
                base = "mix";
                break;
            default:
                Toast.makeText(this, "Select a base", Toast.LENGTH_SHORT).show();
                edName.setEnabled(true);
                rgBase.setEnabled(true);
                rgProtein.setEnabled(true);
                btSendOrder.setEnabled(true);
                return;
        }

        switch (rgProtein.getCheckedRadioButtonId())
        {
            case R.id.rbnChicken:
                protein = "chicken";
                break;
            case R.id.rbnBeef:
                protein = "beef";
                break;
            case R.id.rbnShrimp:
                protein = "shrimp";
                break;
            case R.id.rbnSoy:
                protein = "soy";
                break;
            default:
                Toast.makeText(this, "Select a protein", Toast.LENGTH_SHORT).show();
                edName.setEnabled(true);
                rgBase.setEnabled(true);
                rgProtein.setEnabled(true);
                btSendOrder.setEnabled(true);
                return;
        }

        Base _base = new Base();
        _base.setName(base);

        Protein _protein = new Protein();
        _protein.setName(protein);

        Order order = new Order();
        order.setName(name);
        order.setBase(_base);
        order.setProtein(_protein);
        order.setAttended(false);
        order.setTimestamp(FieldValue.serverTimestamp());

        CollectionReference cRef = fDatabase.collection("orders");
        cRef.add(order).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) 
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(WaiterActivity.this, "Order sent", Toast.LENGTH_SHORT).show();
                    edName.setEnabled(true);
                    rgBase.setEnabled(true);
                    rgProtein.setEnabled(true);
                    btSendOrder.setEnabled(true);
                }
                else
                {
                    Toast.makeText(WaiterActivity.this, "Error: Order don't sent", Toast.LENGTH_SHORT).show();
                    edName.setEnabled(true);
                    rgBase.setEnabled(true);
                    rgProtein.setEnabled(true);
                    btSendOrder.setEnabled(true);
                }
            }
        });



    }
}
