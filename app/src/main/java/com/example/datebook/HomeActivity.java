package com.example.datebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.datebook.adapter.ViewPagerFragmentAdapter;
import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {
    protected ViewPagerFragmentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TabLayout mHomeLayout = findViewById(R.id.tabLayoutHome);
        mHomeLayout.addTab(mHomeLayout.newTab().setText("Matches"));
        mHomeLayout.addTab(mHomeLayout.newTab().setText("Chats"));
        mHomeLayout.addTab(mHomeLayout.newTab().setText("Calls"));
        ViewPager viewPager2 = findViewById(R.id.viewPagerHome);

        adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager());
        viewPager2.setAdapter(adapter);
        viewPager2.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mHomeLayout));
        mHomeLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Spinner spinner = findViewById(R.id.spinner_language_home);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this,
                R.array.language_array,
                android.R.layout.simple_spinner_dropdown_item
        );
        spinner.setAdapter(adapter2);

        ImageView mImageMore = findViewById(R.id.imageViewMore);
        mImageMore.setOnClickListener(view -> {
            Toast.makeText(this, "pressed", Toast.LENGTH_SHORT).show();
        });
    }
}