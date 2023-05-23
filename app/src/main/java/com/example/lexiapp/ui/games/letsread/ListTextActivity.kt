package com.example.lexiapp.ui.games.letsread

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lexiapp.databinding.ActivityListTextBinding
import com.example.lexiapp.ui.adapter.TextAdapter
import com.google.gson.Gson

class ListTextActivity () : AppCompatActivity() {
    private lateinit var binding: ActivityListTextBinding
    //Should be inject
    private lateinit var vM: TextViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListTextBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setVM()
        setListener()
        setRecyclerView()
    }

    private fun setVM(){
        //Should be inject
        val factory = TextViewModel.Factory() // Factory
        vM= ViewModelProvider(this, factory)[TextViewModel::class.java]
    }

    private fun setListener() {
        binding.btnRandomText.setOnClickListener {
            startActivity(Intent(this, LetsReadActivity::class.java))
        }
    }

    private fun setRecyclerView() {
        binding.rvText.layoutManager=LinearLayoutManager(this)
        suscribeToVM()
    }

    private fun suscribeToVM() {
        val gson = Gson()
        vM.listText.observe(this) { list ->
            binding.rvText.adapter = TextAdapter(list){
                val intent = Intent(this, LetsReadActivity::class.java)
                intent.putExtra("TextToRead", gson.toJson(it))
                startActivity(intent)
            }
        }
    }



}