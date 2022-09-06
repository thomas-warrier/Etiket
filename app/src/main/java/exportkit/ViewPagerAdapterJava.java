package exportkit;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;

import java.util.ArrayList;

import exportkit.figma.R;

public class ViewPagerAdapterJava extends RecyclerView.Adapter<ViewPagerAdapterJava.Pager2ViewHolder>{
    private ArrayList<String> linkImage;
    public ViewPagerAdapterJava(ArrayList<String> linkImage){
        this.linkImage=linkImage;
    }


    @NonNull
    @Override
    public ViewPagerAdapterJava.Pager2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Pager2ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pdf_viewer,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerAdapterJava.Pager2ViewHolder holder, int position) {
        Utils.loadPdfFromUrl(linkImage.get(position), holder.pdfView);
        Log.d("TestPDF", "position create " + position);

    }

    @Override
    public int getItemCount() {
        return linkImage.size();
    }

    public class Pager2ViewHolder extends RecyclerView.ViewHolder{
        public Pager2ViewHolder(View itemView){
            super(itemView);
        }
        PDFView pdfView= itemView.findViewById(R.id.pdf_view_item);
    }


}
