package com.lifeline.safety.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.*;

import com.lifeline.safety.R;
import com.lifeline.safety.adapters.SafetyCategoryAdapter;
import com.lifeline.safety.models.SafetyCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SafetyGuideActivity extends AppCompatActivity {

    private RecyclerView recyclerSafety;
    private SafetyCategoryAdapter adapter;
    private List<SafetyCategory> allCategories;
    private List<SafetyCategory> currentCategories;

    private TextView tabAll, tabFirstAid, tabFireSafety, tabDisaster;
    private ImageView backButton;
    private ImageView settingsButton;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_guide);

        initializeViews();
        setupCategories();
        setupRecyclerView();
        setupClickListeners();
    }

    private void initializeViews() {
        backButton = findViewById(R.id.backButton);
        settingsButton = findViewById(R.id.settingsButton);
        recyclerSafety = findViewById(R.id.recyclerSafety);
        searchEditText = findViewById(R.id.searchEditText);

        tabAll = findViewById(R.id.tabAll);
        tabFirstAid = findViewById(R.id.tabFirstAid);
        tabFireSafety = findViewById(R.id.tabFireSafety);
        tabDisaster = findViewById(R.id.tabDisaster);
    }

    private void setupCategories() {
        allCategories = new ArrayList<>();

        // CPR Card
        allCategories.add(new SafetyCategory(
                "How to perform CPR",
                "Hands-only technique",
                R.drawable.ic_heart,
                R.drawable.icon_bg_light_red,
                ContextCompat.getColor(this, R.color.alert_red),
                Arrays.asList(
                        "Check for response and breathing. If none, prepare for action.",
                        "Call 911 immediately or ask someone else to do it.",
                        "Push hard and fast in the center of the chest (100-120 bpm).",
                        "Continue until help arrives or person shows signs of life."
                ),
                true
        ));

        // Medical Emergency
        allCategories.add(new SafetyCategory(
                "Medical Emergency",
                "Immediate response steps",
                R.drawable.ic_emergency,
                R.drawable.icon_bg_light_green,
                ContextCompat.getColor(this, R.color.emer_green),
                Arrays.asList(
                        "Call emergency services immediately (911 or local number).",
                        "Do not move the injured person unless absolutely necessary.",
                        "Apply pressure to any bleeding wounds with clean cloth.",
                        "Keep the person conscious and talking if possible.",
                        "Monitor breathing and pulse until help arrives."
                ),
                true
        ));

        // Dealing with a burn
        allCategories.add(new SafetyCategory(
                "Dealing with a burn",
                "Cool water, no ice",
                R.drawable.ic_fire,
                R.drawable.icon_bg_light_orange,
                0xFFF97316,
                Arrays.asList(
                        "Remove from heat source immediately and safely.",
                        "Cool the burn with cool (not cold) running water for 10-20 minutes.",
                        "Do NOT use ice, butter, or ointments on fresh burns.",
                        "Cover with a clean, dry cloth or sterile bandage.",
                        "Seek medical attention for severe burns or burns on face/hands."
                ),
                true
        ));

        // Accident
        allCategories.add(new SafetyCategory(
                "Accident Response",
                "Vehicle or serious injury",
                R.drawable.ic_contacts,
                R.drawable.icon_bg_light_blue,
                ContextCompat.getColor(this, R.color.status_blue),
                Arrays.asList(
                        "Move to a safe location away from traffic if possible.",
                        "Turn off vehicle engine to prevent fire hazard.",
                        "Call emergency services and provide exact location.",
                        "Share your location with emergency contacts.",
                        "Do not remove helmets from motorcyclists.",
                        "Wait for professional medical help to arrive."
                ),
                true
        ));

        // Earthquake Safety
        allCategories.add(new SafetyCategory(
                "Earthquake Safety",
                "Drop, Cover, Hold On",
                R.drawable.ic_earthquake,
                R.drawable.icon_bg_light_blue,
                ContextCompat.getColor(this, R.color.safety_blue),
                Arrays.asList(
                        "DROP to your hands and knees immediately.",
                        "Take COVER under a sturdy desk or table.",
                        "HOLD ON to your shelter until shaking stops.",
                        "Stay away from windows, mirrors, and heavy objects.",
                        "If outdoors, move to an open area away from buildings.",
                        "After shaking stops, check for injuries and hazards."
                ),
                false
        ));

        // Choking Response
        allCategories.add(new SafetyCategory(
                "Choking Response",
                "Heimlich Maneuver",
                R.drawable.ic_choking,
                R.drawable.icon_bg_light_purple,
                ContextCompat.getColor(this, R.color.status_purple),
                Arrays.asList(
                        "Ask 'Are you choking?' If they can't speak, act immediately.",
                        "Stand behind the person and wrap arms around their waist.",
                        "Make a fist and place it above the belly button.",
                        "Grasp fist with other hand and thrust inward and upward.",
                        "Repeat thrusts until object is expelled or person becomes unconscious.",
                        "If unconscious, begin CPR and call 911."
                ),
                true
        ));

        // Severe Bleeding
        allCategories.add(new SafetyCategory(
                "Severe Bleeding",
                "Apply Pressure",
                R.drawable.ic_blood,
                R.drawable.icon_bg_light_red,
                ContextCompat.getColor(this, R.color.alert_red),
                Arrays.asList(
                        "Call 911 immediately for severe bleeding.",
                        "Apply direct pressure with clean cloth or bandage.",
                        "Maintain firm, continuous pressure for at least 10 minutes.",
                        "Do NOT remove the cloth even if blood soaks through - add more layers.",
                        "Elevate the injured area above heart level if possible.",
                        "Keep person warm and calm while waiting for help."
                ),
                true
        ));

        // Harassment / Threat
        allCategories.add(new SafetyCategory(
                "Harassment / Threat",
                "Personal safety response",
                R.drawable.ic_safety,
                R.drawable.icon_bg_light_green,
                ContextCompat.getColor(this, R.color.status_green),
                Arrays.asList(
                        "Trigger SOS alert immediately if you feel unsafe.",
                        "Move to a crowded, well-lit public place.",
                        "Stay on call with a trusted contact or emergency services.",
                        "Trust your instincts - if something feels wrong, it probably is.",
                        "Make noise and draw attention if being followed or threatened.",
                        "Document incidents (photos, texts, witnesses) for authorities."
                ),
                true
        ));

        currentCategories = new ArrayList<>(allCategories);
    }

    private void setupRecyclerView() {
        adapter = new SafetyCategoryAdapter(currentCategories);
        recyclerSafety.setLayoutManager(new LinearLayoutManager(this));
        recyclerSafety.setAdapter(adapter);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());

        settingsButton.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));

        tabAll.setOnClickListener(v -> {
            updateTabs(tabAll);
            filterCategories("all");
        });

        tabFirstAid.setOnClickListener(v -> {
            updateTabs(tabFirstAid);
            filterCategories("first_aid");
        });

        tabFireSafety.setOnClickListener(v -> {
            updateTabs(tabFireSafety);
            filterCategories("fire");
        });

        tabDisaster.setOnClickListener(v -> {
            updateTabs(tabDisaster);
            filterCategories("disaster");
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCategories(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        // Bottom navigation
        View navHome = findViewById(R.id.navHome);
        View navHistory = findViewById(R.id.navHistory);
        View navSOS = findViewById(R.id.navSOS);
        View navContacts = findViewById(R.id.navContacts);
        View navSafety = findViewById(R.id.navSafety);

        navHome.setOnClickListener(v -> startActivity(new Intent(this, HomeActivity.class)));
        navHistory.setOnClickListener(v -> startActivity(new Intent(this, AlertHistoryActivity.class)));
        navSOS.setOnClickListener(v -> startActivity(new Intent(this, HomeActivity.class)));
        navContacts.setOnClickListener(v -> startActivity(new Intent(this, ViewContactsActivity.class)));
        navSafety.setOnClickListener(v -> startActivity(new Intent(this, SafetyGuideActivity.class)));
    }

    private void updateTabs(TextView selectedTab) {
        // Reset all tabs
        tabAll.setBackgroundResource(R.drawable.tab_unselected_bg);
        tabAll.setTextColor(getResources().getColor(R.color.light_home_text, null));

        tabFirstAid.setBackgroundResource(R.drawable.tab_unselected_bg);
        tabFirstAid.setTextColor(getResources().getColor(R.color.light_home_text, null));

        tabFireSafety.setBackgroundResource(R.drawable.tab_unselected_bg);
        tabFireSafety.setTextColor(getResources().getColor(R.color.light_home_text, null));

        tabDisaster.setBackgroundResource(R.drawable.tab_unselected_bg);
        tabDisaster.setTextColor(getResources().getColor(R.color.light_home_text, null));

        // Highlight selected tab
        selectedTab.setBackgroundResource(R.drawable.tab_selected_bg);
        selectedTab.setTextColor(getResources().getColor(android.R.color.white, null));
    }

    private void filterCategories(String filter) {
        currentCategories.clear();

        if (filter.equalsIgnoreCase("all")) {
            currentCategories.addAll(allCategories);
        } else if (filter.equalsIgnoreCase("first_aid")) {
            for (SafetyCategory cat : allCategories) {
                if (cat.getTitle().contains("CPR") ||
                        cat.getTitle().contains("Bleeding") ||
                        cat.getTitle().contains("Choking") ||
                        cat.getTitle().contains("burn") ||
                        cat.getTitle().contains("Medical Emergency")) {
                    currentCategories.add(cat);
                }
            }
        } else if (filter.equalsIgnoreCase("fire")) {
            for (SafetyCategory cat : allCategories) {
                if (cat.getTitle().contains("burn") ||
                        cat.getTitle().contains("Fire")) {
                    currentCategories.add(cat);
                }
            }
        } else if (filter.equalsIgnoreCase("disaster")) {
            for (SafetyCategory cat : allCategories) {
                if (cat.getTitle().contains("Earthquake") ||
                        cat.getTitle().contains("Accident")) {
                    currentCategories.add(cat);
                }
            }
        } else {
            for (SafetyCategory cat : allCategories) {
                if (cat.getTitle().toLowerCase().contains(filter.toLowerCase()) ||
                        cat.getSubtitle().toLowerCase().contains(filter.toLowerCase())) {
                    currentCategories.add(cat);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }
}