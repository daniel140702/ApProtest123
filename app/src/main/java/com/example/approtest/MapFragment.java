package com.example.approtest;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    HashMap<String,Event> events;
    FirebaseFirestore db;
    LatLng place;

    GoogleMap map;

    User current;

    protected HashMap<String, Marker> markers;
    public MapFragment(HashMap<String,Event> events, User current)
    {
        this.events = events;
        this.current = current;
        Log.d("onsucc", current.getFullName()+current.getEmail()+current.getToken());
        place = new LatLng(0,0);
    }

    private void updateCurrent()
    {
        Log.d("peeo", mAuth.getCurrentUser().getUid());
        DocumentReference docRef = db.collection("users").document(mAuth.getCurrentUser().getUid());
        Log.d("peeo", "here213");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                Log.d("peeo", user.getFullName());
                current.setUser(user);
            }
        });
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            updateCurrent();
            updateEvents();
            markers = new HashMap<String,Marker>();
            LatLng sydney = new LatLng(31, 35);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            MarkerOptions markerOptions = new MarkerOptions().position(sydney).title("current");
            Marker tempMarker = googleMap.addMarker(markerOptions);
            tempMarker.setVisible(false);

            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng)
                {
                    updateEvents();
                    if (markerMoveEnabled) {
                        place =new LatLng(latLng.latitude,latLng.longitude);
                        tempMarker.setVisible(true);
                        tempMarker.setPosition(latLng);
                        showDialog(tempMarker);
                    }
                    else
                    {
                        tempMarker.setVisible(false);
                    }
                }
            });
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    String mName = marker.getTitle();
                    View rootView = getView(); // Get the root view of your activity/fragment

                    // Check if rootView is null before proceeding
                    if (rootView == null) {
                        return false;
                    }
                    // Create and show a Snackbar with the marker title
                    Snackbar snackbar = Snackbar.make(rootView, mName, Snackbar.LENGTH_SHORT);

                    // Add an action to the Snackbar for the click event
                    snackbar.setAction("Show Details", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Create and show a custom dialog here
                            showDialogWithMarkerTitle(mName);
                        }
                    });

                    snackbar.show();

                    // Return true to indicate the event has been consumed
                    return true;
                }

                // Method to show a custom dialog with the marker title
            });
        }
    };


//    private void buildDialog (String markerTitle) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle(markerTitle);
//        builder.setMessage(markerTitle);
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        // build negativeButton
//
//        return;
//    }

    private boolean checkParticipatingFromDB(String eveName,User curr){
        DocumentReference documentReference = db.collection("events").document(eveName);
        final boolean flag[] = {false};
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Event tempEve = documentSnapshot.toObject(Event.class);
                Log.d("onsucc", "succ");
                flag[0] = tempEve.hasUser(current);
            }
        });
        return flag[0];
    }

    private boolean checkParticipating(String eveName,User curr){
        return events.get(eveName).hasUser(curr);
    }

    /*private void constructNegativeButton(boolean participating){
        if (participating){
            builder.setNegativeButton("Unsubscribe From Event", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Delete event
                    dialog.dismiss();
                }
            });
        }
    } */



    private void showDialogWithMarkerTitle(String markerTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(markerTitle);
        builder.setMessage(markerTitle);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Log.d("bool check", String.valueOf(checkParticipating(markerTitle,current))); // check
        if (checkParticipating(markerTitle,current)) {
            builder.setNegativeButton("Unsubscribe From Event", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    events.get(markerTitle).participants.remove(current.getToken());
                    update(events.get(markerTitle));
                    dialog.dismiss();
                }
            });
        }
        else{
            builder.setNegativeButton("Subscribe To Event", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    events.get(markerTitle).addUser(current);
                    update(events.get(markerTitle));
                    dialog.dismiss();
                }
            });
        }
        // Create the AlertDialog instance and show it
        AlertDialog dialog = builder.create();
        dialog.show();
    }


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

                        isAdmin = document.getBoolean("admin");

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

    /*rivate BitmapDescriptor getCustomMarkerIcon(int color) {
        Drawable background = ContextCompat.getDrawable(getContext(), R.drawable.ic_custom_marker);
        background.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(((BitmapDrawable) background).getBitmap());
        return bitmapDescriptor;
    }*/

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    public void updateEvents()
    {
        events.clear();
        map.clear();
        CollectionReference colRef = db.collection("events");
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Event event = new Event(document.toObject(Event.class));
                        Log.d("peepeepoopoo", String.valueOf(event.getLatitude()));
                        events.put(event.getEventName(),event);
                        LatLng pos = new LatLng(event.getLatitude(), event.getLongitude());
                        String name = event.getEventName();
                        MarkerOptions markerOptions = new MarkerOptions().position(pos).title(name);
                        if (event.hasUser(current)){markerOptions
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));}
                        Marker eventMarker = map.addMarker(markerOptions);
                        markers.put(event.getEventName(),eventMarker);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }





    private void update(Event event)
    {
        DocumentReference documentReference = db.collection("events").document(event.eventName);
        documentReference.set(event).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("peepeepoopoo", "here2");
            }
        });
    }


    private void saveEvent(String name, String date, LatLng place)
    {
        Log.d("shitpit", current.getToken());
        double latitude = place.latitude;
        double longitude = place.longitude;
        Event event = new Event(name,date,latitude,longitude);
        event.addUser(current);
        Log.d("shitpit", event.getParticipants().get(current.getToken()).getToken());
        DocumentReference documentReference = db.collection("events").document(name);
        documentReference.set(event).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("peepeepoopoo", "here2");
            }
        });
    }


    private void showDialog(Marker tempMarker) {
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
                String name = String.valueOf(eventN.getText());
                String date = String.valueOf(eventD.getText());
                saveEvent(name, date, place);
                tempMarker.setVisible(false);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing or perform any required action
                tempMarker.setVisible(false);
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }


}


