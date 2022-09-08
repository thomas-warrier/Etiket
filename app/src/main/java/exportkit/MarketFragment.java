package exportkit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import exportkit.figma.R;

import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class MarketFragment extends Fragment implements MarketAdapter.OnTouchMarketListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); //grab the authentification instance
    private FirebaseUser user = mAuth.getCurrentUser(); //get the user
    private String userID = user.getUid(); //ID of user
    RecyclerView recyclerView;
    ArrayList<Market> marketArrayList;
    MarketAdapter marketAdapter;
    FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();;


    public MarketFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirestore = FirebaseFirestore.getInstance();
        marketArrayList = new ArrayList<>();
        marketAdapter = new MarketAdapter(getContext(), marketArrayList,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_market, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_market);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        marketArrayList = new ArrayList<>();
        marketAdapter=new MarketAdapter(this.getContext(),marketArrayList,this);
        recyclerView.setAdapter(marketAdapter);

        mFirestore.collection("User").document(userID).collection("Market").orderBy("favorite").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String, Object> docData = document.getData();
                                Log.d("marketTest", ""+docData);
                                Log.d("marketTest", ""+docData.get("marketLogo"));
                                Log.d("marketTest", ""+(String)docData.get("marketLogo"));
                                Market market = document.toObject(Market.class);
                                Log.d("marketTest", ""+market.getMarketLogo());
                                marketArrayList.add(market); //cast the fetched document into an market object and add this market to the list
                            }
                            marketAdapter.notifyDataSetChanged(); //to update le recyclerview when new market
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        return view;

    }

    @Override
    public void onTouchMarket(int position) {
        Intent intent = new Intent(this.getContext(),TicketActivity.class);
        intent.putExtra("marketName",marketArrayList.get(position).getMarketName());
        intent.putExtra("marketId",marketArrayList.get(position).getMarketId());
        startActivity(intent);
    }

}