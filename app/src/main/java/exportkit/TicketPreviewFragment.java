package exportkit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import exportkit.figma.R;


public class TicketPreviewFragment extends Fragment {

    private static boolean editModeState = false;private
    FirebaseAuth mAuth = FirebaseAuth.getInstance(); //grab the authentification instance
    private FirebaseUser user = mAuth.getCurrentUser(); //get the user
    private String userID = user.getUid(); //ID of user
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    public TicketPreviewFragment() {
        // Required empty public constructor
    }

    public static TicketPreviewFragment newInstance(String param1, String param2) {
        TicketPreviewFragment fragment = new TicketPreviewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_ticket_preview, container, false);
        TicketReciever ticket = new TicketReciever(null,null,null,null,false,null);

        ImageButton editMode= view.findViewById(R.id.edit_mode_button);
        TextView title = view.findViewById(R.id.title_ticket_preview);
        TextView description = view.findViewById(R.id.description_ticket_preview);
        EditText titleEditText = view.findViewById(R.id.editText_Title);
        EditText descriptionEditText = view.findViewById(R.id.editText_description);
        ImageButton favorite = view.findViewById(R.id.favorite_ticket_preview_button);
        ImageButton save = view.findViewById(R.id.save_ticket_preview_button);

        //Button
        editMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editModeState==false){
                    titleEditText.setVisibility(View.VISIBLE);
                    descriptionEditText.setVisibility(View.VISIBLE);
                    title.setVisibility(View.GONE);
                    description.setVisibility(View.GONE);
                    favorite.setVisibility(View.GONE);
                    save.setVisibility(View.VISIBLE);
                    editModeState=true;
                }
                else{
                    titleEditText.setVisibility(View.GONE);
                    descriptionEditText.setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                    description.setVisibility(View.VISIBLE);
                    favorite.setVisibility(View.VISIBLE);
                    save.setVisibility(View.GONE);
                    editModeState=false;
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleGetText = titleEditText.getText().toString();
                String descriptionGetText = descriptionEditText.getText().toString();
                title.setText(titleGetText);
                description.setText(descriptionGetText);
                mFirestore.collection("User").document(userID).collection("Market")
                        .document(this.getIntent().getStringExtra("marketId")).collection("Ticket").document(ticket.getTicketId())
                                .update("title",titleGetText,
                                        "description",descriptionGetText
                                );
                titleEditText.setVisibility(View.GONE);
                descriptionEditText.setVisibility(View.GONE);
                title.setVisibility(View.VISIBLE);
                description.setVisibility(View.VISIBLE);
                favorite.setVisibility(View.VISIBLE);
                save.setVisibility(View.GONE);
                editModeState=false;

            }
        });
        //title
        if (ticket.getTitle()==null){
            title.setVisibility(View.GONE);
        }
        else{
            title.setText(ticket.getTitle());
        }
        //editText
        if(ticket.getTitle()==null) {
            titleEditText.setHint("Titre");
        }
        else{
            titleEditText.setText(ticket.getTitle());
        }

        if(ticket.getDescription()==null) {
            descriptionEditText.setHint("Entrer une description");
        }
        else{
            descriptionEditText.setText(ticket.getTitle());
        }
        return view;
    }
}