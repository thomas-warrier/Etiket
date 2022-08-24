package exportkit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import exportkit.figma.R;

public class TicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        TextView marketName = findViewById(R.id.market_name_ticket_activity);
        marketName.setText(getIntent().getStringExtra("marketName"));
    }
}