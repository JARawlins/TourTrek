package com.tourtrek.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tourtrek.R;
import com.tourtrek.activities.MainActivity;
import com.tourtrek.adapters.FriendsAdapter;
import com.tourtrek.adapters.FriendsOfFriendsAdapter;
import com.tourtrek.data.User;
import com.tourtrek.viewModels.FriendViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class FriendProfileFragment extends Fragment {

    private static final String TAG = "AddFriendFragment";
    private RecyclerView friendsRecyclerView;
    private SwipeRefreshLayout friendsSwipeRefreshLayout;

    private SwipeRefreshLayout friendsOfFriendsSwipeRefreshLayout;
    private RecyclerView toursOfFriendsRecyclerView;
    private SwipeRefreshLayout toursOfFriendsSwipeRefreshLayout;
    private FriendsOfFriendsAdapter friendsListAdapter;

    private FriendViewModel friendViewModel;

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
        View FriendProfileView = inflater.inflate(R.layout.fragment_friend, container, false);

        // Initialize view model
        friendViewModel = new ViewModelProvider(requireActivity()).get(FriendViewModel.class);

        //Set Username
        TextView usernameTextView = FriendProfileView.findViewById(R.id.profile_username_tv);
        usernameTextView.setText(friendViewModel.getSelectedFriend().getUsername());

        //Set Profile Picture
        ImageView profileUserImageView = FriendProfileView.findViewById(R.id.profile_user_iv);
        Glide.with(getContext())
                .load(friendViewModel.getSelectedFriend().getProfileImageURI())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_profile)
                .circleCrop()
                .into(profileUserImageView);

        //populate friends list of user
        friendsRecyclerView = FriendProfileView.findViewById(R.id.friend_friends_rv);
        configureRecyclerViews(friendsRecyclerView);
        configureSwipeRefreshLayouts(FriendProfileView);

        return FriendProfileView;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).setActionBarTitle("Add Friend");
    }


    /**
     * Configure the recycler view
     *
     * @param RV current recycler view needed
     */
    public void configureRecyclerViews(RecyclerView RV ) {

        // Improves performance because content does not change size
        RV.setHasFixedSize(true);

        // Only load 10 friends before loading more
        RV.setItemViewCacheSize(10);

        // Enable drawing cache
        RV.setDrawingCacheEnabled(true);
        RV.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        // User linear layout manager
        RecyclerView.LayoutManager friendsLayoutManager = new LinearLayoutManager(getContext());
        RV.setLayoutManager(friendsLayoutManager);

        // Get all friends
        fetchFriendUsersAsync();

        // Specify an adapter
        friendsListAdapter = new FriendsOfFriendsAdapter(getContext());
        RV.setAdapter(friendsListAdapter);

        // Stop showing progressBar when items are loaded
        RV
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(() -> ((FriendsOfFriendsAdapter) friendsListAdapter).stopLoading());


    }

    /**
     * Configure the swipe down to refresh function of our recycler view
     *
     * @param view current view
     */
    public void configureSwipeRefreshLayouts(View view) {


        friendsSwipeRefreshLayout = view.findViewById(R.id.friend_friends_srl);
        friendsSwipeRefreshLayout.setOnRefreshListener(() -> fetchFriendUsersAsync());

    }




    /**
     * Retrieve all friends belonging to the friend selected
     *
     */
    private void fetchFriendUsersAsync() {

        // Get instance of firestore
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Setup collection reference
        CollectionReference usersCollection = db.collection("Users");

        // Pull out the UID's of each friend that belongs to this user
        List<String> usersUIDs = new ArrayList<>();
        if (!friendViewModel.getSelectedFriend().getFriends().isEmpty()) {
            for (DocumentReference documentReference : friendViewModel.getSelectedFriend().getFriends()) {
                usersUIDs.add(documentReference.getId());
            }
        }

        // Query database
        usersCollection
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (queryDocumentSnapshots.isEmpty()) {
                        Log.w(TAG, "No documents found in the Users collection");
                    }
                    else {

                        // Final list of friends for this category
                        List<User> usersFriends = new ArrayList<>();

                        // Go through each document and compare the user IDs
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {

                            // First check that the document belongs to the user
                            if (usersUIDs.contains(document.getId())) {

                                usersFriends.add(document.toObject(User.class));

                            }
                        }
                        ((FriendsOfFriendsAdapter) friendsListAdapter).clear();
                        ((FriendsOfFriendsAdapter) friendsListAdapter).addAll(usersFriends);

                        friendsSwipeRefreshLayout.setRefreshing(false);

                    }
                });
    }

}
