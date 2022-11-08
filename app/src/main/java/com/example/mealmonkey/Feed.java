package com.example.mealmonkey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class Feed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String email = getIntent().getStringExtra("email");
        setTitle(email);

        getIntent().getExtras().getString("name");

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.feed);
        FloatingActionButton floatingActionButtonLanguages = findViewById(R.id.floatingActionButtonLanguages);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(), MapsActivity.class).putExtra("email", email));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.feed:
                        return true;
                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(), About.class).putExtra("email", email));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        floatingActionButtonLanguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLanguagesDialog();
            }
        });
    }

    private void showChangeLanguagesDialog() {
        final String[] languagesList = {"Español", "English"};
        AlertDialog.Builder lBuilder = new AlertDialog.Builder(this);
        lBuilder.setTitle(R.string.text_choose_language);
        lBuilder.setSingleChoiceItems(languagesList, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        setLocale("es");
                        break;
                    case 1:
                        setLocale("en");
                        break;
                    default:
                        dialogInterface.dismiss();
                        break;
                }
            }
        });

        AlertDialog lDialog = lBuilder.create();
        lDialog.show();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }

    private void notImplemented() {
        Toast toast = Toast.makeText(this, R.string.text_error_not_implemented, Toast.LENGTH_LONG);
        toast.show();
    }
}