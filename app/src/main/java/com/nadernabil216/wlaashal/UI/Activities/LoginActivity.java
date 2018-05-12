package com.nadernabil216.wlaashal.UI.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.nadernabil216.wlaashal.R;
import com.nadernabil216.wlaashal.UI.Fragments.SignIn;
import com.nadernabil216.wlaashal.UI.Fragments.UserSignUp;
import com.nadernabil216.wlaashal.Utils.GMethods;

public class LoginActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    SignIn signIn;
    UserSignUp userSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        GMethods.ChangeFont(this);
        fragmentManager = getSupportFragmentManager();
        SwitchToSignIn();
    }

    public void SwitchToSignIn() {
        signIn = new SignIn();
        fragmentManager.beginTransaction().replace(R.id.frame_layout, signIn).commit();
    }

    public void SwitchToSignUp() {
        userSignUp = new UserSignUp();
        fragmentManager.beginTransaction().replace(R.id.frame_layout, userSignUp).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }
}
