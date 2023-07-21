package com.example.approtest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapFragment extends Fragment {
    private boolean markerMoveEnabled = false;
    Boolean isAdmin;

    private ViewGroup layoutContainer; // Container for the layout to be displayed
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ArrayList<Event> events;
    FirebaseFirestore db;
    LatLng place;

    public MapFragment(ArrayList<Event> events)
    {
        this.events = events;
        place = new LatLng(0,0);
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng sydney = new LatLng(31, 35);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            MarkerOptions markerOptions = new MarkerOptions().position(sydney).title("current");
            Marker tempMarker = googleMap.addMarker(markerOptions);
            tempMarker.setVisible(false);
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    for(int i =0;i < events.size();i++)
                    {
                        LatLng pos = events.get(i).getLatLang();
                        String name = events.get(i).getEventName();
                        MarkerOptions markerOptions = new MarkerOptions().position(pos).title(name);
                        Marker eventMarker = googleMap.addMarker(markerOptions);
                    }
                    if (markerMoveEnabled) {
                        place =new LatLng(latLng.latitude,latLng.longitude);
                        tempMarker.setVisible(true);
                        tempMarker.setPosition(latLng);
                        showDialog();
                    }
                    else
                    {
                        tempMarker.setVisible(false);
                    }
                }
            });
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker)
                {
                    String mName = marker.getTitle();
                    Toast.makeText(getActivity().getApplicationContext(),mName,Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        FloatingActionButton add_button_floating=rootView.findViewById(R.id.add_button_floating);
        add_button_floating.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        DocumentReference userDoc = db.collection("users").document(currentUser.getUid());
        userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {

                        isAdmin = document.getBoolean("Admin");

                        if (isAdmin)
                            add_button_floating.setVisibility(View.VISIBLE);


                    } else {
                    }
                } else {
                }
            }
        });





        add_button_floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markerMoveEnabled=!markerMoveEnabled;
                //Intent intent = new Intent(getActivity(), NewEventActivity.class);
                //startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }



    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Event");

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText eventN = new EditText(getActivity());
        eventN.setHint("Event Name");
        layout.addView(eventN);

        final EditText eventD = new EditText(getActivity());
        eventD.setHint("Date");
        layout.addView(eventD);
        builder.setView(layout);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String eventName = String.valueOf(eventN.getText());
                String eventDate = String.valueOf(eventD.getText());
                Event event = new Event(eventName,eventDate,place);
                User user = new User();
                event.addUser(user);
                events.add(event);

                db.collection("events").document(eventName)
                        .set(event)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Log.w(TAG, "Error writing document", e);
                            }
                        });
                // Implement your logic here using the user input
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing or perform any required action
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }


}


