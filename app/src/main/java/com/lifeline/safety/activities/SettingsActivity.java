package com.lifeline.safety.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.lifeline.safety.R;
import com.lifeline.safety.db.DatabaseHelper;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "lifeline_prefs";
    private static final String KEY_SHAKE_SOS = "shake_sos";
    // private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_COUNTDOWN_SECONDS = "countdown_seconds";
    private static final String KEY_SOS_MESSAGE = "sos_message";

    private SwitchCompat switchShake;
    // private SwitchCompat switchDarkMode;
    private TextView tvCountdownValue;
    private TextView tvSmsPreview;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // applyThemeFromPrefs();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        initializeViews();
        loadPreferences();
        setupClickListeners();
        setupBottomNav();
    }

    private void applyThemeFromPrefs() {
        // Removed dark mode theme application
    }

    private void initializeViews() {
        switchShake = findViewById(R.id.switchShake);
        tvCountdownValue = findViewById(R.id.tvCountdownValue);
        tvSmsPreview = findViewById(R.id.tvSmsPreview);
    }

    private void loadPreferences() {
        boolean shakeEnabled = prefs.getBoolean(KEY_SHAKE_SOS, false);
        int countdownSeconds = prefs.getInt(KEY_COUNTDOWN_SECONDS, 5);

        String defaultMessage = "🚨 EMERGENCY ALERT!\nI need immediate help.\n";
        String message = prefs.getString(KEY_SOS_MESSAGE, defaultMessage);

        switchShake.setChecked(shakeEnabled);
        tvCountdownValue.setText(countdownSeconds + "s");
        tvSmsPreview.setText(message);
    }

    private void setupClickListeners() {
        switchShake.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(KEY_SHAKE_SOS, isChecked).apply();
        });
        // Removed dark mode toggle listener

        RelativeLayout rowCountdown = findViewById(R.id.rowCountdown);
        rowCountdown.setOnClickListener(v -> showCountdownDialog());

        RelativeLayout rowSmsMessage = findViewById(R.id.rowSmsMessage);
        rowSmsMessage.setOnClickListener(v -> showSmsEditDialog());

        RelativeLayout rowClearHistory = findViewById(R.id.rowClearHistory);
        rowClearHistory.setOnClickListener(v -> confirmClearHistory());
    }

    private void showCountdownDialog() {
        final String[] options = {"3 seconds", "5 seconds", "10 seconds"};
        final int[] values = {3, 5, 10};

        int current = prefs.getInt(KEY_COUNTDOWN_SECONDS, 5);
        int checkedIndex = 1;
        for (int i = 0; i < values.length; i++) {
            if (values[i] == current) {
                checkedIndex = i;
                break;
            }
        }

        new AlertDialog.Builder(this)
                .setTitle("Countdown duration")
                .setSingleChoiceItems(options, checkedIndex, (dialog, which) -> {
                    int seconds = values[which];
                    prefs.edit().putInt(KEY_COUNTDOWN_SECONDS, seconds).apply();
                    tvCountdownValue.setText(seconds + "s");
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showSmsEditDialog() {
        final android.widget.EditText editText = new android.widget.EditText(this);
        editText.setMinLines(3);
        editText.setText(tvSmsPreview.getText());

        new AlertDialog.Builder(this)
                .setTitle("Default SOS message")
                .setView(editText)
                .setPositiveButton("Save", (dialog, which) -> {
                    String text = editText.getText().toString().trim();
                    if (text.isEmpty()) {
                        Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    prefs.edit().putString(KEY_SOS_MESSAGE, text).apply();
                    tvSmsPreview.setText(text);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void confirmClearHistory() {
        new AlertDialog.Builder(this)
                .setTitle("Clear alert history")
                .setMessage("This will remove all previous SOS alerts. This cannot be undone.")
                .setPositiveButton("Clear", (dialog, which) -> {
                    DatabaseHelper db = new DatabaseHelper(this);
                    db.clearAlertHistory();
                    Toast.makeText(this, "Alert history cleared", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void setupBottomNav() {
        LinearLayout navHome = findViewById(R.id.navHome);
        LinearLayout navHistory = findViewById(R.id.navHistory);
        RelativeLayout navSOS = findViewById(R.id.navSOS);
        LinearLayout navContacts = findViewById(R.id.navContacts);
        LinearLayout navSafety = findViewById(R.id.navSafety);

        navHome.setOnClickListener(v ->
                startActivity(new Intent(this, HomeActivity.class)));

        navHistory.setOnClickListener(v ->
                startActivity(new Intent(this, AlertHistoryActivity.class)));

        navSOS.setOnClickListener(v ->
                startActivity(new Intent(this, HomeActivity.class)));

        navContacts.setOnClickListener(v ->
                startActivity(new Intent(this, ViewContactsActivity.class)));

        navSafety.setOnClickListener(v ->
                startActivity(new Intent(this, SafetyGuideActivity.class)));
    }
}

