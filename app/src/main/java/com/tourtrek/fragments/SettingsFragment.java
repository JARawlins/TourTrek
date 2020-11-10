package com.tourtrek.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
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
    private Button updateSettingsButton;
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

        setupUpdateSettingsButton(view);


    }


    private void setupUpdateSettingsButton(View view){

        //set up fields
        changeName = view.findViewById(R.id.settings_new_username_et);
        changeEmail = view.findViewById(R.id.settings_new_email_et);
        changePassword1 = view.findViewById(R.id.settings_new_password_et);
        changePassword2 = view.findViewById(R.id.settings_confirm_new_password_et);
        updateSettingsButton = view.findViewById(R.id.settings_update_settings_btn);

        // start click listener
        updateSettingsButton.setOnClickListener(v -> {

            //assign text fields to variables
            String newName = changeName.getText().toString();
            String newEmail = changeEmail.getText().toString();
            String newPassword1 = changePassword1.getText().toString();
            String newPassword2 = changePassword2.getText().toString();

            //check to see if newName is not empty
            if(newName.trim() != ""){
                //mAuth.getCurrentUser().updateProfile({username: newName});
              ///  User user = mAuth.getCurrentUser();

                nameUpdated = true;
            }

            //check to see if newEmail is not empty
            if(newEmail.trim() != ""){
                //todo: update email in firebase
                EmailUpdated = true;
            }

            //check to see if newPassword1 matches newPassword2 and that they have content that is at least  x chars long
            // also make sure any other requirements are met
            if(newPassword1.trim()  != ""){
                //todo: update password in firebase
                nameUpdated = true;
            }



        });

    }
}
