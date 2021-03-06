package com.tourtrek.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.google.firebase.firestore.Query;
import com.tourtrek.R;
import com.tourtrek.activities.MainActivity;
import com.tourtrek.adapters.FriendsAdapter;
import com.tourtrek.data.Tour;
import com.tourtrek.data.User;
import com.tourtrek.utilities.Firestore;
import com.tourtrek.utilities.ItemClickSupport;
import com.tourtrek.utilities.Utilities;
import com.tourtrek.viewModels.FriendViewModel;
import com.tourtrek.viewModels.TourViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class AddFriendToTourFragment extends Fragment {

    private static final String TAG = "AddFriendToTourFragment";

    private FriendViewModel friendViewModel;
    private TourViewModel tourViewModel;
    private DocumentReference tour;
    private EditText emailEditText;
    private Button searchButton;
    private ProgressBar loadingProgressBar;
    private TextView errorTextView;
    private ImageView friendImageView;
    private TextView friendNameTextView;
    private Button addFriendButton;
    private User friend;
    private String friendID;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        View FriendSearchView = inflater.inflate(R.layout.fragment_add_friend_to_tour, container, false);

        // Initialize view model
        friendViewModel = new ViewModelProvider(requireActivity()).get(FriendViewModel.class);
        tourViewModel = new ViewModelProvider(requireActivity()).get(TourViewModel.class);

        //set up fields
        emailEditText = FriendSearchView.findViewById(R.id.add_friend_to_tour_email_et);
        searchButton = FriendSearchView.findViewById(R.id.add_friend_to_tour_search_btn);
        loadingProgressBar = FriendSearchView.findViewById(R.id.add_friend_to_tour_loading_pb);
        errorTextView = FriendSearchView.findViewById(R.id.add_friend_to_tour_error_tv);
        friendImageView = FriendSearchView.findViewById(R.id.add_friend_to_tour_profile_iv);
        friendNameTextView = FriendSearchView.findViewById(R.id.add_friend_to_tour_friendName_tv);
        addFriendButton = FriendSearchView.findViewById(R.id.add_friend_to_tour_add_btn);

        // set up the action to carry out via the search button
        setupSearchButton(FriendSearchView);
        setupAddButton(FriendSearchView);

        return FriendSearchView;
    }

    @Override
    public void onDestroyView() {

        tourViewModel.setReturnedFromAddFriendToTour(false);

        super.onDestroyView();
    }

    private void setupAddButton(View friendSearchView) {
        addFriendButton.setOnClickListener(view1 -> {
            friend.addTourToTours(tour);
            Firestore.updateUser(friend,friendID);
            //addTour(friend,tour);

            Toast.makeText(getContext(), "Successfully added friend to tour", Toast.LENGTH_SHORT).show();

        });
    }
    private void addTour(User user, DocumentReference tour){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

    }

    private void setupSearchButton(View addFriendView) {
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

                    // Setup collection references
                    CollectionReference usersCollection = db.collection("Users");

                    // Query database
                    usersCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {

                                if (queryDocumentSnapshots.isEmpty()) {
                                    Log.w(TAG, "No documents found with current email");
                                }
                                else {

                                    // Stop loading progress circle
                                    loadingProgressBar.setVisibility(View.GONE);

                                    Boolean exists = false;

                                    // Go through each document and compare the dates
                                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {

                                        if (document.get("email").equals(email) && MainActivity.user.getFriends().contains(document.getReference())) {
                                            errorTextView.setVisibility(View.GONE);
                                            exists = true;

                                            friendID = document.getId();
                                            friend = document.toObject(User.class);
                                            friendViewModel.setSelectedFriend(friend);



                                            //load profile picture for friend
                                            Glide.with(getContext())
                                                    .load(friend.getProfileImageURI())
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .placeholder(R.drawable.ic_profile)
                                                    .into(friendImageView);
                                            friendImageView.setVisibility(View.VISIBLE);

                                            //load username for friend
                                            friendNameTextView.setText(friend.getUsername());
                                            friendNameTextView.setVisibility(View.VISIBLE);

                                            addFriendButton.setVisibility(View.VISIBLE);
                                            //get document reference variable for current tour
                                            tour = db.collection("Tours").document(tourViewModel.getSelectedTour().getTourUID());


                                        }
                                    }
                                    if(!exists){
                                        friendNameTextView.setVisibility(View.GONE);
                                        addFriendButton.setVisibility(View.GONE);
                                        friendImageView.setVisibility(View.GONE);
                                        errorTextView.setVisibility(View.VISIBLE);
                                        errorTextView.setText("Cannot find user with email entered on friends list");
                                    }
                                }
                            });
                }
            }
        });
    }
}
