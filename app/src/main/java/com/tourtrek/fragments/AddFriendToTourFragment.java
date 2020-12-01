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
        View addFriendView = inflater.inflate(R.layout.fragment_add_friend_to_tour, container, false);

        // Initialize view model
        friendViewModel = new ViewModelProvider(requireActivity()).get(FriendViewModel.class);
        tourViewModel = new ViewModelProvider(requireActivity()).get(TourViewModel.class);
        // set up the action to carry out via the search button
        setupSearchButton(addFriendView);

        return addFriendView;
    }

    private void setupSearchButton(View addFriendView) {


        final EditText emailEditText = addFriendView.findViewById(R.id.add_friend_to_tour_email_et);
        Button searchButton = addFriendView.findViewById(R.id.add_friend_to_tour_search_btn);
        final ProgressBar loadingProgressBar = addFriendView.findViewById(R.id.add_friend_to_tour_loading_pb);
        final TextView errorTextView = addFriendView.findViewById(R.id.add_friend_to_tour_error_tv);

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

                    // Add UID of users friends to a list
                    List<String> usersUIDs = new ArrayList<>();
                    if (!MainActivity.user.getFriends().isEmpty()) {
                        for (DocumentReference documentReference : MainActivity.user.getFriends()) {
                            usersUIDs.add(documentReference.getId());
                        }
                    }

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

                                    Boolean exists = false;

                                    // Go through each document and compare the dates
                                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {

                                        // First check that the document belongs to the user
                                        if (document.get("email").equals(email) & MainActivity.user.getFriends().contains(document.getReference())) {
                                            final DocumentReference friendReference = document.getReference();

                                            exists = true;

                                            User friend = document.toObject(User.class);
                                            friendViewModel.setSelectedFriend(friend);

                                            final ImageView friendImageView = addFriendView.findViewById(R.id.add_friend_to_tour_profile_iv);
                                            final TextView friendNameTextView= addFriendView.findViewById(R.id.add_friend_to_tour_friendName_tv);
                                            final Button addFriendBtn= addFriendView.findViewById(R.id.add_friend_to_tour_add_btn);

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

                                            // setupAddFriendButton(addFriendView,friend.getEmail());
                                            //tourViewModel.getSelectedTour().getTourUID() us this to get tour from database and the try create document object to pass to friend.addtourtotour
                                            addFriendBtn.setOnClickListener(view1 -> {
                                               // friend.addTourToTours(tourViewModel.getSelectedTour().getTourUID());

                                                Toast.makeText(getContext(), "Successfully Add Friend", Toast.LENGTH_SHORT).show();

                                            });
                                            addFriendBtn.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            errorTextView.setVisibility(View.VISIBLE);
                                            errorTextView.setText("This user must be on your friends list");
                                        }
                                    }
                                    if(!exists){
                                        errorTextView.setVisibility(View.VISIBLE);
                                        errorTextView.setText("Cannot find user with email entered");
                                    }
                                }
                            });
                }
            }
        });
    }
}
