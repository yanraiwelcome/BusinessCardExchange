package com.project.businesscardexchange.fragments;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.project.businesscardexchange.R;

public class ProfileActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Create Business Card");

        FragmentManager fragmentManage = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManage.beginTransaction();
        fragmentTransaction.add(R.id.profile_activity_container,new ProfileFragment(),"PROFILE_FRAGMENT");
        fragmentTransaction.commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ProfileActivity.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
