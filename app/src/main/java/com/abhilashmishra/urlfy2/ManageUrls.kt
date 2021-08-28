package com.abhilashmishra.urlfy2

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import java.io.*
import java.util.*

class ManageUrls : AppCompatActivity() {

    private var urlAdapter: UrlListAdapter? = null
    private var linkList: MutableList<LinkData>? = null
    private var sharedPreferenceUtility: SharedPreferenceUtility? = null

    lateinit var toolbar: Toolbar
    lateinit var urlRecyclerView: RecyclerView

    companion object {
        const val DIALOG_MODE_EDIT = 100001
        const val DIALOG_MODE_ADD = 100002
        const val WRITE_REQUEST_CODE = 43
        const val READ_REQUEST_CODE = 42
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        urlRecyclerView = findViewById(R.id.urlRecyclerView)
        initToolbar()
        initUi()
    }

    private fun initToolbar() {
        toolbar.setTitle(R.string.app_name)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add_link_menu -> {
                    showUrlAddEditDialog(mode = DIALOG_MODE_ADD)
                    true
                }
                R.id.export_file_menu -> {
                    exportFile()
                    true
                }
                R.id.import_file_menu -> {
                    importFile()
                    true
                }
                else -> {
                    false
                }
            }

        }
        toolbar.inflateMenu(R.menu.normal_menu)
    }

    private fun initUi() {
        sharedPreferenceUtility = SharedPreferenceUtility.getSharedPreferenceUtility(this)
        linkList = sharedPreferenceUtility?.savedLinks
        urlAdapter = UrlListAdapter(
            linkList, this,
            delete = { index ->
                val deleteConfirmation: AlertDialog = AlertDialog.Builder(this)
                    .setMessage(resources.getString(R.string.delete_confirm, linkList?.get(index)?.linkTitle))
                    .setPositiveButton(R.string.yes) { _: DialogInterface, _: Int ->
                        linkList?.removeAt(index)
                        sharedPreferenceUtility?.updateUrlList(linkList)
                        urlAdapter?.notifyItemRemoved(index)
                    }
                    .setNegativeButton(R.string.no) { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                    }
                    .create()
                deleteConfirmation.show()
            },
            info = { index ->
                showUrlAddEditDialog(index, DIALOG_MODE_EDIT)
            })
        val linearLayoutManager = LinearLayoutManager(this)

        urlRecyclerView.adapter = urlAdapter
        urlRecyclerView.layoutManager = linearLayoutManager
    }

    private fun showUrlAddEditDialog(index: Int = -1, mode: Int = DIALOG_MODE_ADD) {
        val view: View = LayoutInflater.from(this).inflate(R.layout.dialog_edit_url, null, false)
        val editTextUrl: EditText = view.findViewById(R.id.edit_url_dialog) as EditText
        val editTextTitle: EditText = view.findViewById(R.id.edit_title_dialog) as EditText
        val alertDialog: AlertDialog = AlertDialog.Builder(this)
            .setView(view)
            .setPositiveButton(R.string.save
            ) { dialogInterface: DialogInterface, _: Int ->
                if (!editTextTitle.text.toString().isEmpty() && !editTextTitle.text.toString().isEmpty()) {
                    when {
                        mode == DIALOG_MODE_EDIT -> {
                            linkList?.get(index)?.linkUrl = editTextUrl.text.toString()
                            linkList?.get(index)?.linkTitle = editTextTitle.text.toString()
                            urlAdapter?.notifyItemChanged(index)
                        }
                        mode == DIALOG_MODE_ADD -> {
                            val newLink = LinkData(editTextTitle.text.toString(), editTextUrl.text.toString())
                            linkList?.add(newLink)
                            urlAdapter?.notifyItemInserted(linkList?.size?.let { it - 1 } ?: 0)
                            urlRecyclerView.smoothScrollToPosition(linkList?.size?.let { it - 1 } ?: 0)
                        }
                    }

                    sharedPreferenceUtility?.updateUrlList(linkList)
                    dialogInterface.dismiss()
                }
            }
            .setNegativeButton(R.string.cancel
            ) { dialogInterface: DialogInterface, _: Int -> dialogInterface.dismiss() }
            .create()



        when (mode) {
            DIALOG_MODE_EDIT -> {
                editTextUrl.setText(linkList?.get(index)?.linkUrl)
                editTextTitle.setText(linkList?.get(index)?.linkTitle)
            }
            DIALOG_MODE_ADD -> {
            }
        }

        alertDialog.show()
    }

    private fun importFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        startActivityForResult(intent, READ_REQUEST_CODE)
    }


    private fun exportFile() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        val fileName: String = "UrlFy" + Date().time + ".dlk"
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_TITLE, fileName)
        startActivityForResult(intent, WRITE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            (requestCode == WRITE_REQUEST_CODE && resultCode == Activity.RESULT_OK) -> {
                data?.data?.let { writeFile(it) }
            }

            (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) -> {
                data?.data?.let { writeFile(it) }
            }
        }
    }

    private fun readFile(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()

        do {
            val line = reader.readLine()
            if (line != null) {
                stringBuilder.append(line)
            }
        } while (line != null)

        inputStream?.close()
        val listType = object : TypeToken<ArrayList<LinkData>>() {}.type

        try {
            val list: List<LinkData> = Gson().fromJson<ArrayList<LinkData>>(stringBuilder.toString(), listType)
            for (linkItem: LinkData in list) {
                if (linkList?.contains(linkItem) == false) {
                    linkList?.add(linkItem)
                    urlAdapter?.notifyItemInserted(linkList?.size?.let { it - 1 } ?: 0)
                    sharedPreferenceUtility?.updateUrlList(linkList)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, R.string.invalid_file_msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun writeFile(uri: Uri) {
        val gson = Gson()
        val jsonArr = JSONArray()

        linkList?.forEach {
            jsonArr.put(gson.toJson(it))
        }
        val data: String = jsonArr.toString(4)
        try {
            val pfd = contentResolver.openFileDescriptor(uri, "w")
            val fileOutputStream = FileOutputStream(pfd?.fileDescriptor)
            fileOutputStream.write(data.toByteArray())
            fileOutputStream.close()
            pfd?.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
