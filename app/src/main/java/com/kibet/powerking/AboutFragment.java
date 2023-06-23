package com.kibet.powerking;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class AboutFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton twitter = view.findViewById(R.id.btnTwitter);
        ImageButton telegram = view.findViewById(R.id.btnTelegram);
        ImageButton whatsapp = view.findViewById(R.id.btnWhatsapp);

        MaterialButton moreBtn = view.findViewById(R.id.btnMore);
        MaterialButton shareBtn = view.findViewById(R.id.btnShare);
        MaterialButton rateBtn = view.findViewById(R.id.btnRate);
        MaterialButton termsBtn = view.findViewById(R.id.btnTerms);
        MaterialButton freeBetBtn = view.findViewById(R.id.btnFreeBet);


        twitter.setOnClickListener(v -> openTwitter());
        telegram.setOnClickListener(v -> openTelegram());
        whatsapp.setOnClickListener(v -> openWhatsapp());
        moreBtn.setOnClickListener(v -> moreApps());
        shareBtn.setOnClickListener(v -> share());
        rateBtn.setOnClickListener(v -> rate());
        termsBtn.setOnClickListener(v -> openTerms());
        freeBetBtn.setOnClickListener(v -> getFreeBet());

    }


    public void openTwitter() {
        Uri uri = Uri.parse(getString(R.string.twitter_profile));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void openTelegram() {
        Uri uri = Uri.parse(getString(R.string.telegram_channel));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void openWhatsapp() {
        Uri uri = Uri.parse(getString(R.string.whatsapp_channel));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void moreApps() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(getString(R.string.more_apps_url)));
            intent.setPackage("com.kibet.powerking");
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(getString(R.string.more_apps_url)));
            startActivity(intent);
        }
    }

    private void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Football Livestream");
        String shareMessage = "\nLet me recommend you this application\n\n";
        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + requireContext().getPackageName() + "\n\n";
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    public void rate() {
        try {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getContext().getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        intent.addFlags(flags);
        return intent;
    }

    public void openTerms () {
        Intent intent = new Intent(getContext(), ConditionsActivity.class);
        startActivity(intent);
    }

    public void getFreeBet() {
        Uri uri = Uri.parse(getString(R.string.affiliate_url));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

}