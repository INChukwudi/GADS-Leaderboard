package com.chukwudi.leaderboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SkillsAdapter extends RecyclerView.Adapter<SkillsAdapter.SkillViewHolder> {
    ArrayList<Skill> mSkills;

    public SkillsAdapter(ArrayList<Skill> learners) {
        mSkills = learners;
    }

    @NonNull
    @Override
    public SkillsAdapter.SkillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.skilliq_leaders_list_item, parent, false);
        return new SkillsAdapter.SkillViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillViewHolder holder, int position) {
        Skill skill = mSkills.get(position);
        holder.bind(skill);
    }

    @Override
    public int getItemCount() {
        return mSkills.size();
    }

    public class SkillViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvDetails;

        public SkillViewHolder(View itemView) {
            super(itemView);
            this.tvName = itemView.findViewById(R.id.text_skill_learner_name);
            this.tvDetails = itemView.findViewById(R.id.text_skill_learner_details);
        }

        public void bind (Skill learner) {
            tvName.setText(learner.mName);
            String details = new StringBuilder().append(learner.mScore).append(" skill IQ Score, ").append(learner.mCountry).toString();
            tvDetails.setText(details);
        }
    }
}
