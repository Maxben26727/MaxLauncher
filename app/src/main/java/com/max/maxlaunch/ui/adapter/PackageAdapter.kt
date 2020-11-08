package com.max.maxlaunch.ui.adapter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.max.maxlaunch.databinding.AppItemLayoutBinding
import com.max.maxlaunch.ui.AppInfo
import com.max.maxlaunch.ui.HomeViewModel
import java.lang.Exception


class PackageAdapter(context: Context) : RecyclerView.Adapter<PackageAdapter.PackageViewHolder>() {
      var packageList: ArrayList<AppInfo> = ArrayList()

    val mctx = context
   init {
       loadApps()
    }
    private fun loadApps() {
        packageList.clear()
        val pm: PackageManager = mctx.packageManager

        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)

        val allApps = pm.queryIntentActivities(i, 0)
        for (ri in allApps) {
            val app =AppInfo(ri.loadLabel(pm), ri.activityInfo.packageName, ri.activityInfo.loadIcon(pm))
            packageList.add(app)
        }
        packageList.sortBy {
            it.label.toString()
        }
    }
    inner class PackageViewHolder(val binding: AppItemLayoutBinding) : RecyclerView.ViewHolder(
            binding.root
    )
    {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageViewHolder {
        val binding = AppItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )
        return PackageViewHolder(
                binding
        )
    }

    override fun onBindViewHolder(holder: PackageViewHolder, position: Int) {
     holder.binding.appname.text = packageList[position].label
        holder.binding.icon.setImageDrawable(packageList[position].icon)
        holder.binding.root.setOnClickListener {
            val launchIntent =
                mctx.packageManager.getLaunchIntentForPackage(packageList.get(position).packageName.toString())
           try {
               mctx.startActivity(launchIntent)
           } catch (e:Exception)
           {
               Toast.makeText(mctx,"App Not Installed or Uninstalled Recently",Toast.LENGTH_LONG).show()
               loadApps()
               notifyDataSetChanged()
           }

        }
        holder.binding.root.setOnLongClickListener {
            val intent = Intent(Intent.ACTION_DELETE)
            intent.data = Uri.parse("package:"+packageList.get(position).packageName)
            mctx.startActivity(intent)
            return@setOnLongClickListener true
        }

    }

    override fun getItemCount(): Int {
        return packageList.size
    }
    private fun PackageAdapter() {

    }
}