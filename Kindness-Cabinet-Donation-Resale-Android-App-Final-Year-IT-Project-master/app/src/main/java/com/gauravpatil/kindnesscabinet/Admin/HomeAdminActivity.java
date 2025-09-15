package com.gauravpatil.kindnesscabinet.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.gauravpatil.kindnesscabinet.Admin.AddUser.AddUserActivity;
import com.gauravpatil.kindnesscabinet.Admin.AllUser.AllUserActivity;
import com.gauravpatil.kindnesscabinet.Admin.History.HistoryActivity;
import com.gauravpatil.kindnesscabinet.R;

public class HomeAdminActivity extends AppCompatActivity {

    CardView cv1,cv2,cv3,cv4;
    SharedPreferences preferences; // Store temporary data within app
    SharedPreferences.Editor editor; // edit or put the Temporary dada in SharedPreferences


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        preferences = PreferenceManager.getDefaultSharedPreferences(HomeAdminActivity.this);
        editor= preferences.edit();

        cv1 = findViewById(R.id.cv1);
        cv2 = findViewById(R.id.cv2);
        cv3 = findViewById(R.id.cv3);
        cv4 = findViewById(R.id.cv4);

        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeAdminActivity.this, AllUserActivity.class);
                startActivity(intent);
            }
        });

        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeAdminActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        cv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeAdminActivity.this, AddUserActivity.class);
                startActivity(intent);
            }
        });

        cv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout() {
        AlertDialog.Builder ad = new AlertDialog.Builder(HomeAdminActivity.this);
        ad.setTitle("Kindness Cabinate Admin App");
        ad.setMessage("Are you sure you want to logout");
        ad.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        ad.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(HomeAdminActivity.this, AdminLoginActivity.class);
                editor.putBoolean("isAdminLogin",false).commit();
                startActivity(intent);
                finish();
            }
        });

        AlertDialog alertDialog = ad.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        logout();
    }
}