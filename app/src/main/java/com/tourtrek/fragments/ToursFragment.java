package com.tourtrek.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tourtrek.R;
import com.tourtrek.activities.MainActivity;
import com.tourtrek.data.User;
import com.tourtrek.utilities.Utilities;

import java.util.Objects;

public class ToursFragment extends Fragment {

    private static final String TAG = "ToursFragment";
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Initialize firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            View loginFragmentView = inflater.inflate(R.layout.fragment_login, container, false);

            setupLoginButtonHandler(loginFragmentView);

            setupRegisterButtonHandler(loginFragmentView);

            return loginFragmentView;
        }
        else {
            View toursFragmentView = inflater.inflate(R.layout.fragment_tours, container, false);

            // TODO: This is where we will load the users past, present and future tours

            return toursFragmentView;
        }
    }

    private void setupLoginButtonHandler(final View view){

        Button loginButton = view.findViewById(R.id.login_login_btn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FirebaseFirestore db = FirebaseFirestore.getInstance();

                // Close soft keyboard
                Activity currentActivity = getActivity();
                Utilities.hideKeyboard(currentActivity);

                // Start loading progress circle
                getActivity().findViewById(R.id.login_loading_pb).setVisibility(View.VISIBLE);

                final EditText emailEditText = getActivity().findViewById(R.id.login_email_et);
                final EditText passwordEditText = getActivity().findViewById(R.id.login_password_et);
                final TextView errorTextView = getActivity().findViewById(R.id.login_error_tv);


                final String email = emailEditText.getText().toString();
                final String password = passwordEditText.getText().toString();

                if (emailEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("")) {
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText("Invalid username or password");
                    // Stop loading progress circle
                    getActivity().findViewById(R.id.login_loading_pb).setVisibility(View.GONE);
                }
                else {

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");

                                        // Get the users info from the firestore
                                        db.collection("Users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {

                                                            DocumentSnapshot userDocument = task.getResult();

                                                            if (userDocument != null && userDocument.exists()) {

                                                                MainActivity.user = userDocument.toObject(User.class);

                                                                // Navigate user to the tours fragment
                                                                final FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                                                                ft.replace(R.id.nav_host_fragment, new ToursFragment(), "TourFragment");
                                                                ft.commit();

                                                                // Stop loading progress circle
                                                                getActivity().findViewById(R.id.login_loading_pb).setVisibility(View.GONE);
                                                            }

                                                            // This means the user does not exist in the database
                                                            else {
                                                                Log.w(TAG, "firestore:failure - unable to find user in firestore");
                                                            }
                                                        }
                                                    }
                                                });

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        errorTextView.setVisibility(View.VISIBLE);
                                        errorTextView.setText(Objects.requireNonNull(task.getException()).getLocalizedMessage());
                                        Log.w(TAG, "signInWithEmail:failure - " + task.getException().getMessage());
                                        emailEditText.requestFocus();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void setupRegisterButtonHandler(final View view){

        Button registerButton = view.findViewById(R.id.login_register_btn);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, new RegisterFragment(), "RegisterFragment");
                ft.addToBackStack("ToursFragment").commit();
            }
        });

    }
}