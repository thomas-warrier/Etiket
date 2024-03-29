package exportkit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import exportkit.figma.R;
import org.jetbrains.annotations.NotNull;

public class HomePage extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_view);
        bottomNav.setOnItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomePageFragment()).commit();
        if (isNetworkConnected()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String host = "outlook.office365.com";
                    String port = "995";
                    String userName = "etiket@outlook.fr";
                    String password = "T2o1t1o1";


                    MailReception receiver = new MailReception();
                    receiver.downloadEmailAttachments(host, port, userName, password);
                }
            }).start();
            }
        else{
            Toast.makeText(this, "Aucune connexion", Toast.LENGTH_LONG).show();
        }
        }


    private NavigationBarView.OnItemSelectedListener navListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch(item.getItemId()){
                        case R.id.qr_code_nav_bottom:
                            selectedFragment = new HomePageFragment();
                            break;
                        case R.id.ticket_nav_bottom:
                            selectedFragment = new MarketFragment();
                            break;
                        case R.id.profile_nav_bottom:
                            selectedFragment = new ProfileFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    return true;
                }
            };

    private boolean isNetworkConnected() { //to verify the internet connection
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


}