package com.adgarboc.mrwok.orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import Model.Base;
import Model.Order;
import Model.Protein;

public class ChefActivity extends AppCompatActivity
{
    FirebaseFirestore fDatabase;
    FirebaseAuth fAuth;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef);

        linearLayout = (LinearLayout) findViewById(R.id.lltCards);
        fDatabase = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        getOrders();
    }

    private void getOrders()
    {
        CollectionReference cRef = fDatabase.collection("orders");
        cRef.orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>()
        {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e)
            {
                if (e != null) {
                    Log.w("Error Listener", "Listen failed.", e);
                    return;
                }

                List<Order> orders = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots)
                {
                        if (!doc.getBoolean("attended"))
                        {
                            Order order = new Order();
                            order.setUid(doc.getId());
                            order.setName(doc.get("name").toString());
                            Map<String, Object> mBase = (Map<String, Object>) doc.get("base");
                            Base base = new Base();
                            base.setName(mBase.get("name").toString());
                            base.setPrice(Double.valueOf(mBase.get("price").toString()));

                            order.setBase(base);

                            Map<String, Object> mProtein = (Map<String, Object>) doc.get("protein");
                            Protein protein = new Protein();
                            protein.setName(mProtein.get("name").toString());
                            protein.setPrice(Double.valueOf(mProtein.get("price").toString()));
                            order.setProtein(protein);
                            order.setAttended(doc.getBoolean("attended"));
                            orders.add(order);
                        }
                }
                if (orders != null)
                {
                    fillCardView(orders);
                }
            }
        });
    }

    private void fillCardView(final List<Order> orders)
    {

        LayoutInflater linf = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linf = LayoutInflater.from(ChefActivity.this);

        LinearLayout tbl_layout=(LinearLayout)findViewById(R.id.lltCards);
        tbl_layout.removeAllViews();
        for (int i = 0; i < orders.size(); i++)
        {
            final int z = i;
            View v = linf.inflate(R.layout.order_card, linearLayout, false);//Pass your linearLayout

            ((TextView) v.findViewById(R.id.tvwName)).setText("Client: " + orders.get(i).getName());
            ((TextView) v.findViewById(R.id.tvwBase)).setText("Base: " + orders.get(i).getBase().getName());
            ((TextView) v.findViewById(R.id.tvwProtein)).setText("Protein: " + orders.get(i).getProtein().getName());

            ((Button) v.findViewById(R.id.btnAttended)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    ((Button) v.findViewById(R.id.btnAttended)).setEnabled(false);

                    DocumentReference dRef = fDatabase.collection("orders").document(orders.get(z).getUid());
                    dRef.update("attended", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) 
                        {
                            Toast.makeText(ChefActivity.this, "Attended :)", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChefActivity.this, "Error" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            if (i != 0)
            {
                ((Button) v.findViewById(R.id.btnAttended)).setEnabled(false);
            }

            tbl_layout.addView(v);
        }

    }

    public void logout(View view)
    {
        if (fAuth.getCurrentUser() != null)
        {
            fAuth.signOut();
        }
        Intent intent = new Intent(ChefActivity.this, LoginActivity.class);
        intent.addFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
