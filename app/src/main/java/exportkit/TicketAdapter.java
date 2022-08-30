package exportkit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
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

        View v= LayoutInflater.from(context).inflate(R.layout.dynamic_ticket_button_layout,parent,false);

        return new MyViewHolder(v,mOnTouchTicketListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TicketReciever ticket = ticketArrayList.get(position);
        Log.d("marketTest", "market title = "+ticket.getTitle());
        if(ticket.getTitle()==null) {
            holder.title.setVisibility(View.GONE);
        }else{
            holder.title.setText(ticket.getTitle());
        }
        holder.description.setText(ticket.getDescription());
        holder.date.setText(dateFormat(ticket.getDate())); //set the content of the date with a formated date
    }

    private String dateFormat(Date date){
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM  hh:mm");
        String strDate = dateFormat.format(date);
        return strDate;
    }


    @Override
    public int getItemCount() {
        return ticketArrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        TextView description;
        TextView date;

        OnTouchTicketListener onTouchTicketListener;
        public MyViewHolder(@NonNull View itemView,OnTouchTicketListener touchMarketListener) {
            super(itemView);
            title = itemView.findViewById(R.id.title_dynamic_ticket_button);
            description=itemView.findViewById(R.id.description_dynamic_ticket_button);
            date = itemView.findViewById(R.id.date_dynamic_ticket_button);

            this.onTouchTicketListener = touchMarketListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTouchTicketListener.onTouchTicket(getAdapterPosition());
        }
    }

    public interface OnTouchTicketListener{
        void onTouchTicket(int position);
    }
}
