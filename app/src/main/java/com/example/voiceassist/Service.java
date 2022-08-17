package com.example.voiceassist;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.util.Log;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class Service extends AccessibilityService {
    public static String appname = "";
    private static final String TAG = "My Service: ";
    public static boolean recording = false;
    private static AccessibilityEvent[] events = new AccessibilityEvent[100];
    private static String[] text = new String[10];
    private static boolean typing = false;
    private int count = 0;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (recording) {
            System.out.println(event.getPackageName().toString());
            if (event.getPackageName() != null && event.getPackageName().length() != 0) {
                String app = (String) event.getPackageName().toString();
                if (app.equals(appname)) {
                    //System.out.println("whatsapp event");
                    if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
                        typing = false;
                        System.out.println("clicked: " + event.toString());
                    } else if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
                        typing = true;
                        System.out.println("Text: " + event.toString());

                    } else if (typing) {
                        count += 1;
                        text[count] = String.valueOf(event.getText());
                        System.out.println(String.valueOf(event.getText()));
                    }

                }
            } else if (event.getPackageName() != appname) {
                recording = false;
            }

            // System.out.println("Event: " + event.toString());
            // Log.v(TAG, "Event: " + event.toString());
        }
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onServiceConnected() {
        Log.v(TAG, "Service running");
    }
}