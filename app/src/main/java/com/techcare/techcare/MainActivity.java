package com.techcare.techcare;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import static com.techcare.techcare.DataUtility.APP_ID;
import static com.techcare.techcare.DataUtility.APP_SIGN;
import static com.techcare.techcare.DataUtility.currentUserID;
import static com.techcare.techcare.DataUtility.currentUserName;
import static com.techcare.techcare.DataUtility.targetUserID;
import static com.techcare.techcare.DataUtility.targetUserName;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.zegocloud.uikit.plugin.invitation.ZegoInvitationType;
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallConfig;
import com.zegocloud.uikit.prebuilt.call.config.ZegoMenuBarButtonName;
import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoCallInvitationData;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallConfigProvider;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static volatile ArrayList<GridCell> gridCells = new ArrayList<GridCell>();
    private ZegoSendCallInvitationButton btnZegoCallSendInvitation;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // keeps screen on

        findViewById(R.id.cell1).setOnClickListener(v -> {
            Intent intent = new Intent(this, CallActivity.class);
            startActivity(intent);
        });

        startCallInvitationService();
        initSendCallInvitationButton();
        findViewById(R.id.cell2).setOnClickListener(v -> {
            btnZegoCallSendInvitation.performClick();
        });

        findViewById(R.id.cell3).setOnClickListener(v -> {
            Intent intent = new Intent(this, UserAppLayoutActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.cell4).setOnClickListener(v -> {
            btnZegoCallSendInvitation.performClick();
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TextView txtDate = findViewById(R.id.txtDate);
                txtDate.setText(DateTimeUtility.getCurrentDateTime());
                // Repeat the task every second (1000 milliseconds)
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void startCallInvitationService(){
        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();

        callInvitationConfig.provider = new ZegoUIKitPrebuiltCallConfigProvider() {
            @Override
            public ZegoUIKitPrebuiltCallConfig requireConfig(ZegoCallInvitationData invitationData) {
                ZegoUIKitPrebuiltCallConfig config = null;
                boolean isVideoCall = invitationData.type == ZegoInvitationType.VIDEO_CALL.getValue();
                boolean isGroupCall = invitationData.invitees.size() > 1;
                if (isVideoCall && isGroupCall) {
                    config = ZegoUIKitPrebuiltCallConfig.groupVideoCall();
                } else if (!isVideoCall && isGroupCall) {
                    config = ZegoUIKitPrebuiltCallConfig.groupVoiceCall();
                } else if (!isVideoCall) {
                    config = ZegoUIKitPrebuiltCallConfig.oneOnOneVoiceCall();
                } else {
                    config = ZegoUIKitPrebuiltCallConfig.oneOnOneVideoCall();
                }
                config.topMenuBarConfig.isVisible = true;
                config.topMenuBarConfig.buttons.add(ZegoMenuBarButtonName.MINIMIZING_BUTTON);
                return config;
            }
        };

        ZegoNotificationConfig notificationConfig = new ZegoNotificationConfig();
        notificationConfig.sound = "zego_uikit_sound_call";
        notificationConfig.channelID = "CallInvitation";
        notificationConfig.channelName = "CallInvitation";
        ZegoUIKitPrebuiltCallInvitationService.init(getApplication(), APP_ID, APP_SIGN, currentUserID, currentUserName, callInvitationConfig);
    }

    private void initSendCallInvitationButton(){
        Context context = getApplicationContext();

        btnZegoCallSendInvitation = new ZegoSendCallInvitationButton(context);
        // If true, a video call. Otherwise, a voice call is made.
        btnZegoCallSendInvitation.setIsVideoCall(true);
        btnZegoCallSendInvitation.setResourceID("zego_uikit_call");

        btnZegoCallSendInvitation.setOnClickListener(v -> {
            btnZegoCallSendInvitation.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserID, targetUserName)));
        });
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        ZegoUIKitPrebuiltCallInvitationService.unInit();
        super.onDestroy();
    }
}