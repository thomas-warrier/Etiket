package exportkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import exportkit.figma.R;
import me.relex.circleindicator.CircleIndicator2;
import me.relex.circleindicator.CircleIndicator3;

public class TicketPreviewActivity extends AppCompatActivity {
    private static boolean editModeState = false;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); //grab the authentification instance
    private FirebaseUser user = mAuth.getCurrentUser(); //get the user
    private String userID = user.getUid(); //ID of user
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private static ArrayList<String> imageList ;

    private String dateFormat(Date date){
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM  hh:mm");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_preview);
        Date date = new Date(getIntent().getLongExtra("date",0));
        TicketReciever ticket = new TicketReciever(getIntent().getStringExtra("title"),getIntent().getStringExtra("description"),new Timestamp(date),getIntent().getBooleanExtra("favorite",false),getIntent().getStringExtra("ticketId"));
        ticket.setImageArray(getIntent().getStringArrayListExtra("imageUrlArray"));

        ImageButton editMode= findViewById(R.id.edit_mode_button);
        TextView title = findViewById(R.id.title_ticket_preview);
        TextView dateText = findViewById(R.id.date_ticket_preview);
        TextView description = findViewById(R.id.description_ticket_preview);
        EditText titleEditText = findViewById(R.id.editText_Title);
        EditText descriptionEditText = findViewById(R.id.editText_description);
        ImageButton favorite = findViewById(R.id.favorite_ticket_preview_button);
        ImageButton save = findViewById(R.id.save_ticket_preview_button);

        //title
        if (ticket.getTitle()==null){
            title.setVisibility(View.GONE);
        }
        else{
            title.setText(ticket.getTitle());
        }
        dateText.setText(dateFormat(ticket.getDate()));
        description.setText(ticket.getDescription());


        if(ticket.isFavorite()==false) {
            favorite.setImageResource(R.drawable.ic_favoris_nav);
        }
        else {
            favorite.setImageResource(R.drawable.ic_favorite_yellow);
        }


        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ticket.isFavorite()==false){
                    favorite.setImageResource(R.drawable.ic_favorite_yellow);
                    ticket.setFavorite(true);
                    mFirestore.collection("User").document(userID).collection("Market").document(getIntent().getStringExtra("marketId")).collection("Ticket").document(ticket.getTicketId()).update("favorite",true);
                }
                else{
                    favorite.setImageResource(R.drawable.ic_favoris_nav);
                    ticket.setFavorite(false);
                    mFirestore.collection("User").document(userID).collection("Market").document(getIntent().getStringExtra("marketId")).collection("Ticket").document(ticket.getTicketId()).update("favorite",false);
                }
            }
        });
        //Button
        editMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editModeState==false){
                    titleEditText.setVisibility(View.VISIBLE);
                    descriptionEditText.setVisibility(View.VISIBLE);
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
                        descriptionEditText.setText(ticket.getDescription());
                    }

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
                title.setVisibility(View.VISIBLE);
                description.setVisibility(View.VISIBLE);
                title.setText(titleGetText);
                description.setText(descriptionGetText);
                ticket.setTitle(titleGetText);
                ticket.setDescription(descriptionGetText);
                mFirestore.collection("User").document(userID).collection("Market")
                        .document(getIntent().getStringExtra("marketId")).collection("Ticket").document(ticket.getTicketId())
                        .update("title",titleGetText,
                                "description",descriptionGetText
                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(),"Modification enregistrée",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Enregistrement des modifications echouées",Toast.LENGTH_LONG).show();
                            }
                        });
                titleEditText.setVisibility(View.GONE);
                descriptionEditText.setVisibility(View.GONE);
                favorite.setVisibility(View.VISIBLE);
                save.setVisibility(View.GONE);
                editModeState=false;

            }
        });

        imageList = getIntent().getStringArrayListExtra("imageUrlArray");
        Log.d("imageListTest", "image list :"+imageList);
        ViewPager2 viewPager2 = findViewById(R.id.view_pager_ticket_preview);
        ViewPagerAdapterJava viewPagerAdapter = new ViewPagerAdapterJava(imageList);
        viewPager2.setAdapter(viewPagerAdapter);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);


        CircleIndicator3 indicator=(CircleIndicator3) findViewById(R.id.indicator_viewpager);
        indicator.setViewPager(viewPager2);



    }
}