package com.example.approtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.approtest.databinding.ActivityChatBinding;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private Event event;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadChatEventDetails();
    }

    private void loadChatEventDetails(){
        event = (Event) getIntent().getSerializableExtra(Constants.KEY_EVENT);
        binding.chatTitle.setText(event.eventName);
    }

    private void setListeners(){
        binding.chatImageBack.setOnClickListener(v -> onBackPressed());
    }
}