package com.tourtrek.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tourtrek.R;
import com.tourtrek.activities.MainActivity;
import com.tourtrek.data.User;
import com.tourtrek.utilities.Utilities;

import java.util.Objects;

public class RegisterFragment extends Fragment {

    private static String TAG = "RegisterFragment";
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View registerFragmentView = inflater.inflate(R.layout.fragment_register, container, false);

        // Initialize firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        // Setup handler for registration button
        setupRegisterButtonHandler(registerFragmentView);

        return registerFragmentView;
    }

    private void setupRegisterButtonHandler(final View view){

        final EditText usernameEditText = view.findViewById(R.id.register_username_et);
        final EditText emailEditText = view.findViewById(R.id.register_email_et);
        final EditText password1EditText = view.findViewById(R.id.register_password1_et);
        final EditText password2EditText = view.findViewById(R.id.register_password2_et);
        Button registerButton = view.findViewById(R.id.register_register_btn);
        final ProgressBar loadingProgressBar = view.findViewById(R.id.register_loading_pb);
        final TextView errorTextView = view.findViewById(R.id.register_error_tv);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Close keyboard
                Utilities.hideKeyboard(getActivity());

                // Start loading the progress circle
                loadingProgressBar.setVisibility(View.VISIBLE);

                // Grab the information the user entered
                final String username = usernameEditText.getText().toString();
                final String email = emailEditText.getText().toString();
                final String password1 = password1EditText.getText().toString();
                final String password2 = password2EditText.getText().toString();

                // Check to make sure some input was entered
                if (username.equals("") || email.equals("") || password1.equals("") || password2.equals("") ) {

                    // Show error to user
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText("Not all fields entered");

                    // Stop loading progress circle
                    loadingProgressBar.setVisibility(View.GONE);
                }
                // Check to make sure the passwords match
                else if (!password1.equals(password2)) {

                    // Show error to user
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText("Passwords do not match");

                    // Stop loading progress circle
                    loadingProgressBar.setVisibility(View.GONE);
                }
                else {

                    mAuth.createUserWithEmailAndPassword(email, password1)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        // Create user
                                        final User user = new User(username, email);

                                        MainActivity.user = user;

                                        final FirebaseFirestore db = FirebaseFirestore.getInstance();

                                        // Create document for user
                                        db.collection("Users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).set(user)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            FragmentManager fm = getParentFragmentManager();

                                                            if (fm.getBackStackEntryAt(0).getName().equals("ToursFragment")) {
                                                                final FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                                                                ft.replace(R.id.nav_host_fragment, new ToursFragment(), "ToursFragment");
                                                                ft.commit();
                                                            }
                                                            else if (fm.getBackStackEntryAt(0).getName().equals("ProfileFragment")) {
                                                                final FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                                                                ft.replace(R.id.nav_host_fragment, new ProfileFragment(), "ProfileFragment");
                                                                ft.commit();
                                                            }

                                                            // Stop loading progress circle
                                                            loadingProgressBar.setVisibility(View.GONE);
                                                        }
                                                        else {
                                                            Log.i(TAG, "Failed to add user to firestore");
                                                            errorTextView.setVisibility(View.VISIBLE);
                                                            errorTextView.setText("Failed to add user to firestore");
                                                        }
                                                    }
                                                });

                                    } else {

                                        // Stop loading progress circle
                                        loadingProgressBar.setVisibility(View.GONE);

                                        // Display error
                                        errorTextView.setVisibility(View.VISIBLE);
                                        errorTextView.setText(Objects.requireNonNull(task.getException()).getLocalizedMessage());

                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
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
        ((MainActivity) getActivity()).setActionBarTitle("Register");
    }
}