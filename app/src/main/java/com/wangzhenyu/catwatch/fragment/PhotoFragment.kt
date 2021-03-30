package com.wangzhenyu.catwatch.fragment

import android.Manifest
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
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
import com.wangzhenyu.catwatch.data.Hit
import com.wangzhenyu.catwatch.databinding.FPhotoFragmentBinding
import com.wangzhenyu.catwatch.util.LogUtil
import com.wangzhenyu.catwatch.util.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*


class PhotoFragment : Fragment() {


    private lateinit var viewModel: PhotoViewModel
    private lateinit var binding: FPhotoFragmentBinding

    private lateinit var adapter: PhotoListAdapter

    private val readList: ArrayList<Hit> = ArrayList<Hit>()

    private lateinit var list: ArrayList<Hit>
    private var listPosition: Int = 0

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.photo_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            //保存图片到内部存储
            R.id.create_file -> {
                lifecycleScope.launch(Dispatchers.IO)
                {
                    try {
                        requireContext().filesDir

                        val outputStream = File(
                            requireContext().getDir("Photo", Context.MODE_PRIVATE),
                            "photo${listPosition + 1}.png"
                        ).outputStream()
                        //将drawable转化为bitmap并保存
//                        BitmapFactory.decodeResource(resources, R.drawable.one)
//                            .compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//                        outputStream.close()
                        Glide.with(this@PhotoFragment).asBitmap()
                            .load(list[listPosition].largeImageURL)
                            .into(object : SimpleTarget<Bitmap>() {
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap>?
                                ) {
                                    resource.compress(
                                        Bitmap.CompressFormat.PNG,
                                        100,
                                        outputStream as OutputStream
                                    )
                                }

                            })
                        outputStream.close()

                    } catch (e: Exception) {
                        LogUtil.d("StorageABC", "创建内部存储文件失败")
                    }
                }
                true
            }
            //从内部存储读取图片
            R.id.open_file -> {
                lifecycleScope.launch(Dispatchers.IO)
                {
                    val openFileUrl: String =
                        "${requireContext().filesDir.path}/create_internal.png"
                    LogUtil.d("StorageABC", openFileUrl)
                    // list.add(Hit())
                    // adapter.submitList(list)
                }
                true
            }

            R.id.create_file_external -> {
                lifecycleScope.launch(Dispatchers.IO)
                {
//                    try {
//                        File(requireContext().getExternalFilesDir("photo"), "external.png")
//                            .outputStream().buffered().use {
//                              //  val bis = BufferedInputStream(loadPhoto(uri))
//                                val buffer = ByteArray(1024 * 8)
//                                var bytes = bis.read(buffer)
//                                while (bytes >= 0) {
//                                    it.write(buffer, 0, bytes)
//                                    bytes = bis.read(buffer)
//                                }
//                                bis.close()
//                            }
//                    } catch (e: Exception) {
//                        LogUtil.d("StorageABC", "创建外部存储文件失败")
//                    }

                }
                true
            }
            R.id.open_file_external -> {
//                lifecycleScope.launch(Dispatchers.IO)
//                {
//                    val externalURL =
//                        "${requireContext().getExternalFilesDir("photo")}/external.png"
//                    list.clear()
//                    list.add(Hit(externalURL))
//                    adapter.submitList(list)
//                }

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
                true
            }
            R.id.photo_menu_two -> {
                lifecycleScope.launch(Dispatchers.IO)
                {
                    val url: String = list[listPosition].largeImageURL
                    Glide.with(this@PhotoFragment).asBitmap().load(url)
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                addBitmapToAlbum(
                                    resource,
                                    "photo${listPosition}.png",
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
                lifecycleScope.launch(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "保存图片成功", Toast.LENGTH_SHORT).show()
                }
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

    private fun getPhoto() {
        val cursor =
            MyApplication.context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null
            )
        if (cursor != null) {
            readList.clear()
            while (cursor.moveToNext()) {
                val id =
                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                val uri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                readList.add(Hit(largeImageURL = uri.toString()))
            }
            cursor.close()
            adapter.submitList(readList)
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

        list = arguments?.getParcelableArrayList<Hit>("PhotoList")!!
        listPosition = arguments?.getInt("PhotoPosition") ?: 0
        binding.photoFragmentTextView.text =
            "${listPosition.plus(1) }/${list.size }"

        adapter = PhotoListAdapter()
        binding.photoFragmentViewPager2.adapter = adapter
        adapter.submitList(list)


        binding.photoFragmentViewPager2.apply {
            setCurrentItem(listPosition, false)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.photoFragmentTextView.text =
                        "${position + 1 }/${list.size }"
                }
            })
        }

    }

}