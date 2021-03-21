package com.wangzhenyu.catwatch.fragment

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.wangzhenyu.catwatch.R
import com.wangzhenyu.catwatch.adapter.PhotoListAdapter
import com.wangzhenyu.catwatch.data.GirlPhoto
import com.wangzhenyu.catwatch.databinding.FPhotoFragmentBinding
import com.wangzhenyu.catwatch.util.LogUtil
import com.wangzhenyu.catwatch.util.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

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
    }

    private lateinit var viewModel: PhotoViewModel
    private lateinit var binding: FPhotoFragmentBinding

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
                        requireContext().openFileOutput(
                            internalStorageFileName,
                            Context.MODE_PRIVATE
                        ).buffered().use {
                            it.write(internalStorageFileContent.toByteArray(Charsets.UTF_8))
                        }
                    } catch (e: Exception) {
                        LogUtil.d("StorageABC", "读取失败")
                    }

                    try {
                        File(context?.cacheDir, "123.txt").outputStream().bufferedWriter()
                            .use {
                                it.write("外部存储分为两部分：自带外部存储和扩展外部存储(外置SD卡)")
                            }
                    } catch (e: Exception) {

                    }

                }
                true
            }
            R.id.open_file -> {
                lifecycleScope.launch(Dispatchers.IO)
                {
                    try {
                        requireContext().openFileInput(
                            internalStorageFileName
                        ).bufferedReader().useLines {
                            val a = it.fold("WZY") { a, b ->
                                "$a $b"
                            }

                            LogUtil.d("StorageABC", a)
                        }
                    } catch (e: Exception) {
                        LogUtil.d("StorageABC", "读取失败")
                    }
                }

                val files: Array<String> = requireContext().fileList()
                LogUtil.d("StorageABC", files.contentToString())
                try {
                    File(requireContext().cacheDir, "123.txt")
                        .inputStream().bufferedReader()
                        .use {
                            val a = it.readText()
                            LogUtil.d("StorageABC", a)
                        }

                } catch (e: Exception) {
                }
                true
            }

            R.id.create_file_external -> {

                lifecycleScope.launch(Dispatchers.IO)
                {
                    try {
                        val appSpecificExternalDir =
                            File(
                                ContextCompat.getExternalCacheDirs(MyApplication.context)[1],
                                "111.txt"
                            ).outputStream()
                                .bufferedWriter().use {
                                    it.write(internalStorageFileContent)
                                }

                    } catch (e: Exception) {

                    }
                }

                true
            }
            R.id.open_file_external -> {
                lifecycleScope.launch(Dispatchers.IO)
                {
                    File(ContextCompat.getExternalCacheDirs(MyApplication.context)[1], "111.txt")
                        .inputStream().bufferedReader()
                        .useLines {
                            val x = it.fold("Wzy") { a, b ->
                                "$a$b"
                            }
                            LogUtil.d("StorageABC", x)
                        }
                }

                true
            }


            R.id.photo_menu_share -> {
                LogUtil.d("StorageABC", "share")
                true
            }
            else -> super.onOptionsItemSelected(item)
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

        PhotoListAdapter().let {
            binding.photoFragmentViewPager2.adapter = it
            it.submitList(list)
        }

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