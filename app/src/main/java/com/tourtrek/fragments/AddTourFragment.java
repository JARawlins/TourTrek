package com.tourtrek.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tourtrek.R;
import com.tourtrek.activities.MainActivity;
import com.tourtrek.adapters.EditTourAttractionsAdapter;
import com.tourtrek.data.Attraction;
import com.tourtrek.data.Tour;
import com.tourtrek.utilities.Firestore;
import com.tourtrek.utilities.Utilities;
import com.tourtrek.viewModels.TourViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class AddTourFragment extends Fragment {

    private static final String TAG = "AddTourFragment";
    private TourViewModel tourViewModel;
    private EditTourAttractionsAdapter attractionsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView coverImageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Pop the last fragment off the stack
                getActivity().getSupportFragmentManager().popBackStack();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View addTourView = inflater.inflate(R.layout.fragment_edit_tour2, container, false);

        // Initialize view model
        tourViewModel = new ViewModelProvider(getActivity()).get(TourViewModel.class);

        // Set selected tour as a new tour
        tourViewModel.setSelectedTour(new Tour());

        // Set profile picture
        coverImageView = addTourView.findViewById(R.id.edit_tour_2_cover_iv);

        // If user clicks tour cover image, they can change it
        coverImageView.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            int PICK_IMAGE = 1;
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        });

        // set up the recycler view of attractions
        configureRecyclerViews(addTourView);
        configureSwipeRefreshLayouts(addTourView);

        setUpAddAttractionButton(addTourView);
        setUpEditTourSaveButton(addTourView);

        return addTourView;
    }

    private void setUpEditTourSaveButton(View addTourView) {

        Button editTourSaveButton = addTourView.findViewById(R.id.edit_tour_2_save_bt);

        editTourSaveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                // Close keyboard
                Utilities.hideKeyboard(getActivity());

                // Instantiate all fields in add Tour
                final EditText tourNameEditText = addTourView.findViewById(R.id.edit_tour_2_tour_name_ct);
                final CheckBox publicCheckBox = addTourView.findViewById(R.id.edit_tour_2_public_cb);
                final CheckBox notificationCheckBox = addTourView.findViewById(R.id.edit_tour_2_notifications_cb);
                final EditText startDateEditText = addTourView.findViewById(R.id.edit_tour_2_startDate_ct);
                final EditText locationEditText = addTourView.findViewById(R.id.edit_tour_2_tour_location_ct);
                final EditText lengthEditText = addTourView.findViewById(R.id.edit_tour_2_length_ct);

                // Check that all fields have been entered
                if (tourNameEditText.getText().toString().equals("") || locationEditText.getText().toString().equals("")
                        ||lengthEditText.getText().toString().equals("")||startDateEditText.getText().toString().equals("")) {
                    Toast.makeText(getContext(),"Not All Fields entered",Toast.LENGTH_SHORT).show();
                }
                else {

                    String tourName = tourNameEditText.getText().toString();
                    String tourLocation = locationEditText.getText().toString();
                    boolean isPublic = publicCheckBox.isChecked();
                    boolean notificationsAreOn = notificationCheckBox.isChecked();
                    Long tourLength = Long.parseLong(lengthEditText.getText().toString());
                    String startDateString = startDateEditText.getText().toString();

                    try {

                        // Format date
                        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        Date date = (Date) formatter.parse(startDateString);
                        final Timestamp startDate = new Timestamp(date);

                        tourViewModel.getSelectedTour().setName(tourName);
                        tourViewModel.getSelectedTour().setLocation(tourLocation);
                        tourViewModel.getSelectedTour().setLength(tourLength);
                        tourViewModel.getSelectedTour().setNotifications(notificationsAreOn);
                        tourViewModel.getSelectedTour().setPubliclyAvailable(isPublic);
                        tourViewModel.getSelectedTour().setStartDate(startDate);

                        final FirebaseFirestore db = FirebaseFirestore.getInstance();

                        DocumentReference tourRef = db.collection("Tours").document();

                        tourViewModel.getSelectedTour().setTourUID(tourRef.getId());

                        // Add new tour to firestore
                        db.collection("Tours").document(tourViewModel.getSelectedTour().getTourUID())
                                .set(tourViewModel.getSelectedTour()).addOnSuccessListener(w -> {

                                    // Add the tour to the user
                                    MainActivity.user.addTourToTours(tourRef);

                                    // Update the user in the firestore
                                    Firestore.updateUser();

                                    getActivity().getSupportFragmentManager().popBackStack();

                        }).addOnFailureListener(a -> {
                            Log.w(TAG, "Error saving hive to firestore");
                        });

                    } catch (ParseException e) {
                        Log.w(TAG, "Error parsing date in new tour");
                    }
                }
            }
        });
    }


    public void setUpAddAttractionButton(View view) {

        // Create a button which directs to addAttractionFragment when pressed
        Button tour_attractions_btn = view.findViewById(R.id.edit_tour_2_add_attraction_bt);

        // When the button is clicked, switch to the AddAttractionFragment
        tour_attractions_btn.setOnClickListener(v -> {

            final FragmentTransaction ft = getParentFragmentManager().beginTransaction();

            ft.replace(R.id.nav_host_fragment, new AddAttractionFragment(), "AddAttractionFragment");

            ft.addToBackStack("AddAttractionFragment").commit();

        });
    }


    /**
     * Configure the recycler view
     *
     * @param view current view
     */
    public void configureRecyclerViews(View view) {

        // Get our recycler view from the layout
        RecyclerView attractionsView = view.findViewById(R.id.edit_tour_2_attractions_rv);

        // Improves performance because content does not change size
        attractionsView.setHasFixedSize(true);

        // Only load 10 tours before loading more
        attractionsView.setItemViewCacheSize(10);

        // Enable drawing cache
        attractionsView.setDrawingCacheEnabled(true);
        attractionsView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        // User linear layout manager
        RecyclerView.LayoutManager attractionsLayoutManager = new LinearLayoutManager(getContext());
        attractionsView.setLayoutManager(attractionsLayoutManager);

        // Specify an adapter
        attractionsAdapter = new EditTourAttractionsAdapter(getContext());

        // Fetch attractions
        fetchAttractionsAsync();

        // set the adapter
        attractionsView.setAdapter(attractionsAdapter);
    }

    /**
     * Retrieve all attractions belonging to this user
     *
     */
    private void fetchAttractionsAsync() {

        // Get instance of firestore
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Setup collection reference
        CollectionReference attractionsCollection = db.collection("Attractions");

        // Pull out the UID's of each tour that belongs to this user
        List<String> usersAttractionUIDs = new ArrayList<>();
        if (!tourViewModel.getSelectedTour().getAttractions().isEmpty()) {
            for (DocumentReference documentReference : tourViewModel.getSelectedTour().getAttractions()) {
                usersAttractionUIDs.add(documentReference.getId());
            }
        }

        // Query database
        attractionsCollection
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (queryDocumentSnapshots.isEmpty()) {
                        Log.w(TAG, "No documents found in the Attractions collection for this user");
                    }
                    else {

                        // Final list of tours for this category
                        List<Attraction> usersAttractions = new ArrayList<>();

                        // Go through each document and compare the dates
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {

                            // First check that the document belongs to the user
                            if (usersAttractionUIDs.contains(document.getId())) {
                                usersAttractions.add(document.toObject(Attraction.class));
                            }
                        }

                        ((EditTourAttractionsAdapter) attractionsAdapter).clear();
                        ((EditTourAttractionsAdapter) attractionsAdapter).addAll(usersAttractions);

                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    /**
     * Configure the swipe down to refresh function of our recycler view
     *
     * @param view current view
     */
    public void configureSwipeRefreshLayouts(View view) {

        // Current
        swipeRefreshLayout = view.findViewById(R.id.edit_tour_2_attractions_srl);
        swipeRefreshLayout.setOnRefreshListener(() -> fetchAttractionsAsync());

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle("Add a Tour");


//        if (tourViewModel.getSelectedTour().getAttractions().size())
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("Attractions").document(tourViewModel.getSelectedTour().getAttractions().get(tourViewModel.getSelectedTour().getAttractions().size() - 1).getId())
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        Attraction attraction = task.getResult().toObject(Attraction.class);
//
//                        ((EditTourAttractionsAdapter) editTourAttractionsAdapter).add(attraction);
//
//                    }
//                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(resultCode == Activity.RESULT_OK) {
            assert imageReturnedIntent != null;

            Glide.with(this)
                    .load(imageReturnedIntent.getData())
                    .placeholder(R.drawable.default_image)
                    .into(coverImageView);

            uploadImageToDatabase(imageReturnedIntent);
        }
    }

    /**
     * Uploads an image to the Profile Images cloud storage.
     *
     * @param imageReturnedIntent intent of the image being saved
     */
    public void uploadImageToDatabase(Intent imageReturnedIntent) {

        final FirebaseStorage storage = FirebaseStorage.getInstance();

        // Uri to the image
        Uri selectedImage = imageReturnedIntent.getData();

        final UUID imageUUID = UUID.randomUUID();

        final StorageReference storageReference = storage.getReference().child("TourCoverPictures/" + imageUUID);

        final UploadTask uploadTask = storageReference.putFile(selectedImage);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(exception -> Log.e(TAG, "Error adding image: " + imageUUID + " to cloud storage"))
                .addOnSuccessListener(taskSnapshot -> {
                    Log.i(TAG, "Successfully added image: " + imageUUID + " to cloud storage");

                    storage.getReference().child("TourCoverPictures/" + imageUUID).getDownloadUrl()
                            .addOnSuccessListener(uri -> {


                                tourViewModel.getSelectedTour().setCoverImageURI(uri.toString());


                            })
                            .addOnFailureListener(exception -> {
                                Log.e(TAG, "Error retrieving uri for image: " + imageUUID + " in cloud storage, " + exception.getMessage());
                            });
                });
    }
}
