package com.kibet.powerking;

import android.content.Context;
import android.content.Intent;

import com.squareup.picasso.BuildConfig;

public class ShareManager {

    Context context;

    public ShareManager(Context context) {
        this.context = context;
    }

    public void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Powerking");
        String shareMessage= "\nGet Today's Football Tips Here\n\n";
        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + context.getPackageName() +"\n\n";
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        context.startActivity(shareIntent);
    }
}
