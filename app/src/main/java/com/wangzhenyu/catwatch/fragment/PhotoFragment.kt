package com.wangzhenyu.catwatch.fragment

import android.Manifest
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.wangzhenyu.catwatch.R
import com.wangzhenyu.catwatch.adapter.PhotoListAdapter
import com.wangzhenyu.catwatch.data.GirlPhoto
import com.wangzhenyu.catwatch.databinding.FPhotoFragmentBinding
import com.wangzhenyu.catwatch.util.LogUtil
import com.wangzhenyu.catwatch.util.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class PhotoFragment : Fragment() {

    companion object {
        const val internalStorageFileName: String = "internal.txt"
        const val internalStorageFileContent: String = "Android 开发者\n" +
                "文档\n" +
                "指南\n" +
                "请求应用权限\n" +
                "您可能无法从当前所在的区域访问此资源。\n" +
                "每款 Android 应用都在访问权受限的沙盒中运行。如果您的应用需要使用自身沙盒以外的资源或信息，您可以声明权限并设置提供此访问权限的权限请求。以下步骤是权限使用工作流程的一部分。\n" +
                "\n" +
                "如果您声明了任何危险权限，并且您的应用安装在搭载 Android 6.0（API 级别 23）或更高版本的设备上，那么您必须按照本指南中的步骤在运行时请求这些危险权限。\n" +
                "\n" +
                "如果您没有声明任何危险权限，或者您的应用安装在搭载 Android 5.1（API 级别 22）或更低版本的设备上，则系统会自动授予相应的权限，您无需完成本页剩下的任何步骤。\n" +
                "\n" +
                "基本原则"

        const val uri: String =
            "https://cn.bing.com/th?id=OHR.LoftedMadagascar_ZH-CN0062899981_1920x1080.jpg&amp;rf=LaDigue_1920x1080.jpg&amp;pid=hp"
        const val uri1 =
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fhuafans.dbankcloud.com%2Fpic%2F2017%2F01%2F25%2F195116eebdd5aa2fdaebff832280a391_IMG_20170125_153017.jpg%3Fmode%3Ddownload&refer=http%3A%2F%2Fhuafans.dbankcloud.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1619073600&t=a6668fff195cd5b608e40c328b189b27"
        const val uri2 = ""
    }

    private lateinit var viewModel: PhotoViewModel
    private lateinit var binding: FPhotoFragmentBinding

    private lateinit var adapter: PhotoListAdapter

    private val list = ArrayList<GirlPhoto>()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.photo_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.create_file -> {
                lifecycleScope.launch(Dispatchers.IO)
                {
                    try {
                        File(
                            requireContext().getDir("photo", Context.MODE_PRIVATE),
                            "create_internal.png"
                        ).outputStream().buffered().use {
                            val bis = BufferedInputStream(loadPhoto(uri))
                            val buffer = ByteArray(1024 * 8)
                            var bytes = bis.read(buffer)
                            while (bytes >= 0) {
                                it.write(buffer, 0, bytes)
                                bytes = bis.read(buffer)
                            }
                            bis.close()
                        }
                    } catch (e: Exception) {
                        LogUtil.d("StorageABC", "创建内部存储文件失败")
                    }
                }
                true
            }
            R.id.open_file -> {
                lifecycleScope.launch(Dispatchers.IO)
                {
                    val openFileUrl: String =
                        "${requireContext().filesDir.path}/create_internal.png"
                    LogUtil.d("StorageABC", openFileUrl)
                    list.add(GirlPhoto(openFileUrl))
                    adapter.submitList(list)
                }
                true
            }

            R.id.create_file_external -> {
                lifecycleScope.launch(Dispatchers.IO)
                {
                    try {
                        File(requireContext().getExternalFilesDir("photo"), "external.png")
                            .outputStream().buffered().use {
                                val bis = BufferedInputStream(loadPhoto(uri))
                                val buffer = ByteArray(1024 * 8)
                                var bytes = bis.read(buffer)
                                while (bytes >= 0) {
                                    it.write(buffer, 0, bytes)
                                    bytes = bis.read(buffer)
                                }
                                bis.close()
                            }
                    } catch (e: Exception) {
                        LogUtil.d("StorageABC", "创建外部存储文件失败")
                    }

                }
                true
            }
            R.id.open_file_external -> {
                lifecycleScope.launch(Dispatchers.IO)
                {
                    val externalURL =
                        "${requireContext().getExternalFilesDir("photo")}/external.png"
                    list.clear()
                    list.add(GirlPhoto(externalURL))
                    adapter.submitList(list)
                }

                true
            }

            R.id.photo_menu_one -> {
                lifecycleScope.launch(Dispatchers.IO)
                {
                    if (Build.VERSION.SDK_INT > 23) {

                        val checkList = arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        val needRequestPermission = checkPermission(requireContext(), checkList)
                        LogUtil.d("StorageABC", needRequestPermission.toString())
                        if (needRequestPermission.isEmpty()) {
                            LogUtil.d("StorageABC", "已经获得所有权限")
                            getPhoto()
                        } else {
                            requestPermissions(needRequestPermission.toTypedArray(), 1)
                        }
                    } else {
                        getPhoto()
                    }

                }

//                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//                intent.addCategory(Intent.CATEGORY_OPENABLE)
//                intent.type = "image/jpeg"
//                startActivityForResult(intent, 100)

                true
            }
            R.id.photo_menu_two -> {
                lifecycleScope.launch(Dispatchers.IO)
                {
                    val list: ArrayList<GirlPhoto> =
                        arguments?.getParcelableArrayList("PhotoList")!!
                    val position: Int = arguments?.getInt("PhotoPosition") ?: 0
                    val url: String = list[position].url
                    Glide.with(this@PhotoFragment).asBitmap().load(url)
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                addBitmapToAlbum(
                                    resource,
                                    "photo${position}.png",
                                    "image/png",
                                    Bitmap.CompressFormat.PNG
                                )
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }

                        })
                }
                true
            }
            R.id.photo_menu_three -> {
                lifecycleScope.launch(Dispatchers.IO)
                {

                }
                true
            }
            R.id.photo_menu_four -> {
                lifecycleScope.launch(Dispatchers.IO)
                {

                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //将位图添加到图库中
    private fun addBitmapToAlbum(
        bitmap: Bitmap,
        displayName: String,
        mimeType: String,
        compressFormat: Bitmap.CompressFormat
    ) {
        val values = ContentValues()
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
        } else {
            values.put(
                MediaStore.MediaColumns.DATA,
                "${Environment.getExternalStorageDirectory().path}/${Environment.DIRECTORY_DCIM}/$displayName"
            )
        }
        val uri = MyApplication.context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )
        if (uri != null) {
            val outputStream = MyApplication.context.contentResolver.openOutputStream(uri)
            if (outputStream != null) {
                bitmap.compress(compressFormat, 100, outputStream)
                outputStream.close()
            }
        }
    }

    //检查权限，返回需要申请的权限
    private fun checkPermission(context: Context, checkList: Array<String>): ArrayList<String> {
        val list: ArrayList<String> = ArrayList<String>()
        for (i in 1..checkList.size) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(
                    context,
                    checkList[i - 1]
                )
            ) {
                list.add(checkList[i - 1])
            }
        }
        return list
    }

    //将图片添加到图库中
    private fun writeInputStreamToAlbum(
        inputStream: InputStream,
        displayName: String,
        mimeType: String
    ) {
        val values = ContentValues()
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
        } else {
            values.put(
                MediaStore.MediaColumns.DATA,
                "${Environment.getExternalStorageDirectory().path}/${Environment.DIRECTORY_DCIM}/$displayName"
            )
        }
        val bis = BufferedInputStream(inputStream)
        val uri = MyApplication.context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )
        if (uri != null) {
            val outputStream = MyApplication.context.contentResolver.openOutputStream(uri)
            if (outputStream != null) {
                val bos = BufferedOutputStream(outputStream)
                val buffer = ByteArray(1024)
                var bytes = bis.read(buffer)
                while (bytes >= 0) {
                    bos.write(buffer, 0, bytes)
                    bos.flush()
                    bytes = bis.read(buffer)
                }
                bos.close()
            }
        }
        bis.close()
    }

    //从网络上加载图片
    private fun loadPhoto(url: String): InputStream {
        val conn: HttpURLConnection = (URL(url).openConnection() as HttpURLConnection)
        conn.connect()
        return conn.inputStream
    }

    private fun getBitmap(url: String): Bitmap {
        val conn: HttpURLConnection = (URL(url).openConnection() as HttpURLConnection)
        conn.connect()
        val inputStream: InputStream = conn.inputStream
        if (inputStream == null) {
            LogUtil.d("AAAA", "inputStream为空")
        }
        val bitmap = BitmapFactory.decodeStream(inputStream)
        conn.disconnect()
        return bitmap
    }

    private fun getPhoto() {
        val list: ArrayList<GirlPhoto> = ArrayList<GirlPhoto>()
        val cursor =
            MyApplication.context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null
            )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id =
                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                val uri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                list.add(GirlPhoto(uri.toString()))
            }
            cursor.close()
            adapter.submitList(list)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 100) {
            //选中返回的图片封装在uri里
            LogUtil.d("StorageABC", data?.data?.toString() ?: "您未获取图片")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty()) {
                    LogUtil.d("StorageABC", grantResults.contentToString())
                    var a: Boolean = true
                    for (i in grantResults) {
                        if (i != PackageManager.PERMISSION_GRANTED) {
                            a = false
                        }
                    }
                    if (!a) {
                        LogUtil.d("StorageABC", "未全部获取权限")
                    } else {
                        getPhoto()
                    }
                } else {
                    LogUtil.d("StorageABC", "未获取权限")
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.f_photo_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PhotoViewModel::class.java)

        setHasOptionsMenu(true)

        val list: ArrayList<GirlPhoto> =
            arguments?.getParcelableArrayList<GirlPhoto>("PhotoList")!!

        binding.photoFragmentTextView.text =
            "${arguments?.getInt("PhotoPosition")?.plus(1) ?: 0}/${list.size ?: 0}"

        adapter = PhotoListAdapter()
        binding.photoFragmentViewPager2.adapter = adapter
        adapter.submitList(list)


        binding.photoFragmentViewPager2.apply {
            setCurrentItem(arguments?.getInt("PhotoPosition") ?: 0, false)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.photoFragmentTextView.text =
                        "${position + 1 ?: 0}/${list.size ?: 0}"
                }
            })
        }

    }

}