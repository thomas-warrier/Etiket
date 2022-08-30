package exportkit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import exportkit.figma.R;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<Market> marketArrayList;
    private OnTouchMarketListener mOnTouchMarketListener;


    public MarketAdapter(Context context, ArrayList<Market> marketArrayList,OnTouchMarketListener onTouchMarketListener) {
        this.context = context;
        this.marketArrayList = marketArrayList;
        this.mOnTouchMarketListener=onTouchMarketListener;
    }

    @NonNull
    @Override
    public MarketAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(context).inflate(R.layout.market_recyclerview_layout,parent,false);

        return new MyViewHolder(v,mOnTouchMarketListener);
    }

    private String dateFormat(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM  hh:mm");
        return dateFormat.format(date);
    }

    @Override
    public void onBindViewHolder(@NonNull MarketAdapter.MyViewHolder holder, int position) {
        Market market = marketArrayList.get(position);
        Utils.fetchSvg(context, market.getMarketLogo(), holder.marketLogo);
        holder.dateOfLastTicket.setText(dateFormat(market.getDateOfLastTicket())); //set the content of the date with a formated date
    }

    @Override
    public int getItemCount() {
        return marketArrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView dateOfLastTicket;
        ImageView marketLogo;
        OnTouchMarketListener onTouchMarketListener;
        public MyViewHolder(@NonNull View itemView,OnTouchMarketListener touchMarketListener) {
            super(itemView);
            dateOfLastTicket = itemView.findViewById(R.id.date_dynamic_market_button);
            marketLogo = itemView.findViewById(R.id.image_dynamic_market_button);
            this.onTouchMarketListener = touchMarketListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTouchMarketListener.onTouchMarket(getAdapterPosition());
        }
    }

    public interface OnTouchMarketListener{
        void onTouchMarket(int position);
    }
}
