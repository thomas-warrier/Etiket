package exportkit
/*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.barteksc.pdfviewer.PDFView
import exportkit.figma.R

class ViewPagerAdapter(private var linkImage:ArrayList<String>) : RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val pdfViewerItem: PDFView = itemView.findViewById(R.id.pdf_view_item)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapter.Pager2ViewHolder {
        return Pager2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pdf_viewer,parent,false))
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.Pager2ViewHolder, position: Int) {
        Utils.loadPdfFromUrl(linkImage[position],holder.pdfViewerItem)
    }

    override fun getItemCount(): Int {
        return linkImage.size
    }
}
 */