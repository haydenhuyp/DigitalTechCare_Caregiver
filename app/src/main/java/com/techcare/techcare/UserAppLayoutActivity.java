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
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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

import java.util.ArrayList;
import java.util.Collections;

public class UserAppLayoutActivity extends AppCompatActivity {
    private static volatile ArrayList<GridCell> gridCells = new ArrayList<GridCell>();
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_app_layout);
        updateGridCells();

        findViewById(R.id.btnAddLayout1).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseActionActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnAddLayout2).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseActionActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnAddLayout3).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseActionActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnAddLayout4).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseActionActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnAddLayout5).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnCallUserAppLayout).setOnClickListener(v -> {

        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TextView txtDate = findViewById(R.id.txtTimeUserAppLayout);
                txtDate.setText(DateTimeUtility.getCurrentDateTime());
                // Repeat the task every second (1000 milliseconds)
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    /**
     * Updates the grid cells with the latest data from the database.
     */
    private void updateGridCells() {
        Log.w("updateGridCells", "updateGridCells");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("gridCells")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // process each document (gridCell data) and add to the gridCells list
                            String icon = document.getString("icon");
                            int _id = document.get("_id", Integer.TYPE).intValue();
                            String title = document.getString("title");
                            String actionParameter = document.getString("actionParameter");
                            String action = document.getString("action");
                            GridCell gridCell = new GridCell(_id, title, icon, actionParameter, action);
                            gridCells.add(gridCell);
                        }

                        CardView cardView1 = findViewById(R.id.btnAddLayout1);
                        CardView cardView2 = findViewById(R.id.btnAddLayout2);
                        CardView cardView3 = findViewById(R.id.btnAddLayout3);
                        CardView cardView4 = findViewById(R.id.btnAddLayout4);
                        for (int i = 0; i < gridCells.size(); i++) {
                            switch (gridCells.get(i).get_id()){
                                case 1:
                                    /* Hard-coded text to fix the \n issue - Firebase could not process \n correctly */
                                    updateCardView(cardView1, "Community\nRoom", gridCells.get(i).getIcon());
                                    break;
                                case 2:
                                    updateCardView(cardView2, gridCells.get(i).getTitle(), gridCells.get(i).getIcon());
                                    break;
                                case 3:
                                    updateCardView(cardView3, gridCells.get(i).getTitle(), gridCells.get(i).getIcon());
                                    break;
                                case 4:
                                    updateCardView(cardView4, gridCells.get(i).getTitle(), gridCells.get(i).getIcon());
                                    break;
                            }
                        }
                    } else {
                        Log.e(TAG, "Error getting data from Firebase: ", task.getException());
                    }
                });
    }

    /**
     * Update the cardView with the given title and icon
     * @param cardView one of the cardView buttons on main activity
     * @param title
     * @param icon the icon name in drawable, e.g. "ic_church"
     */
    private void updateCardView(CardView cardView, String title, String icon){
        if (cardView == null) return;
        // cardView doesn't have any child, return
        if (cardView.getChildCount() == 0) return;

        // first child is a LinearLayout
        View childView = cardView.getChildAt(0);

        if (childView instanceof LinearLayout) {
            LinearLayout linearLayout = (LinearLayout) childView;

            if (linearLayout.getChildCount() >= 2) {
                View imageView = linearLayout.getChildAt(0);
                View textView = linearLayout.getChildAt(1);

                if (imageView instanceof ImageView && textView instanceof TextView) {
                    ImageView myImageView = (ImageView) imageView;
                    TextView myTextView = (TextView) textView;
                    myTextView.setText(title);
                    // if resource not found, keep the current icon
                    if (getResources().getIdentifier(icon, "drawable", getPackageName()) == 0) return;
                    myImageView.setForeground(getDrawable(getResources()
                            .getIdentifier(icon, "drawable", getPackageName())));
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}