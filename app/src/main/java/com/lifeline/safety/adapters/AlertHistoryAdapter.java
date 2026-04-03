package com.lifeline.safety.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.lifeline.safety.R;
import com.lifeline.safety.models.AlertHistory;

import java.util.List;

public class AlertHistoryAdapter extends RecyclerView.Adapter<AlertHistoryAdapter.ViewHolder> {

    private final List<AlertHistory> history;

    public AlertHistoryAdapter(List<AlertHistory> history) {
        this.history = history;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTime, tvLocation, tvDeliveryStatus;
        TextView tvLocationTitle, tvLocationSubtitle, tvErrorMessage;
        ShapeableImageView mapPreview;
        CardView errorCard;

        ViewHolder(@NonNull View v) {
            super(v);
            tvDate = v.findViewById(R.id.tvDate);
            tvTime = v.findViewById(R.id.tvTime);
            tvLocation = v.findViewById(R.id.tvLocation);
            tvDeliveryStatus = v.findViewById(R.id.tvDeliveryStatus);
            tvLocationTitle = v.findViewById(R.id.tvLocationTitle);
            tvLocationSubtitle = v.findViewById(R.id.tvLocationSubtitle);
            tvErrorMessage = v.findViewById(R.id.tvErrorMessage);
            mapPreview = v.findViewById(R.id.mapPreview);
            errorCard = v.findViewById(R.id.errorCard);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alert_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        AlertHistory item = history.get(pos);

        String location = item.getLocation();
        boolean isSuccessful = item.isSuccessful();

        h.tvDate.setText(item.getDate());
        h.tvTime.setText(item.getTime());
        h.tvLocation.setText(location);

        // Clear old listeners (RecyclerView reuse safety)
        h.tvLocation.setOnClickListener(null);
        if (h.mapPreview != null) {
            h.mapPreview.setOnClickListener(null);
        }

        // Configure UI based on success/failure
        if (isSuccessful) {
            // Success state - show map preview and "Delivered" status
            h.tvDeliveryStatus.setVisibility(View.VISIBLE);
            h.tvDeliveryStatus.setText(" • Delivered");
            h.tvDeliveryStatus.setTextColor(h.itemView.getContext().getResources().getColor(R.color.status_green, null));

            h.tvLocationTitle.setText("Location Shared");
            h.tvLocationSubtitle.setText("Sent to Emergency Contacts");

            h.mapPreview.setVisibility(View.VISIBLE);
            h.errorCard.setVisibility(View.GONE);

            // Make map preview and location clickable
            if (location != null && location.startsWith("http")) {
                View.OnClickListener clickListener = v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(location));
                    v.getContext().startActivity(intent);
                };

                h.tvLocation.setOnClickListener(clickListener);
                h.mapPreview.setOnClickListener(clickListener);
            }
        } else {
            // Error state - hide map, show error card, hide "Delivered"
            h.tvDeliveryStatus.setVisibility(View.GONE);

            h.tvLocationTitle.setText("Alert Failed");
            h.tvLocationSubtitle.setText("Could not send SOS");

            h.mapPreview.setVisibility(View.GONE);
            h.errorCard.setVisibility(View.VISIBLE);

            // Set error message
            String errorMessage = item.getErrorMessage();
            h.tvErrorMessage.setText(errorMessage);
        }
    }

    @Override
    public int getItemCount() {
        return history.size();
    }
}