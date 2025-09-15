package com.gauravpatil.kindnesscabinet;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.gauravpatil.kindnesscabinet.AboutUs.About_Us_Activity;
import com.gauravpatil.kindnesscabinet.AddDonateorSell.AddFragment;
import com.gauravpatil.kindnesscabinet.Contact.Contact_Us_Activity;
import com.gauravpatil.kindnesscabinet.Favorites.FavoritesActivity;
import com.gauravpatil.kindnesscabinet.Feedback.Feedback_Activity;
import com.gauravpatil.kindnesscabinet.History.HistoryFragment;
import com.gauravpatil.kindnesscabinet.HomeFragment.HomeFragment;
import com.gauravpatil.kindnesscabinet.ViewProductRequest.ViewProductRequestActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener
{
    BottomNavigationView bottomNavigationView;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    SharedPreferences preferences; // Store temporary data within app
    SharedPreferences.Editor editor; // edit or put the Temporary dada in SharedPreferences

    GoogleSignInOptions googleSignInOptions; //Show option of gmail
    GoogleSignInClient googleSignInClient;  //Selected gmail option store



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        editor= preferences.edit();

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(HomeActivity.this,googleSignInOptions);

        // Initialize Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Set as ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable Home button
        getSupportActionBar().setHomeButtonEnabled(true);

        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Drawer Toggle (☰ Hamburger Menu)
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState(); // Show Hamburger Icon

        bottomNavigationView = findViewById(R.id.homeBottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.menuBottomNavHome);
    }
    HomeFragment homeFragment = new HomeFragment();
    CategoriesFragment categoriesFragment = new CategoriesFragment();
    AddFragment addFragment = new AddFragment();
    HistoryFragment historyFragment = new HistoryFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        if(menuItem.getItemId() == R.id.menuBottomNavHome)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout,homeFragment).commit();
        }

        else if(menuItem.getItemId() == R.id.menuBottomNavSearch)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout, categoriesFragment).commit();
        }

        else if(menuItem.getItemId() == R.id.menuBottomNavAdd)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout,addFragment).commit();
        }

        else if(menuItem.getItemId() == R.id.menuBottomNavHistory)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout,historyFragment).commit();
        }

        else if(menuItem.getItemId() == R.id.menuBottomNavProfile)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout,profileFragment).commit();
        }

        else if(menuItem.getItemId() == R.id.nav_product_request)
        {
            Intent intent = new Intent(HomeActivity.this, ViewProductRequestActivity.class);
            startActivity(intent);
        }

        else if(menuItem.getItemId() == R.id.nav_contact)
        {
            Intent intent = new Intent(HomeActivity.this, Contact_Us_Activity.class);
            startActivity(intent);
        }

        else if(menuItem.getItemId() == R.id.nav_about_us)
        {
            Intent intent = new Intent(HomeActivity.this, About_Us_Activity.class);
            startActivity(intent);
        }

        else if(menuItem.getItemId() == R.id.nav_feedback)
        {
            Intent intent = new Intent(HomeActivity.this, Feedback_Activity.class);
            startActivity(intent);
        }

        else if(menuItem.getItemId() == R.id.nav_favorites)
        {
            Intent intent = new Intent(HomeActivity.this, FavoritesActivity.class);
            startActivity(intent);
        }
        else if(menuItem.getItemId() == R.id.nav_logout)
        {
            logout();
        }

        return true;
    }

    private void logout() {
        AlertDialog.Builder ad = new AlertDialog.Builder(HomeActivity.this);
        ad.setTitle("Kindness Cabinate App");
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
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                googleSignInClient.signOut();
                editor.putBoolean("islogin",false).commit();
                startActivity(intent);
                finish();
            }
        });

        AlertDialog alertDialog = ad.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    @Override
    public void onBackPressed()
    {
        logout();
    }
}