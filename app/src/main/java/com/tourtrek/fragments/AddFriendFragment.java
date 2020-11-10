package com.tourtrek.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tourtrek.R;
import com.tourtrek.activities.MainActivity;
import com.tourtrek.adapters.CurrentTourAttractionsAdapter;
import com.tourtrek.data.Attraction;
import com.tourtrek.data.User;
import com.tourtrek.utilities.Firestore;
import com.tourtrek.utilities.Utilities;
import com.tourtrek.viewModels.AttractionViewModel;
import com.tourtrek.viewModels.TourViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class AddFriendFragment extends Fragment {

    private static final String TAG = "AddFriendFragment";


    /**
     * Default for proper back button usage
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getParentFragmentManager().popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Grab a reference to the current view
        View addFriendView = inflater.inflate(R.layout.fragment_add_friend, container, false);

        // set up the action to carry out via the search button
        setupSearchButton(addFriendView);

        return addFriendView;
    }

    private void setupSearchButton(View addFriendView) {


        final EditText emailEditText = addFriendView.findViewById(R.id.add_friend_email_et);
        Button searchButton = addFriendView.findViewById(R.id.add_friend_search_btn);
        final ProgressBar loadingProgressBar = addFriendView.findViewById(R.id.add_friend_loading_pb);
        final TextView errorTextView = addFriendView.findViewById(R.id.add_friend_error_tv);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Close keyboard
                Utilities.hideKeyboard(requireActivity());

                // Start loading the progress circle
                loadingProgressBar.setVisibility(View.VISIBLE);

                // Grab the information the user entered
                final String email = emailEditText.getText().toString();

                // Check to make sure some input was entered
                if (email.equals("")) {

                    // Show error to user
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText("Please enter your friend's email");

                    // Stop loading progress circle
                    loadingProgressBar.setVisibility(View.GONE);
                }else if(email.equals(MainActivity.user.getEmail())){
                    // Show error to user
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText("You cannot add yourself");

                    // Stop loading progress circle
                    loadingProgressBar.setVisibility(View.GONE);
                } else{

                    // Get instance of firestore
                    final FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Setup collection reference
                    CollectionReference usersCollection = db.collection("Users");

                    // Query database
                    usersCollection
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {

                                if (queryDocumentSnapshots.isEmpty()) {
                                    Log.w(TAG, "No documents found with current email");
                                }
                                else {

                                    // Stop loading progress circle
                                    loadingProgressBar.setVisibility(View.GONE);

                                    // Final result for the query
                                    User friend=null;

                                    // Go through each document and compare the dates
                                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {

                                        // First check that the document belongs to the user
                                        if (document.get("email").equals(email)) {
                                            final DocumentReference friendReference =document.getReference();
                                            friend =new User();
                                            friend.setEmail(email);
                                            friend.setUsername((String) document.get("username"));
                                            friend.setProfileImageURI((String) document.get("profileImageURI"));

                                            final ImageView friendImageView = addFriendView.findViewById(R.id.add_friend_profile_iv);
                                            final TextView friendNameTextView= addFriendView.findViewById(R.id.add_friend_friendName_tv);
                                            final Button addFriendBtn= addFriendView.findViewById(R.id.add_friend_add_btn);

                                            //load profile picture for friend
                                            Glide.with(getContext())
                                                    .load(friend.getProfileImageURI())
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .placeholder(R.drawable.ic_profile_black)
                                                    .into(friendImageView);
                                            friendImageView.setVisibility(View.VISIBLE);

                                            //load username for friend
                                            friendNameTextView.setText(friend.getUsername());
                                            friendNameTextView.setVisibility(View.VISIBLE);

                                           // setupAddFriendButton(addFriendView,friend.getEmail());
                                            addFriendBtn.setOnClickListener(view1 -> {
                                                if(MainActivity.user.getFriends().contains(friendReference)){
                                                    Toast.makeText(getContext(), "Friend already exists", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    MainActivity.user.getFriends().add(friendReference);
                                                    Firestore.updateUser();
                                                    Toast.makeText(getContext(), "Successfully Add Friend", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            addFriendBtn.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    if(friend==null){
                                        errorTextView.setVisibility(View.VISIBLE);
                                        errorTextView.setText("Cannot find user with email entered");
                                    }
                                }
                            });
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).setActionBarTitle("Add Friend");
    }




}
