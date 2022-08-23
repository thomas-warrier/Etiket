package exportkit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import exportkit.figma.R;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.MyViewHolder> {
    Context context;
    ArrayList<Market> marketArrayList;

    public MarketAdapter(Context context, ArrayList<Market> marketArrayList) {
        this.context = context;
        this.marketArrayList = marketArrayList;
    }

    @NonNull
    @Override
    public MarketAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(context).inflate(R.layout.market_recyclerview_layout,parent,false);

        return new MyViewHolder(v);
    }

    private String dateFormat(Date date){
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM  hh:mm");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    @Override
    public void onBindViewHolder(@NonNull MarketAdapter.MyViewHolder holder, int position) {
        Market market = marketArrayList.get(position);
        Picasso.get().load(market.getMarketLogo()).into(holder.marketLogo);//set the logo content
        holder.date.setText("Dernier Ticket : "+ dateFormat(market.getDate())); //set the content of the date with a formated date
    }

    @Override
    public int getItemCount() {
        return marketArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        ImageView marketLogo;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date_dynamic_market_button);
            marketLogo = itemView.findViewById(R.id.image_dynamic_market_button);
        }
    }
}
