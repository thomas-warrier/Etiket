package exportkit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import exportkit.figma.R;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<TicketReciever> ticketArrayList;
    private OnTouchTicketListener mOnTouchTicketListener;

    public TicketAdapter(Context context, ArrayList<TicketReciever> ticketArrayList, OnTouchTicketListener onTouchTicketListener) {
        this.context = context;
        this.ticketArrayList = ticketArrayList;
        this.mOnTouchTicketListener=onTouchTicketListener;
    }

    @NonNull
    @Override
    public TicketAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.dynamic_ticket_button_layout,parent,false);

        return new MyViewHolder(v,mOnTouchTicketListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TicketReciever ticket = ticketArrayList.get(position);
        Log.d("marketTest", "market title = "+ticket.getTitle());
        if(ticket.isFavorite()==false){
            holder.favorite.setImageResource(R.drawable.ic_favoris_nav);
        }
        else{
            holder.favorite.setImageResource(R.drawable.ic_favorite_yellow);
        }
        if(ticket.getTitle()==null) {
            holder.title.setVisibility(View.GONE);
        }else{
            holder.title.setText(ticket.getTitle());
        }
        holder.description.setText(ticket.getDescription());
        holder.date.setText(DateFormat.dateDeduction(ticket.getDate())); //set the content of the date with a formated date
    }


    @Override
    public int getItemCount() {
        return ticketArrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        TextView description;
        TextView date;
        ImageButton favorite;
        OnTouchTicketListener onTouchTicketListener;
        public MyViewHolder(@NonNull View itemView, OnTouchTicketListener touchMarketListener) {
            super(itemView);
            title = itemView.findViewById(R.id.title_dynamic_ticket_button);
            description=itemView.findViewById(R.id.description_dynamic_ticket_button);
            date = itemView.findViewById(R.id.date_dynamic_ticket_button);
            favorite = itemView.findViewById(R.id.favorite_ticket_activity_button);

            this.onTouchTicketListener = touchMarketListener;
            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onTouchTicketListener.onTouchFavorite(getAdapterPosition());
                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTouchTicketListener.onTouchTicket(getAdapterPosition());
        }
    }

    public interface OnTouchTicketListener{
        void onTouchTicket(int position);
        void onTouchFavorite(int position);
    }

}
