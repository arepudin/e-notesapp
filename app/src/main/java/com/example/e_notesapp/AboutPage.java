package com.example.e_notesapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import com.example.e_notesapp.databinding.ActivityAboutPageBinding;

public class AboutPage extends AppCompatActivity {

    private ActivityAboutPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Use binding to access TextView
        binding.text1.setMovementMethod(LinkMovementMethod.getInstance());

        // handle click, go back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
