package com.lifeline.safety.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lifeline.safety.R;
import com.lifeline.safety.models.SafetyCategory;

import java.util.List;

public class SafetyCategoryAdapter extends RecyclerView.Adapter<SafetyCategoryAdapter.ViewHolder> {

    private final List<SafetyCategory> categories;

    public SafetyCategoryAdapter(List<SafetyCategory> categories) {
        this.categories = categories;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon, expandIcon;
        TextView title, subtitle;
        LinearLayout expandedContent, headerLayout;
        View divider;

        ViewHolder(@NonNull View v) {
            super(v);
            icon = v.findViewById(R.id.categoryIcon);
            title = v.findViewById(R.id.categoryTitle);
            subtitle = v.findViewById(R.id.categorySubtitle);
            expandIcon = v.findViewById(R.id.expandIcon);
            expandedContent = v.findViewById(R.id.expandedContent);
            divider = v.findViewById(R.id.divider);
            headerLayout = v.findViewById(R.id.headerLayout);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_safety_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        SafetyCategory category = categories.get(pos);

        h.icon.setImageResource(category.getIconRes());
        h.icon.setBackgroundResource(category.getBackgroundRes());
        h.icon.setColorFilter(category.getIconTint());

        h.title.setText(category.getTitle());
        h.subtitle.setText(category.getSubtitle());

        if (category.isExpanded()) {
            h.expandedContent.setVisibility(View.VISIBLE);
            h.divider.setVisibility(View.VISIBLE);
            h.expandIcon.setImageResource(R.drawable.ic_expand_less);
            populateSteps(h.expandedContent, category);
        } else {
            h.expandedContent.setVisibility(View.GONE);
            h.divider.setVisibility(View.GONE);
            h.expandIcon.setImageResource(R.drawable.ic_expand_more);
        }

        h.headerLayout.setOnClickListener(v -> {
            category.toggleExpanded();
            notifyItemChanged(pos);
        });

        h.itemView.setOnClickListener(v -> {
            category.toggleExpanded();
            notifyItemChanged(pos);
        });
    }

    private void populateSteps(LinearLayout container, SafetyCategory category) {
        container.removeAllViews();

        List<String> steps = category.getSteps();
        for (int i = 0; i < steps.size(); i++) {

            View stepView = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.item_safety_step, container, false);

            TextView stepNumber = stepView.findViewById(R.id.stepNumber);
            TextView stepText = stepView.findViewById(R.id.stepText);

            stepNumber.setText((i + 1) + ".");
            stepText.setText(steps.get(i));

            container.addView(stepView);
        }

        if (category.isOfflineAvailable()) {
            View badgeView = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.item_offline_badge, container, false);
            container.addView(badgeView);
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
