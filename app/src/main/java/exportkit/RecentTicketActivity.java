package exportkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import exportkit.figma.R;

public class RecentTicketActivity extends AppCompatActivity implements TicketAdapter.OnTouchTicketListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); //grab the authentification instance
    private FirebaseUser user = mAuth.getCurrentUser(); //get the user
    private String userID = user.getUid(); //ID of user
    private RecyclerView recyclerView;
    private ArrayList<TicketReciever> ticketArrayList;
    private TicketAdapter ticketAdapter;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        TextView activityName = findViewById(R.id.title_ticket_preview);
        activityName.setText("Favoris");
        mFirestore = FirebaseFirestore.getInstance();
        ticketArrayList = new ArrayList<>();
        ticketAdapter = new TicketAdapter(this,ticketArrayList,this);

        recyclerView = findViewById(R.id.ticket_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ticketAdapter);
        ImageButton backButton = findViewById(R.id.back_arrow_to_market);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        int time = 5;
        Calendar c1=Calendar.getInstance();
        c1.add(Calendar.DAY_OF_YEAR, -time);
        Timestamp date=new Timestamp(c1.getTime());
        mFirestore.collection("User").document(userID).collection("Market").document().collection("Ticket").whereGreaterThanOrEqualTo("date",date).orderBy("date").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(ContentValues.TAG, document.getId() + " => " + document.getData());

                                TicketReciever ticketReciever = document.toObject(TicketReciever.class);
                                Map<String,Object> map = document.getData();
                                ArrayList<String> imageList = (ArrayList<String>) map.get("imageArray");
                                ticketReciever.setImageArray(imageList);


                                Log.d("ticketTest", "title = "+ticketReciever.getDescription());
                                ticketArrayList.add(ticketReciever); //cast the fetched document into an market object and add this market to the list
                            }
                        } else {
                            Log.d(ContentValues.TAG, "Error getting documents: ", task.getException());
                        }
                        ticketAdapter.notifyDataSetChanged(); //to update the recyclerview when new market
                    }
                });

    }
    @Override
    public void onTouchTicket(int position) {
        Intent intent = new Intent(this,TicketPreviewActivity.class);
        intent.putExtra("title",ticketArrayList.get(position).getTitle());
        intent.putExtra("description",ticketArrayList.get(position).getDescription());
        intent.putExtra("date",ticketArrayList.get(position).getDate().getTime());
        intent.putExtra("imageUrlArray",ticketArrayList.get(position).getImageUrlList());
        intent.putExtra("favorite",ticketArrayList.get(position).isFavorite());
        intent.putExtra("ticketId",ticketArrayList.get(position).getTicketId());
        startActivity(intent);
    }

    @Override
    public void onTouchFavorite(int position){
        ImageButton favoriteButton = findViewById(R.id.favorite_ticket_activity_button);
        TicketReciever ticket = ticketArrayList.get(position);
        if(ticket.isFavorite()==false){
            favoriteButton.setImageResource(R.drawable.ic_favorite_yellow);
            ticket.setFavorite(true);
            mFirestore.collection("User").document(userID).collection("Market").document().collection("Ticket").document(ticket.getTicketId()).update("favorite",true);
        }
        else{
            favoriteButton.setImageResource(R.drawable.ic_favoris_nav);
            ticket.setFavorite(false);
            mFirestore.collection("User").document(userID).collection("Market").document().collection("Ticket").document(ticket.getTicketId()).update("favorite",false);
        }
    }

}