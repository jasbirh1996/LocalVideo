package com.jasbir.localvideo.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.jasbir.localvideo.R
import com.jasbir.localvideo.data.GalleryVideo_Get_Set
import com.jasbir.localvideo.utils.Utils
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var gAdapter: GalleryVideos_Adapter
    private lateinit var data_list: ArrayList<GalleryVideo_Get_Set>
    private lateinit var rv: RecyclerView
    private lateinit var searchView: SearchView

    @Inject
     lateinit var utils :Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        data_list = ArrayList<GalleryVideo_Get_Set>()
        rv = findViewById(R.id.recylerview)
        searchView = findViewById(R.id.search_bar)
        searchView.setOnQueryTextListener(this)


        if (getResources()
                .getConfiguration().orientation === Configuration.ORIENTATION_PORTRAIT
        ) {
            rv.setLayoutManager(GridLayoutManager(this, 2))
        } else {
            rv.setLayoutManager(GridLayoutManager(this, 3))
        }
        rv.setHasFixedSize(true)
        starter(this)



    }


    fun getAllVideoPath(context: Context) {
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Video.VideoColumns.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val item = GalleryVideo_Get_Set()
                item.video_path = cursor.getString(0)
                item.video_duration_ms =   utils.getfileduration(Uri.parse((cursor.getString(0))),context)

                Log.d("resp", "" + item.video_duration_ms)
                item.video_time = utils.change_sec_to_time(item.video_duration_ms)


                data_list.add(item)
            }
            gAdapter.notifyDataSetChanged()
            cursor.close()
        }
    }
    private fun starter(context: Context) {
        Dexter.withContext(context).withPermissions(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report!!.areAllPermissionsGranted()) {
                    //                    permission grant


                    gAdapter = GalleryVideos_Adapter(context, data_list)
                    getAllVideoPath(context)
                    rv.adapter = gAdapter


                } else {
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // go to setting
                        showAlDialog()

                    } else {
                        starter(context)
                    }
                }

            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                token!!.continuePermissionRequest()
            }

        }).withErrorListener {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
        }.onSameThread().check()

    }

    private fun showAlDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Important Permission")
            .setMessage("You must give all Permissions")
            .setPositiveButton("Go To Setting") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", packageName, null)
                startActivityForResult(intent, 101)
            }.setCancelable(false)
            .show()
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {


        return false
    }


}