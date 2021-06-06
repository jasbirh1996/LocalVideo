package com.jasbir.localvideo.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jasbir.localvideo.R
import com.jasbir.localvideo.data.GalleryVideo_Get_Set
import java.io.File


class GalleryVideos_Adapter(
        var context: Context,
        var datalist: List<GalleryVideo_Get_Set>):

        RecyclerView.Adapter<GalleryVideos_Adapter.MyViewHolder>() {




    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var thumb_image: ImageView? = null

        var view_txt: TextView? = null


        init{

            thumb_image = itemView.findViewById<ImageView>(R.id.thumb_image)
            view_txt = itemView.findViewById<TextView>(R.id.view_txt)
        }



    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_galleryvideo_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item: GalleryVideo_Get_Set =   datalist.get(position)

        holder.view_txt!!.setText(item.video_time)
//        val bitmap =
//            ThumbnailUtils.createVideoThumbnail(item.video_path!!, MediaStore.Video.Thumbnails.MICRO_KIND)
//        holder.thumb_image?.setImageBitmap(bitmap)
        holder.thumb_image?.let {
            Glide.with(context)
                    .load(Uri.fromFile(File(item.video_path!!)))
                    .into(it)
        }

        holder.thumb_image?.setOnClickListener {

            val video_path = position
            val intent :Intent = Intent(context, DetailViewActivity::class.java)
            intent.putExtra("path",video_path)
            context.startActivity(intent)
        }



    }

    override fun getItemCount(): Int {
        return datalist.size
    }


}