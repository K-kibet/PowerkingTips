package com.codesui.powerkingtips;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codesui.powerkingtips.activities.TipActivity;
import com.codesui.powerkingtips.ads.InterstitialManager;
import com.codesui.powerkingtips.data.Tip;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.ViewHolder> {
    private final Context context;
    private final Activity activity;
    private final List<Tip> tipsList;
    InterstitialManager interstitialManager = new InterstitialManager();

    public TipsAdapter(Context context, Activity activity, List<Tip> list) {
        this.context = context;
        this.activity = activity;
        this.tipsList = list;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tip, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        final Tip tip = this.tipsList.get(position);
        interstitialManager.loadInterstitial(TipsAdapter.this.activity);
        holder.textHome.setText(tip.getHomeTeam());
        holder.textAway.setText(tip.getAwayTeam());
        holder.textOdd.setText(tip.getOdds());
        holder.textPrediction.setText(tip.getPrediction());
        holder.textStatus.setText(tip.getStatus());
        holder.isWon.setText(tip.getWon());


        if (Objects.equals(tip.getStatus(), "live")) {
            holder.btnStatus.setVisibility(View.VISIBLE);
        } else {
            holder.btnStatus.setVisibility(View.INVISIBLE);
        }


        if (Objects.equals(tip.getWon(), "won")) {
            Picasso.get().load(R.drawable.baseline_verified_24).placeholder(R.drawable.baseline_verified_24).error(R.drawable.baseline_verified_24).into(holder.isWonImage);
        } else if (Objects.equals(tip.getWon(), "lost")) {
            Picasso.get().load(R.drawable.baseline_error_24).placeholder(R.drawable.baseline_error_24).error(R.drawable.baseline_error_24).into(holder.isWonImage);
        } else {
            Picasso.get().load(R.drawable.baseline_unpublished_24).placeholder(R.drawable.baseline_unpublished_24).error(R.drawable.baseline_unpublished_24).into(holder.isWonImage);
        }

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(TipsAdapter.this.context, TipActivity.class);
            intent.putExtra("tip_id", tip.getId());
            TipsAdapter.this.context.startActivity(intent);
            interstitialManager.showInterstitial(TipsAdapter.this.activity);
        });

    }

    public int getItemCount() {
        return this.tipsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textHome;
        private final TextView textAway;
        private final TextView textOdd;
        private final TextView textPrediction;
        private final TextView textStatus;
        private final TextView isWon;
        private final TextView btnStatus;
        private final ImageView isWonImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textHome = itemView.findViewById(R.id.textHome);
            this.textAway = itemView.findViewById(R.id.textAway);
            this.textOdd = itemView.findViewById(R.id.textOdd);
            this.textPrediction = itemView.findViewById(R.id.textPrediction);
            this.textStatus = itemView.findViewById(R.id.textStatus);
            this.isWon = itemView.findViewById(R.id.isWon);
            this.btnStatus = itemView.findViewById(R.id.btnStatus);
            this.isWonImage = itemView.findViewById(R.id.isWonImage);
        }
    }
}
