package polarpixel.indexcardapp

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlin.math.min
import androidx.recyclerview.widget.RecyclerView

class DrawAdapter(private val drawList: List<Bitmap>) : RecyclerView.Adapter<DrawAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_drawing, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bitmap = drawList[position]
        holder.imageView.setImageBitmap(bitmap)
    }

    override fun getItemCount(): Int {
        return min(drawList.size, 10) // Limit to 10 items
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.drawingImageView)
    }
}


