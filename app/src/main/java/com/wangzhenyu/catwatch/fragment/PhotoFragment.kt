package com.wangzhenyu.catwatch.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class PhotoFragment : Fragment() {

    companion object {
        const val internalStorageFileName: String = "internal.txt"
        const val internalStorageFileContent: String =
            "刘增艳，1996年8月31日出生于四川省 [1]  ，中国内地流行乐女歌手，SNH48 Team SII成员。\n" +
                    "2015年7月25日，成为SNH48五期生 [2]  ；\n" +
                    "12月4日，加入SNH48 Team XII，并在《剧场女神》公演中正式出道 [3-4]  。\n" +
                    "2016年3月25日，随SNH48推出首张原创EP专辑《源动力》 [5]  ；\n" +
                    "7月30日，随组合举办“比翼齐飞”SNH48第三届偶像年度人气总选举演唱会 [6]  ；\n" +
                    "9月15日，担任SNH48 Team XII副队长 [7]  。\n" +
                    "2017年7月29日，获得“我心翱翔”SNH48 GROUP第四届偶像年度人气总决选第63名。\n" +
                    "2018年2月3日，SNH48首次重组后，她被重新分队至SNH48 Team SII；\n" +
                    "7月28日，获得“砥砺前行”SNH48 GROUP第五届偶像年度人气总决选第30名 [8]  。\n" +
                    "2019年7月27日，获得“新的旅程”SNH48 GROUP第六届偶像年度人气总决选第30名 [9]  。\n" +
                    "2020年8月15日，获得“创造炙热的青春”SNH48 GROUP第七届偶像年度人气总决选第21名 [10]  ；\n" +
                    "9月21日，加入丝芭传媒旗下电商女团浪彩少女AW9 [11]  。"
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
            R.id.photo_menu_external -> {
                LogUtil.d("StorageABC", "external")
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