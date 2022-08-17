package com.example.voiceassist;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.View;

import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.voiceassist.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "My Application: ";
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private List<String> applist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PackageManager pm = getPackageManager();
        //get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.applist);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.loadLabel(pm).toString().equals("VoiceAssist") || packageInfo.loadLabel(pm).toString().contains(".")) {
                continue;
            }
            ImageView imageView = new ImageView(this);
            TextView textView = new TextView(this);

            try {
                imageView.setImageDrawable(pm.getApplicationIcon(packageInfo.packageName));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            imageView.setForegroundGravity(Gravity.LEFT);

            textView.setText(packageInfo.loadLabel(pm));
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setTag(packageInfo.packageName);
            textView.setOnClickListener(appclicked);

            linearLayout.addView(imageView);
            linearLayout.addView(textView);
        }

        Intent intent = new Intent(getContext(), Action.class);
        intent.setAction(Intent.ACTION_MAIN);

        ShortcutInfoCompat shortcutInfo = new ShortcutInfoCompat.Builder(getContext(), "eh")
                .setShortLabel("to")
                .setLongLabel("to do")
                .addCapabilityBinding(
                        "actions.intent.ASK", "Action.name", Arrays.asList("test"))
                .setIntent(intent)
                .build();

        // Push the shortcut
        ShortcutManagerCompat.pushDynamicShortcut(getContext(), shortcutInfo);



    }

    private Context getContext() {
        return this;
    }

    public void switch1(View view) {
        if (Service.recording == true) {
            Service.recording = false;
            // TODO: fix this binding.switch1.setChecked(false);
        } else {
            Service.recording = true;
            // binding.switch1.setChecked(true);
        }
    }


    private View.OnClickListener appclicked = new View.OnClickListener() {
        public void onClick(View v) {
            String packageName = (String) v.getTag();
            Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
            if (intent != null) {
                startActivity(intent);
                Service.appname = packageName;
                Service.recording = true;
                Toast.makeText(getContext(), "Recording started", Toast.LENGTH_SHORT).show();


            }

        }
    };
}