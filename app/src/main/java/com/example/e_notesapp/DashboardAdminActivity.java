package com.example.e_notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.example.e_notesapp.adapters.AdapterCategory;
import com.example.e_notesapp.databinding.ActivityDashboardAdminBinding;
import com.example.e_notesapp.models.ModelCategory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardAdminActivity extends AppCompatActivity {

    //view binding
    private ActivityDashboardAdminBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;
    
   //arraylist to store category
    private ArrayList<ModelCategory> categoryArrayList;
    private AdapterCategory adapterCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        loadCategories();

        //edit text change lister, search
        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // called as and when the user types each letter
                try {
                    adapterCategory.getFilter().filter(s);
                } catch (Exception e) {
                    e.printStackTrace(); // Log the exception or handle it appropriately
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
            }
        });

        //handle click, start category add screen
        binding.addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardAdminActivity.this, CategoryAddActivity.class));
            }
        });

        //handle click, start category add screen
        binding.addPdfFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardAdminActivity.this,  PdfAddActivity.class));
            }
        });

        //handle click, start about button
        binding.aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardAdminActivity.this,  AboutPage.class));
            }
        });
    }

    private void loadCategories() {
        //init arraylist
        categoryArrayList = new ArrayList<>();

        //get all categories from firebase > Categories
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //clear arraylist before adding data into it
                categoryArrayList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    //get data
                    ModelCategory model = ds.getValue(ModelCategory.class);

                    //add to arraylist
                    categoryArrayList.add(model);
                }
                //setup adapter
                adapterCategory = new AdapterCategory(DashboardAdminActivity.this, categoryArrayList);
                //set adapter to recyclerview
                binding.categoriesRv.setAdapter(adapterCategory);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Log or handle the error
                Log.e("DashboardAdminActivity", "DatabaseError: " + error.getMessage());
            }

        });
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            String email = firebaseUser.getEmail();
            if (email != null) {
                binding.subtitleIv.setText(email);
            }
        }
    }

}