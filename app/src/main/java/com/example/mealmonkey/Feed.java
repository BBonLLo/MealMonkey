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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Locale;
import java.util.Map;

public class Feed extends AppCompatActivity {
    private FloatingActionButton floatingActionButtonLanguages;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_feed);
        getSupportActionBar().hide();
        loadLocale();

        String email = getIntent().getStringExtra("email");

        textView = findViewById(R.id.textMarkets);
        //tableLayout.setLayoutParams(new TableLayout.LayoutParams());
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.feed);

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

        floatingActionButtonLanguages = findViewById(R.id.floatingActionButtonLanguages);
        floatingActionButtonLanguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLanguagesDialog();
            }
        });

        db.collection("markers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    /*TableRow tableRow = new TableRow(null);
                    tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                    tableRow.setMinimumHeight(120);
                    tableLayout.addView(tableRow);*/

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (email.equals(document.get("User"))) {
                            Map<String, Object> m = document.getData();
                            textView.append("~~~~~~~~~~~~~~~~~~~~~~~~\n" + R.string.name +": " + m.get("Name") + "\n" + R.string.description +": "
                                    + m.get("Description") + "\n" + R.string.score + ": " + m.get("Score") + " ☆\n");
                        }
                    }
                }
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
                        recreate();
                        break;
                    case 1:
                        setLocale("en");
                        recreate();
                        break;
                    default:
                        dialogInterface.dismiss();
                        break;
                }
                dialogInterface.dismiss();
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