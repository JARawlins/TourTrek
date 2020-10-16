package com.tourtrek.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class LoginFragment extends Fragment {

    private static String TAG = "LoginFragment";
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Do nothing because there is no user logged in
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        setupLoginButton(view);

        setupRegisterButton(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View loginFragmentView = inflater.inflate(R.layout.fragment_login, container, false);

        return loginFragmentView;

    }

    private void setupLoginButton(View view) {

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

                                                                // Go back to the profile screen
                                                                NavHostFragment.findNavController(LoginFragment.this).popBackStack();

                                                                // Stop loading progress circle
                                                                getActivity().findViewById(R.id.login_loading_pb).setVisibility(View.GONE);
                                                            }

                                                            // This means the user does not exist in the database
                                                            else {
                                                                Log.w(TAG, "firestore:failure - unable to find user in firestore");

                                                                // Stop loading progress circle
                                                                getActivity().findViewById(R.id.login_loading_pb).setVisibility(View.GONE);
                                                            }
                                                        }
                                                    }
                                                });



                                    } else {
                                        // If sign in fails, display a message to the user.
                                        errorTextView.setVisibility(View.VISIBLE);
                                        errorTextView.setText(Objects.requireNonNull(task.getException()).getLocalizedMessage());

                                        Log.w(TAG, "signInWithEmail:failure - " + task.getException().getMessage());

                                        // Stop loading progress circle
                                        getActivity().findViewById(R.id.login_loading_pb).setVisibility(View.GONE);

                                        emailEditText.requestFocus();
                                    }
                                }
                            });
                }
            }
        });

    }

    private void setupRegisterButton(View view) {

        Button registerButton = view.findViewById(R.id.login_register_btn);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, new RegisterFragment(), "RegisterFragment");
                ft.addToBackStack("RegisterFragment").commit();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle("Login");
    }
}