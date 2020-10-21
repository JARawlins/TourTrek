package com.tourtrek.utilities;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tourtrek.activities.MainActivity;
import com.tourtrek.data.Attraction;
import com.tourtrek.data.Tour;

import java.util.List;
import java.util.Objects;

public class Firestore {

    private static final String TAG = "Firestore";

    public static void updateUser() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        db.collection("Users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .set(MainActivity.user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User document successfully updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating users document", e);
                    }
                });

    }

    /**
     * Update the user's tours, including their constituent attractions, in Firestore.
     * Note that we iterate through the LOCAL versions stored through the User class.
     * IMPORTANT PRECONDITION: developers add and remove entries from List<DocumentReference>, List<Tour>, and List<Attraction>
     *     to keep them in sync. This ought to be via add and remove methods elsewhere.
     * // TODO include logic to only update entries modified recently
     * // TODO add error-checking with listeners
     */
    public static void updateTours() {
        // firebase access
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // LOCAL version of user tours list
        List<Tour> tours = MainActivity.user.getToursObj();

        // update every tour in the user's list
        for (int i = 0; i < tours.size(); i++) {
            // get the local version of the current tour
            Tour currentTourObj = tours.get(i);
            // get a reference to the current tour in Firestore
            DocumentReference currentTourDoc = MainActivity.user.getLocalToFirebaseMap().get(currentTourObj);
            // get the local version of the attractions in the current local tour
            List<Attraction> currentAttractionObjs = currentTourObj.getAttractionsObj();
            // update every attraction in the current tour
            for (int j = 0; j < currentAttractionObjs.size(); j++){
                Attraction currentAttractionObj = currentAttractionObjs.get(j);
                DocumentReference currentAttractionDoc = currentTourObj.getLocalToFirebase().get(currentAttractionObj);
                // database update
                if (currentAttractionObj.getLastModified().getSeconds() - Timestamp.now().getSeconds() < -5){
                    currentAttractionDoc.set(currentAttractionObj);
                }
            }
            if (currentTourObj.getLastModified().getSeconds() - Timestamp.now().getSeconds() < -5){
                currentTourDoc.set(currentTourObj);
            }
        }
    }
}

//Attraction currentAttraction = currentAttractionDoc.get().getResult().toObject(Attraction.class);
