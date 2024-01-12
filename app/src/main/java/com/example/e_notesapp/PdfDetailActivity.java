package com.example.e_notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.e_notesapp.MyApplication;
import com.example.e_notesapp.databinding.ActivityPdfDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class PdfDetailActivity extends AppCompatActivity {

    // view binding
    private ActivityPdfDetailBinding binding;

    // pdf id, get from intent
    String bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // get data from intent e.g. bookId
        Intent intent = getIntent();
        bookId = intent.getStringExtra("bookId");

        loadBookDetails();

        // handle click, go back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //handle click, open to view pdf
        binding.readBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(PdfDetailActivity.this, PdfViewActivity.class);
                intent1.putExtra("bookId", bookId);
                        startActivity(intent1);
            }
        });

    }

    private void loadBookDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(Objects.requireNonNull(bookId))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // get data
                        String title = Objects.requireNonNull(snapshot.child("title").getValue()).toString();
                        String description = Objects.requireNonNull(snapshot.child("description").getValue()).toString();
                        String categoryId = Objects.requireNonNull(snapshot.child("categoryId").getValue()).toString();
                        String url = Objects.requireNonNull(snapshot.child("url").getValue()).toString();
                        String timestamp = Objects.requireNonNull(snapshot.child("timestamp").getValue()).toString();

                        // format date
                        String date = MyApplication.formatTimestamp(Long.parseLong(timestamp));

                        MyApplication.loadCategory(
                                categoryId,
                                binding.categoryTv
                        );
                        MyApplication.loadPdfFromUrlSinglePage(
                                url,
                                title,
                                binding.pdfView,  // Pass PDFView parameter here
                                binding.progressBar
                        );
                        MyApplication.loadPdfSize(
                                url,
                                title,
                                binding.sizeTv
                        );

                        // set data
                        binding.titleTv.setText(title);
                        binding.descriptionTv.setText(description);
                        binding.dateTv.setText(date);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
