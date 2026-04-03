package com.lifeline.safety.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.lifeline.safety.db.DatabaseHelper;
import com.lifeline.safety.R;

public class AddContactActivity extends AppCompatActivity {

    private static final int PERMISSION_READ_CONTACTS = 100;
    private MaterialButton btnImportContacts, btnSaveContact;
    private ImageView btnBack;
    private ImageView settingsButton;
    private TextInputEditText etFullName, etPhoneNumber;
    private DatabaseHelper db;
    private ActivityResultLauncher<Intent> pickContactLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        initializeViews();
        setupDatabase();
        setupActivityLaunchers();
        setupClickListeners();
    }
    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        settingsButton = findViewById(R.id.settingsButton);
        btnImportContacts = findViewById(R.id.btnImportContacts);
        etFullName = findViewById(R.id.etFullName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnSaveContact = findViewById(R.id.btnSaveContact);
    }
    private void setupDatabase() {
        db = new DatabaseHelper(this);
    }
    private void setupActivityLaunchers() {
        pickContactLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri contactUri = result.getData().getData();
                        handleContactSelection(contactUri);
                    }
                }
        );
    }
    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        settingsButton.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));
        btnImportContacts.setOnClickListener(v -> importFromContacts());
        btnSaveContact.setOnClickListener(v -> saveContact());
    }
    private void importFromContacts() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_READ_CONTACTS);
        } else {
            Intent pickContactIntent = new Intent(Intent.ACTION_PICK,
                    ContactsContract.Contacts.CONTENT_URI);
            pickContactLauncher.launch(pickContactIntent);
        }
    }
    private void handleContactSelection(Uri contactUri) {
        try {
            android.database.Cursor cursor = getContentResolver().query(contactUri,
                    null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);

                String name = cursor.getString(nameIndex);
                String contactId = cursor.getString(idIndex);

                etFullName.setText(name);

                // Get phone number
                android.database.Cursor phoneCursor = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{contactId},
                        null);

                if (phoneCursor != null && phoneCursor.moveToFirst()) {
                    int phoneIndex = phoneCursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String phoneNumber = phoneCursor.getString(phoneIndex);
                    etPhoneNumber.setText(phoneNumber);
                    phoneCursor.close();
                }

                cursor.close();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error importing contact", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private void saveContact() {
        String name = etFullName.getText().toString().trim();
        String phone = etPhoneNumber.getText().toString().trim();

        // Validate name
        if (name.isEmpty()) {
            etFullName.setError("Full name is required");
            etFullName.requestFocus();
            return;
        }

        // Validate phone number
        if (phone.isEmpty()) {
            etPhoneNumber.setError("Phone number is required");
            etPhoneNumber.requestFocus();
            return;
        }

        // Remove spaces and special characters for validation
        String cleanPhone = phone.replaceAll("[\\s\\-\\(\\)\\+]", "");
        if (!cleanPhone.matches("\\d{10,13}")) {
            etPhoneNumber.setError("Enter a valid phone number (10-13 digits)");
            etPhoneNumber.requestFocus();
            return;
        }

        // Save to database
        boolean inserted = db.insertContact(name, phone);

        if (inserted) {
            Toast.makeText(this, "Contact saved successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving contact. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                importFromContacts();
            } else {
                Toast.makeText(this, "Permission denied to read contacts", Toast.LENGTH_SHORT).show();
            }
        }
    }
}