
        // navigation should be available for every attraction in the database
        if (attractionViewModel.getSelectedAttraction().getAttractionUID() != null){
            navigationAttractionButton.setVisibility((View.VISIBLE));
        }

        // enables updating an attraction when it is part of a tour owned by the user and when it is a new attraction
        if (tourViewModel.isUserOwned() || attractionViewModel.isNewAttraction()){
            nameEditText.setEnabled(true);
            locationEditText.setEnabled(true);
            costEditText.setEnabled(true);
            startDateButton.setEnabled(true);
            startTimeButton.setEnabled(true);
            endDateButton.setEnabled(true);
            endTimeButton.setEnabled(true);
            coverImageView.setClickable(true);
            coverTextView.setVisibility(View.VISIBLE);
            buttonsContainer.setVisibility(View.VISIBLE);

            // to enable deletion of attractions selected from the tour's recycler view
            if (attractionViewModel.getSelectedAttraction().getAttractionUID() != null){
                deleteAttractionButton.setVisibility((View.VISIBLE));
            }

            coverImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    int PICK_IMAGE = 1;
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                ((MainActivity)requireActivity()).disableTabs();
                loading = true;

                Place place = Autocomplete.getPlaceFromIntent(data);

                attractionViewModel.getSelectedAttraction().setCoverImageURI("");
                weatherTextView.setText("N/A");

                if (place.getPhotoMetadatas() != null) {
                    PhotoMetadata photoMetadata = place.getPhotoMetadatas().get(0);
                    String attributes = photoMetadata.getAttributions();

                    FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata).build();

                    PlacesClient placesClient = Places.createClient(requireContext());

                    LinearLayout loadingContainer = getActivity().findViewById(R.id.attraction_cover_loading_container);
                    loadingContainer.setVisibility(View.VISIBLE);

                    placesClient.fetchPhoto(photoRequest)
                            .addOnSuccessListener(new OnSuccessListener<FetchPhotoResponse>() {
                                @Override
                                public void onSuccess(FetchPhotoResponse fetchPhotoResponse) {

                                    Bitmap bitmap = fetchPhotoResponse.getBitmap();

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] data = baos.toByteArray();

                                    // Load image into view
                                    Glide.with(requireContext())
                                            .load(data)
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .placeholder(R.drawable.default_image)
                                            .into(coverImageView);

                                    loadingContainer.setVisibility(View.GONE);

                                    ((MainActivity)requireActivity()).enableTabs();
                                    loading = false;

                                    // Upload Image to firestore storage
                                    final FirebaseStorage storage = FirebaseStorage.getInstance();

                                    final UUID imageUUID = UUID.randomUUID();

                                    final StorageReference storageReference = storage.getReference().child("AttractionCoverPictures/" + imageUUID);

                                    final UploadTask uploadTask = storageReference.putBytes(data);

                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            storage.getReference().child("AttractionCoverPictures/" + imageUUID).getDownloadUrl()
                                                    .addOnSuccessListener(uri -> {
                                                        attractionViewModel.getSelectedAttraction().setCoverImageURI(uri.toString());

                                                        Log.i(TAG, "Successfully loaded cover image");

                                                    });
                                        }
                                    })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Failed to upload attraction cover image from Places API");
                                                    ((MainActivity)requireActivity()).enableTabs();
                                                    loading = false;
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Failed to fetch attraction photo from Places API");
                                    ((MainActivity)requireActivity()).enableTabs();
                                    loading = false;
                                }
                            });
                }
                else {

                    ((MainActivity)requireActivity()).enableTabs();
                    loading = false;

                    // Load image into view
                    Glide.with(requireContext())
                            .load(R.drawable.default_image)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(coverImageView);
                }

                attractionViewModel.setReturnedFromSearch(true);

                attractionViewModel.getSelectedAttraction().setName(place.getName());
                attractionViewModel.getSelectedAttraction().setLocation(place.getAddress());
                attractionViewModel.getSelectedAttraction().setLat(Objects.requireNonNull(place.getLatLng()).latitude);
                attractionViewModel.getSelectedAttraction().setLon(Objects.requireNonNull(place.getLatLng()).longitude);

                // Get updated weather
                Weather.getWeather(attractionViewModel.getSelectedAttraction().getLat(), attractionViewModel.getSelectedAttraction().getLon(), getContext());

                // get temperature
                // Wait for the weather api to receive the data
                if (attractionViewModel.getSelectedAttraction().getWeather() != null && attractionViewModel.getSelectedAttraction().getStartDate() != null) {

                    for (Map.Entry<String, String> entry : attractionViewModel.getSelectedAttraction().getWeather().entrySet()) {
                        String aDateString = entry.getKey();

                        java.text.DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");

                        Calendar calendar = Calendar.getInstance();

                        try {
                            Date aDate = formatter.parse(aDateString);
                            calendar.setTime(aDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Log.e(TAG, "Error converting string date");
                        }

                        String temperature = entry.getValue();

                        int aMonth = calendar.get(Calendar.MONTH);
                        int aDay = calendar.get(Calendar.DAY_OF_MONTH);
                        int aYear = calendar.get(Calendar.YEAR);

                        Calendar newCalendar = Calendar.getInstance();

                        newCalendar.setTime(attractionViewModel.getSelectedAttraction().getStartDate());

                        int startMonth = newCalendar.get(Calendar.MONTH);
                        int startDay = newCalendar.get(Calendar.DAY_OF_MONTH);
                        int startYear = newCalendar.get(Calendar.YEAR);

                        if (aMonth == startMonth && aDay == startDay && aYear == startYear) {
                            weatherTextView.setText(String.format("%s ℉", temperature));
                            break;
                        }
                        else
                            weatherTextView.setText("N/A");
                    }
                }
            }
            else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                ((MainActivity)requireActivity()).enableTabs();
                loading = false;
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                // Do Nothing because the user is exiting intent
            }
        }
        else {
            if(resultCode == Activity.RESULT_OK) {
                assert data != null;

                Glide.with(this)
                        .load(data.getData())
                        .placeholder(R.drawable.default_image)
                        .into(coverImageView);
                uploadImageToDatabase(data);
            }
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

        final StorageReference storageReference = storage.getReference().child("AttractionCoverPictures/" + imageUUID);

        final UploadTask uploadTask = storageReference.putFile(selectedImage);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(exception -> Log.e(TAG, "Error adding image: " + imageUUID + " to cloud storage"))
                .addOnSuccessListener(taskSnapshot -> {
                    Log.i(TAG, "Successfully added image: " + imageUUID + " to cloud storage");

                    storage.getReference().child("AttractionCoverPictures/" + imageUUID).getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                attractionViewModel.getSelectedAttraction().setCoverImageURI(uri.toString());
                            })
                            .addOnFailureListener(exception -> {
                                Log.e(TAG, "Error retrieving uri for image: " + imageUUID + " in cloud storage, " + exception.getMessage());
                            });
                });
    }

    /**
     * This methods is usable for both adding a new attraction and updating an existing attraction
     * @param view
     */
    private void setupUpdateAttractionButton(View view){

        updateAttractionButton.setOnClickListener(v -> {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            // first get the information from each EditText
            String name = nameEditText.getText().toString();
            String location = locationEditText.getText().toString();
            String cost = costEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String startDate = startDateButton.getText().toString();
            String startTime = startTimeButton.getText().toString();
            String endDate = endDateButton.getText().toString();
            String endTime = endTimeButton.getText().toString();

            // error-handling of dates
            try {
                Date start = simpleDateFormat.parse(startDate);
                Date end = simpleDateFormat.parse(endDate);
                if (end.compareTo(start) < 0){
                    Toast.makeText(getContext(), "Start dates must be before end dates!", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (name.equals("") ||
                    location.equals("") ||
                    cost.equals("") ||
                    startDate.equals("") ||
                    startTime.equals("") ||
                    endDate.equals("") ||
                    endTime.equals("") ||
                    description.equals("")) {
                Toast.makeText(getContext(), "Not all fields entered", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                attractionViewModel.getSelectedAttraction().setStartDateFromString(startDate);
            } catch (ParseException e) {
                Log.e(TAG, "Error converting endDate to a firebase Timestamp");
                e.printStackTrace();
            }

            // parse dates to firebase format
            try {
                attractionViewModel.getSelectedAttraction().setEndDateFromString(endDate);
            } catch (ParseException e) {
                Log.e(TAG, "Error converting startDate to a firebase Timestamp");
                return;
            }

            attractionViewModel.getSelectedAttraction().setName(name);
            attractionViewModel.getSelectedAttraction().setLocation(location);
            attractionViewModel.getSelectedAttraction().setDescription(description);
            attractionViewModel.getSelectedAttraction().setStartTime(startTime);
            attractionViewModel.getSelectedAttraction().setEndTime(endTime);

            // Remove $ from cost
            if (cost.startsWith("$"))
                attractionViewModel.getSelectedAttraction().setCost(Float.parseFloat(cost.substring(1)));
            else
                attractionViewModel.getSelectedAttraction().setCost(Float.parseFloat(cost));

            // Get Firestore instance
            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Create a new attraction in the firestore if it doesn't exist
            if (attractionViewModel.isNewAttraction()) {
                final DocumentReference attractionDocumentReference = db.collection("Attractions").document();
                attractionViewModel.getSelectedAttraction().setAttractionUID(attractionDocumentReference.getId());
                tourViewModel.getSelectedTour().addAttraction(attractionDocumentReference);
            }

            ((MainActivity)requireActivity()).disableTabs();
            loading = true;

            db.collection("Attractions")
                    .document(attractionViewModel.getSelectedAttraction().getAttractionUID())
                    .set(attractionViewModel.getSelectedAttraction())
                    .addOnCompleteListener(task -> {
                        Log.d(TAG, "Attraction written to firestore with UID: " + attractionViewModel.getSelectedAttraction().getAttractionUID());

                        // Add/Update attraction to the selected tour
                        db.collection("Tours").document(tourViewModel.getSelectedTour().getTourUID()).update("attractions", tourViewModel.getSelectedTour().getAttractions());

                        if (attractionViewModel.isNewAttraction()) {
                            Toast.makeText(getContext(), "Successfully Added Attraction", Toast.LENGTH_SHORT).show();
                            getParentFragmentManager().popBackStack();
                        }
                        else {
                            Toast.makeText(getContext(), "Successfully Updated Attraction", Toast.LENGTH_SHORT).show();
                        }

                        ((MainActivity)requireActivity()).enableTabs();
                        loading = false;
                    })
                    .addOnFailureListener(e -> {
                        ((MainActivity)requireActivity()).enableTabs();
                        loading = false;
                        Log.w(TAG, "Error writing document");
                    });
        });
    }

    /**
     * Upon clicking the delete button, the current attraction is removed from the current tour view model
     * and the user is returned to the current tour fragment.
     * A toast message is shown marking successful deletion.
     *
     * Precondition: the attraction has been formally added and has a UID
     * @param view
     */
    private void setupDeleteAttractionButton(View view) {

        deleteAttractionButton.setOnClickListener(v -> {

            String currentAttractionUID = attractionViewModel.getSelectedAttraction().getAttractionUID();
            List<DocumentReference> attractionRefs = tourViewModel.getSelectedTour().getAttractions();
            int originalSize = attractionRefs.size();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // search through the tour view model's list of attractions and delete the one corresponding to the current attraction based on UID
            for (int i = 0; i < originalSize; i++){
                if (attractionRefs.get(i).getId().equals(currentAttractionUID)){
                    tourViewModel.getSelectedTour().getAttractions().remove(i);
                    break;
                }
            }

            // remove the attraction from the database
            db.collection("Attractions").document(currentAttractionUID).delete()
                    .addOnCompleteListener(task -> {

                        Toast.makeText(getContext(), "Attraction Deleted", Toast.LENGTH_SHORT).show();

                        attractionViewModel.setSelectedAttraction(null);
                        attractionViewModel.setIsNewAttraction(null);

                        // update the tour
                        updateTourWithDeletion(db);

                        // go back
                        getParentFragmentManager().popBackStack();
                    })
                    .addOnFailureListener(task2 -> {
                        Toast.makeText(getContext(), "Error Deleting Attraction", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    /**
     * Helper method for updating the current tour in the DB when the user deletes an attraction to leave no dangling references
     * Updating the current tour in the DB to eliminate the deleted attraction's reference immediately is necessary
     * attraction addition and updating immediately write to the DB without tapping the update tour button.
     * Precondition: not a new tour
     */
    private void updateTourWithDeletion(FirebaseFirestore db){
                db.collection("Tours").document(tourViewModel.getSelectedTour().getTourUID())
                        .set(tourViewModel.getSelectedTour())
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "Tour written to Firestore");
                        })
                        .addOnFailureListener(e -> Log.w(TAG, "Error writing tour document"));
    }

    public void startAutoCompleteActivity(View view) {
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN,
                Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS,
                        Place.Field.PHOTO_METADATAS, Place.Field.LAT_LNG))
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .build(requireContext());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    public void updateCoverImage() {
        Glide.with(getContext())
                .load(attractionViewModel.getSelectedAttraction().getCoverImageURI())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.default_image)
                .into(coverImageView);
    }

}
