package com.example.pawtner;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private BottomNavigationView navBottom;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Hilangkan judul default

        // Setup Navigation
        navBottom = findViewById(R.id.nav_bottom);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        // Hanya fragment utama (navbar) dimasukkan ke AppBarConfiguration
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_chat,
                R.id.navigation_mypets,
                R.id.navigation_events
        ).build();

        NavigationUI.setupWithNavController(navBottom, navController);

        // Listener untuk BottomNavigationView
        navBottom.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            int currentId = navController.getCurrentDestination().getId();

            if (currentId == id) {
                return false; // Sudah di halaman yang sama, tidak navigasi ulang
            }

            // Tampilkan toast sesuai menu
//            if (id == R.id.navigation_home) {
//                Toast.makeText(this, "Home ditekan", Toast.LENGTH_SHORT).show();
//            } else if (id == R.id.navigation_chat) {
//                Toast.makeText(this, "Chat ditekan", Toast.LENGTH_SHORT).show();
//            } else if (id == R.id.navigation_mypets) {
//                Toast.makeText(this, "My Pets ditekan", Toast.LENGTH_SHORT).show();
//            } else if (id == R.id.navigation_events) {
//                Toast.makeText(this, "Event ditekan", Toast.LENGTH_SHORT).show();
//            }

            // Navigasi dengan popUpTo agar membersihkan fragment sebelumnya
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.mobile_navigation, true)
                    .setLaunchSingleTop(true)
                    .build();

            navController.navigate(id, null, navOptions);
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        int currentId = navController.getCurrentDestination().getId();

        if (id == R.id.action_notifications && currentId != R.id.navigation_notifications) {
            navController.navigate(R.id.navigation_notifications);
            return true;
        } else if (id == R.id.action_profile && currentId != R.id.navigation_profile) {
            navController.navigate(R.id.navigation_profile);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
