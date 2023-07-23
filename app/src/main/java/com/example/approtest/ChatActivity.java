package com.example.approtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;

import com.example.approtest.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.EventListener;

import org.checkerframework.checker.units.qual.C;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private Event event;

    private User current;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private FirebaseFirestore database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadChatEventDetails();
        init();
        listenMessages();
    }

    private void init() {
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                chatMessages,
                this.current
        );
        binding.chatRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private void listenMessages(){
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_EVENT, event)
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null){
            return;
        } else if (value != null) {
            int count = chatMessages.size();
            for(DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED) {
//                    ChatMessage chatMessage = new ChatMessage();
//                    chatMessage.sender = documentChange.getDocument().get(Constants.KEY_SENDER).toObject(User.class);
//                    chatMessage.event = (Event) documentChange.getDocument().get(Constants.KEY_EVENT);
//                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
//                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
//                    chatMessage.dateTime = getReadableDateTime(chatMessage.dateObject);
                    chatMessages.add(documentChange.getDocument().toObject(ChatMessage.class));
                    ChatMessage lastChatMessage = chatMessages.get(chatMessages.size() - 1);
                    lastChatMessage.dateTime = getReadableDateTime(lastChatMessage.dateObject);

                }
            }
            Collections.sort(chatMessages, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if (count == 0){
                chatAdapter.notifyDataSetChanged();
            } else {
                chatAdapter.notifyItemRangeChanged(chatMessages.size(), chatMessages.size());
                binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
        binding.progressBar.setVisibility(View.GONE);
    };

    private void loadChatEventDetails(){
        event = (Event) getIntent().getSerializableExtra(Constants.KEY_EVENT);
        current = (User) getIntent().getSerializableExtra(Constants.KEY_CURRENT_USER);
        binding.chatTitle.setText(event.eventName);
    }

    private void sendMessage() {
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER, current);
        message.put(Constants.KEY_EVENT, event);
        message.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());
        message.put(Constants.KEY_DATE_OBJECT, new Date());
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message);
        binding.inputMessage.setText(null);


    }

    private void setListeners(){
        binding.chatImageBack.setOnClickListener(v -> onBackPressed());
        binding.layoutSend.setOnClickListener(v -> sendMessage());
    }

    private String getReadableDateTime(Date date){
        return new SimpleDateFormat("dd MMMM, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }
}