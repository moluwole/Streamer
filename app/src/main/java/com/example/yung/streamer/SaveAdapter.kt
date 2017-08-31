package com.example.yung.streamer

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import java.io.File


/**
 * Created by yung on 7/11/17.
 */
class SaveAdapter constructor(mList: ArrayList<String>?) : RecyclerView.Adapter<SaveAdapter.SaveView>() {

    var mList: ArrayList<String>? = null
    var app_context: Context? = null

    init {
        this.mList = mList
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SaveView {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.saved_video_list, parent, false)
        val vh = SaveView(view)
        app_context = view.context
        return vh
    }

    override fun onBindViewHolder(holder: SaveView, position: Int) {
        holder.saved_video.text = mList?.get(position)
        val video_name = mList?.get(position)
        val file: File = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).path.toString(), ".streamer/" + video_name)
        val bMap = ThumbnailUtils.createVideoThumbnail(file.toString(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND)

        holder.saved_image.setImageBitmap(bMap)

        holder.saved_video.setOnClickListener {
            //            val file: File = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).path.toString(), ".streamer/" + video_name)

            val intent = Intent(app_context as Home, OfflinePlay::class.java)
            intent.putExtra("File", Uri.fromFile(file).toString())
            // var intent = Intent(Intent.ACTION_VIEW)
            // intent.setDataAndType(Uri.fromFile(file), "video/*")

            app_context?.startActivity(intent)
        }

        var builder: AlertDialog.Builder?
        holder.saved_video.setOnLongClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = AlertDialog.Builder(app_context, android.R.style.Theme_Material_Dialog_Alert)
            } else {
                builder = AlertDialog.Builder(app_context)
            }
            builder?.setTitle("Delete Video")
                    ?.setMessage("Are you sure you want to delete this video?")
                    ?.setPositiveButton(android.R.string.yes) { _, _ ->
                        val file: File = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).path.toString(), ".streamer/" + video_name)
                        file.delete()
                        mList?.removeAt(position)
                        notifyItemRemoved(position)
                        notifyDataSetChanged()
                    }
                    ?.setNegativeButton(android.R.string.no) { _, _ ->
                        // do nothing
                    }
                    ?.setIcon(android.R.drawable.ic_dialog_alert)
                    ?.show()

            true
        }
    }

    override fun getItemCount(): Int {
        return mList?.count() ?: 0
    }

    class SaveView(layoutView: View) : RecyclerView.ViewHolder(layoutView) {
        var saved_video = layoutView.findViewById(R.id.saved_videos_title) as TextView
        var saved_image = layoutView.findViewById(R.id.saved_video_thumbnail) as ImageView
    }
}