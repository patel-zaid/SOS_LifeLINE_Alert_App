package com.lifeline.safety.adapters;

import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.lifeline.safety.R;
import com.lifeline.safety.db.DatabaseHelper;
import com.lifeline.safety.models.Contact;
import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    ArrayList<Contact> contacts;
    DatabaseHelper db;

    public ContactAdapter(ArrayList<Contact> contacts, DatabaseHelper db){
        this.contacts = contacts;
        this.db = db;
    }

    // ✅ PROPERLY UPDATE CONTACTS
    public void updateContacts(ArrayList<Contact> newContacts) {
        if (newContacts != null) {
            this.contacts.clear();
            this.contacts.addAll(newContacts);
            notifyDataSetChanged();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvPhone;
        ImageView btnDelete;

        ViewHolder(View v){
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvPhone = v.findViewById(R.id.tvPhone);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact c = contacts.get(position);
        holder.tvName.setText(c.getName());
        holder.tvPhone.setText(c.getPhone());

        holder.btnDelete.setOnClickListener(v -> {
            db.deleteContact(c.getId());
            contacts.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, contacts.size());
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}