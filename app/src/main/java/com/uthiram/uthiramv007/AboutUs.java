package com.uthiram.uthiramv007;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static com.uthiram.uthiramv007.R.string.navigation_draw_open;

/**
 * About Us Activity will display the why <b>UTHIRAM</b> was launched.
 */
public class AboutUs extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // declaring all the used widgets and dataTypes in this activity.
    private DrawerLayout drawerLayout;

    private String loginPath = "null", currentDate, currentTime;

    private String rollNoPath = "null";

    private String rollNo = "";

    private char[] j = new char[1];

    private char r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        //created two strings for retrieving both loginCredential text and rollNo path for sharedPreferences.

        loginPath = getExternalFilesDir("text").getAbsolutePath() + "/loginCredentials.txt";

        rollNoPath = getExternalFilesDir("text").getAbsolutePath() + "/rollNo.txt";

        //using FileReader class, reads the contents of loginCredential text file and stores it in an character array(j).

        FileReader fr = null;
        try {
            fr = new FileReader(loginPath);
            int i;
            while ((i = fr.read()) != -1)
                j[0] = (char) i;
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //using FileReader class, reads the contents of rollNo text file and stores it in an String(rollNo).

        File file = new File(rollNoPath);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String st;
            while ((st = br.readLine()) != null)
                rollNo = st;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Navigation Drawer is initialized and implemented.

        drawerLayout = findViewById(R.id.aboutUs_design_navigation_view);

        Toolbar toolbar = findViewById(R.id.aboutUs_toolbar);

        NavigationView navigationView = findViewById(R.id.aboutUs_nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, navigation_draw_open, R.string.navigation_draw_close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();
    }

    /**
     * onBackPressed() this activity will redirected to EmergencyRequest Activity.
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        Intent intent = new Intent(AboutUs.this, EmergencyRequests.class);
        startActivity(intent);
        AboutUs.this.finish();
    }

    /**
     * onNavigationItemSelected() function is declared to redirect respective activities based on their id.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.home:
                Intent intent = new Intent(AboutUs.this, EmergencyRequests.class);
                startActivity(intent);
                AboutUs.this.finish();
                break;
            case R.id.filter:
                Intent intent1 = new Intent(AboutUs.this, HomePage.class);
                startActivity(intent1);
                AboutUs.this.finish();
                break;
            case R.id.donor_login:
                if (checkLoginCredential()) {
                    Intent intent2 = new Intent(AboutUs.this, LoginPage.class);
                    intent2.putExtra("rollNo", rollNo);
                    startActivity(intent2);
                    AboutUs.this.finish();
                } else {
                    Intent intent3 = new Intent(AboutUs.this, LoginPage.class);
                    rollNo = "123456";
                    intent3.putExtra("rollNo", rollNo);
                    startActivity(intent3);
                    AboutUs.this.finish();
                }
                break;
            case R.id.about_us:
                Intent intent2 = new Intent(AboutUs.this, AboutUs.class);
                startActivity(intent2);
                AboutUs.this.finish();
                break;
            case R.id.contact_us:
                Intent intent4 = new Intent(AboutUs.this, ContactUs.class);
                startActivity(intent4);
                AboutUs.this.finish();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * checks for SharedPreferences(Auto-Login)
     */
    private boolean checkLoginCredential() {
        int k = Character.compare(j[0], '1');
        if (k == 0) {

            return true;
        } else {
            return false;
        }
    }
}