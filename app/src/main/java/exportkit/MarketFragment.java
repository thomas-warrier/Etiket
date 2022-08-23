package exportkit;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import exportkit.figma.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MarketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MarketFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); //grab the authentification instance
    private FirebaseUser user = mAuth.getCurrentUser(); //get the user
    private String userID = user.getUid(); //ID of user
    RecyclerView recyclerView;
    ArrayList<Market> marketArrayList;
    MarketAdapter marketAdapter;
    FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MarketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MarketFragment newInstance(String param1, String param2) {
        MarketFragment fragment = new MarketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mFirestore = FirebaseFirestore.getInstance();
        marketArrayList = new ArrayList<Market>();
        marketAdapter = new MarketAdapter(getContext(), marketArrayList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        recyclerView = getView().findViewById(R.id.recycler_view_market);
        recyclerView.setHasFixedSize(true); //set the size
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        marketArrayList = new ArrayList<>();
        marketAdapter=new MarketAdapter(this.getContext(),marketArrayList);
        recyclerView.setAdapter(marketAdapter);

        mFirestore.collection("User").document(userID).collection("Market").orderBy("favorite").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                marketArrayList.add(document.toObject(Market.class)); //cast the fetched document into an market object and add this market to the list
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        marketAdapter.notifyDataSetChanged(); //to update le recyclerview when new market
                    }
                });

        return inflater.inflate(R.layout.fragment_market, container, false);

    }

}