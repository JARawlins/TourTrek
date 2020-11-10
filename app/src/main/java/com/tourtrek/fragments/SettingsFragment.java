package com.tourtrek.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.rpc.context.AttributeContext;
import com.tourtrek.R;
import com.tourtrek.data.User;

//extends preferenceFragmentCompact
public class SettingsFragment extends Fragment {
    private FirebaseAuth mAuth;
    private EditText changeName;
    private EditText changeEmail;
    private EditText changePassword1;
    private EditText changePassword2;
    private Button updatePasswordButton;
    private Button updateUsernameButton;
    private boolean nameUpdated = false;
    private boolean EmailUpdated = false;
    private boolean passwordUpdated = false;
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
        View SettingsView = inflater.inflate(R.layout.fragment_settings_screen, container, false);
            return SettingsView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        //setupUpdateSettingsButton(view);

        //create change password button listener
        updatePasswordButton = view.findViewById(R.id.settings_change_password_btn);
        updatePasswordButton.setOnClickListener(v -> {
            showChangePasswordDialog();
        });

        //create change username button listener
        updateUsernameButton = view.findViewById(R.id.settings_change_username_btn);
        updateUsernameButton.setOnClickListener(v -> {
            showChangeUsernameDialog();
        });

    }
    private void showChangeUsernameDialog(){
        //set view to fragment_change_username.xml
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_change_username, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        //get fields
        EditText newUsername = view.findViewById(R.id.profile_change_username_username_et);
        EditText password = view.findViewById(R.id.profile_change_username_password_et);
        Button updateUsernameButton = view.findViewById(R.id.profile_update_username_btn);

        //create update username button listener
        updateUsernameButton.setOnClickListener(v -> {
            updateUsername(newUsername.getText().toString(),password.getText().toString(), dialog);
        });
    }

    private void updateUsername(String newUsername, String oldPassword, AlertDialog dialog) {
        FirebaseUser user = mAuth.getCurrentUser();

        //ReAuthenticate the user
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(newUsername)
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getActivity(),
                                                "Username changed successfully", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(),
                                        "Failed to change username. Please try again ", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Enter correct password", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showChangePasswordDialog(){

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_change_password, null);
        //Get elements
        EditText passwordEt0 = view.findViewById(R.id.profile_change_password0_et);
        EditText passwordEt1 = view.findViewById(R.id.profile_change_password1_et);
        EditText passwordEt2 = view.findViewById(R.id.profile_change_password2_et);
        Button updatePasswordButton = view.findViewById(R.id.profile_update_password_btn);
        TextView errorTextView = view.findViewById(R.id.change_password_error_tv);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String password0 = passwordEt0.getText().toString();
                String password1 = passwordEt1.getText().toString();
                String password2 = passwordEt2.getText().toString();

                //validate the password
                if (password1 == null || password2 == null) {
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText("Password fields are empty");
                } else if (!password1.equals(password2)) {
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText("Passwords do not match");
                } else if (password1.equals(password2) && password1.length() < 6) {
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText("New passwords should be at least 6 characters");
                } else {
                    updatePassword(password0,password1,errorTextView,dialog);
                }

            }
        });
    }

    private void updatePassword(String oldPassword, String newPassword, TextView errorTextView, AlertDialog dialog){
        FirebaseUser user = mAuth.getCurrentUser();

        //ReAuthenticate the user
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        user.updatePassword(newPassword)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getActivity(),
                                                "Password changed successfully", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        errorTextView.setVisibility(View.VISIBLE);
                                        errorTextView.setText("" + e.getMessage());
                                        Toast.makeText(getActivity(),
                                                "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Enter correct password", Toast.LENGTH_SHORT).show();
                    }
                });
    }


//    private void setupUpdateSettingsButton(View view){
//
//        //set up fields
//        changeName = view.findViewById(R.id.settings_new_username_et);
//        changeEmail = view.findViewById(R.id.settings_new_email_et);
//        changePassword1 = view.findViewById(R.id.settings_new_password_et);
//        changePassword2 = view.findViewById(R.id.settings_confirm_new_password_et);
//        updateSettingsButton = view.findViewById(R.id.settings_update_settings_btn);
//
//        // start click listener
//        updateSettingsButton.setOnClickListener(v -> {
//
//            //assign text fields to variables
//            String newName = changeName.getText().toString();
//            String newEmail = changeEmail.getText().toString();
//            String newPassword1 = changePassword1.getText().toString();
//            String newPassword2 = changePassword2.getText().toString();
//
//            //check to see if newName is not empty
//            if(newName.trim() != ""){
//                //mAuth.getCurrentUser().updateProfile({username: newName});
//                //User user = mAuth.getCurrentUser();
//
//                nameUpdated = true;
//            }
//
//            //check to see if newEmail is not empty
//            if(newEmail.trim() != ""){
//                //todo: update email in firebase
//                EmailUpdated = true;
//            }
//
//            //check to see if newPassword1 matches newPassword2 and that they have content that is at least  x chars long
//            // also make sure any other requirements are met
//            if(newPassword1.trim()  != ""){
//                //todo: update password in firebase
//                nameUpdated = true;
//            }
//
//
//
//        });
//
//    }
}
