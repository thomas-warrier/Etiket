package exportkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import exportkit.figma.R;

public class TicketActivity extends AppCompatActivity implements TicketAdapter.OnTouchTicketListener{
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); //grab the authentification instance
    private FirebaseUser user = mAuth.getCurrentUser(); //get the user
    private String userID = user.getUid(); //ID of user
    RecyclerView recyclerView;
    ArrayList<TicketReciever> ticketArrayList;
    TicketAdapter ticketAdapter;
    FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        TextView marketName = findViewById(R.id.market_name_ticket_activity);
        marketName.setText(getIntent().getStringExtra("marketName"));
        mFirestore = FirebaseFirestore.getInstance();
        ticketArrayList = new ArrayList<>();
        ticketAdapter = new TicketAdapter(this,ticketArrayList,this);

        recyclerView = findViewById(R.id.ticket_recycler_view);
        recyclerView.setHasFixedSize(true); //set the size
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ticketAdapter);

        mFirestore.collection("User").document(userID).collection("Market").document(getIntent().getStringExtra("marketId")).collection("Ticket").orderBy("favorite").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(ContentValues.TAG, document.getId() + " => " + document.getData());
                                document.get("title");
                                ticketArrayList.add(document.toObject(TicketReciever.class)); //cast the fetched document into an market object and add this market to the list
                            }
                        } else {
                            Log.d(ContentValues.TAG, "Error getting documents: ", task.getException());
                        }
                        ticketAdapter.notifyDataSetChanged(); //to update le recyclerview when new market
                    }
                });
    }



    @Override
    public void onTouchTicket(int position) {
        Log.d("OntouchMarket", "onTouchTicket:ca marche !");
        //rajouter la Dialog avec infos complémentaire
    }
}