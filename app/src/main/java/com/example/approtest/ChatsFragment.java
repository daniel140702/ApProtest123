package com.example.approtest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.approtest.databinding.FragmentChatsBinding;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class ChatsFragment extends Fragment {

    private FragmentChatsBinding binding;

    HashMap<String,Event> events;
    public ChatsFragment(HashMap<String,Event> events)
    {
        this.events = events;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentChatsBinding.inflate(getLayoutInflater());



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_chats, container, false);
        return binding.getRoot();
    }

    private void getEvent(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_EVENTS)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
//                    if(task.isSuccessful() && task.getResult() != null) {
//                        List<Event> events  new ArrayList<>();
//                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

//                            ArrayList<User> participants = queryDocumentSnapshot.getData();



//                        }
//                    }
                });
    }

    private void loading(Boolean isLoading) {
        if (isLoading){
            binding.chatsProgressBar.setVisibility(View.VISIBLE);
        } else {
            binding.chatsProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void showErrorMessage(){
        binding.textErrorMessage.setText(String.format("%s", "Events are unavailiable"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }
}