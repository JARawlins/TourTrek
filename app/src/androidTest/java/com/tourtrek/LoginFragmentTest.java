package com.tourtrek;

import android.util.Log;

import com.tourtrek.fragments.LoginFragment;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class LoginFragmentTest {

    @Test
    public void test1(){
        FragmentScenario scenario = FragmentScenario.launch(LoginFragment.class);
        Log.i("TEST", "TESTING");
    }
}
