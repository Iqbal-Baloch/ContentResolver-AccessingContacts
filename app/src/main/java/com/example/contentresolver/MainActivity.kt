package com.example.contentresolver

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import com.example.contentresolver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var list: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) requestPermission() ;
        else getContacts() ;
    }
    private fun getContacts(){
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME),
            null,
            null,
            null
        )
        list = ArrayList<String>();
        cursor!!.moveToFirst()
        do {
            list.add(cursor.getString(0))
        }while (cursor.moveToNext())
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        binding.lst.adapter = adapter
    }
    private fun requestPermission(){
        if(hasContactsReadPermission()) getContacts()
        else ActivityCompat.requestPermissions(this, arrayOf( Manifest.permission.READ_CONTACTS),1234 )
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1234 && grantResults.isNotEmpty()){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) getContacts()
        }
    }
    private fun hasContactsReadPermission() =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED

}