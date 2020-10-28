package com.tourtrek.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.navigation.fragment.NavHostFragment;

public class FragmentFactoryImpl extends FragmentFactory {

    public FragmentFactoryImpl() {

    }

    @NonNull
    @Override
    public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
        Class<? extends Fragment> clazz = loadFragmentClass(classLoader, className);

        Fragment fragment = null;

        if (clazz == LoginFragment.class) {
            fragment = new LoginFragment();
        }
        else if (clazz == NavHostFragment.class) {
            fragment = new NavHostFragment();
        }
        else if (clazz == PersonalToursFragment.class) {
            fragment = new PersonalToursFragment();
        }
        else {
            return super.instantiate(classLoader, className);
        }

        return fragment;
    }
}
