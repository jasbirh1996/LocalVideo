package com.jasbir.localvideo.data

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.jasbir.localvideo.utils.Utils
import java.util.ArrayList
import javax.inject.Inject


class Repository @Inject constructor(
    val util : Utils
) {



    fun getAllVideoPath(context: Context) : List<GalleryVideo_Get_Set> {
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Video.VideoColumns.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)

        var data_list: ArrayList<GalleryVideo_Get_Set> =ArrayList<GalleryVideo_Get_Set>()


        if (cursor != null) {

            while (cursor.moveToNext()) {
                val item = GalleryVideo_Get_Set()
                item.video_path = cursor.getString(0)
                item.video_duration_ms = util.getfileduration(Uri.parse(cursor.getString(0)),context)

                item.video_time = util.change_sec_to_time(item.video_duration_ms)


                item.video_title =    "file name"

                data_list.add(item)
            }
//            gAdapter.notifyDataSetChanged()
            cursor.close()
        }
        return data_list
    }
    fun getfileduration(uri: Uri?, context: Context): Long {
        try {
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(context, uri)
            val durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val file_duration = durationStr?.toInt()
            return file_duration!!.toLong()
        } catch (e: java.lang.Exception) {
        }
        return 0
    }
    fun change_sec_to_time(file_duration: Long): String? {
        val second = file_duration / 1000 % 60
        val minute = file_duration / (1000 * 60) % 60
        return String.format("%02d:%02d", minute, second)

    }

}