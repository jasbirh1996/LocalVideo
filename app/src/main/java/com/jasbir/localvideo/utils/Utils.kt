package com.jasbir.localvideo.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.material.tabs.TabLayout
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import javax.inject.Inject

class Utils @Inject constructor() {

    fun getfileduration(uri: Uri?,context : Context): Long {
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