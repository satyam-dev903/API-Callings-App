package com.satyam15.apicallings.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.satyam15.apicallings.R
import com.satyam15.apicallings.api.ApiResult
import com.satyam15.apicallings.data.User
import com.satyam15.apicallings.databinding.ActivityMainBinding
import com.satyam15.apicallings.viewmodel.UserVM
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.satyam15.apicallings.utils.SearchDebounce

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: UserVM by viewModels()
    private lateinit var adapter: UserAdapter

    private lateinit var search: SearchDebounce


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        viewModel.fetchUsers()
        setupRCV()
        observeUsers()

        binding.swipeRefresh.setOnRefreshListener {
            binding.etSearch.setText("")      // clear search
            viewModel.refreshUsers()
        }



        binding.addUserBtn.setOnClickListener {
            showAddUserDialog()
        }


        search= SearchDebounce{
            query ->
            viewModel.searchUser(query)
        }
        binding.etSearch.addTextChangedListener{
            search.submitQuery(it.toString())
        }


//        binding.rcv.setOnTouchListener{ _, _->
//            if (binding.etSearch.hasFocus()){
//                binding.etSearch.clearFocus()
//            }
//            false
//        }

    }
    override fun onBackPressed() {
        if (binding.etSearch.hasFocus() ){
            binding.etSearch.setText("")
            // remove cursor from search bar
            binding.etSearch.clearFocus()
        }else{super.onBackPressed()}
    }


    fun setupRCV(){
        adapter= UserAdapter(
        onDeleteClick = {user->
            viewModel.deleteUser(user)
        },
        onEditClick={user ->
            showEditDialog(user)
        }
    )
        val lm= LinearLayoutManager(this)
        binding.rcv.layoutManager= lm
        binding.rcv.adapter=adapter
        binding.rcv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)

                val lastVisible = lm.findLastVisibleItemPosition()

                val totalItemCount = lm.itemCount

                if (lastVisible == totalItemCount - 1) {
                    viewModel.loadNextPage()
                }
            }
        })
    }
    private fun observeUsers() {
        viewModel.userState.observe(this) { state ->
            when (state) {

                is ApiResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ApiResult.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefresh.isRefreshing = false
                    adapter.submitList(state.data)

                }

                is ApiResult.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefresh.isRefreshing = false
                    Toast.makeText(this, state.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun showEditDialog(user:User){
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_user, null)
        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etEmail = dialogView.findViewById<EditText>(R.id.etEmail)

        etName.setText(user.name)
        etEmail.setText(user.email)

        AlertDialog.Builder(this)
            .setTitle("Edit User")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val name = etName.text.toString()
                val email = etEmail.text.toString()

                if (name.isNotEmpty() && email.isNotEmpty()) {
                    val updatedUser = User(user.id, name, email)
                    viewModel.updateUser(user.id, updatedUser)

                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    private fun showAddUserDialog(){
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_user, null)
        val etName: EditText = dialogView.findViewById(R.id.etName)
        val etEmail: EditText = dialogView.findViewById(R.id.etEmail)
        AlertDialog.Builder(this)
            .setTitle("ADD USER")
            .setView(dialogView)
            .setPositiveButton("Add"){_,_->
                val name = etName.text.toString()
                val email = etEmail.text.toString()

                if (name.isNotEmpty() && email.isNotEmpty()) {
                    val user = User(
                        id = 0,
                        name = name,
                        email = email
                    )
                    viewModel.addUser(user)
                }

            }
            .setNegativeButton("Cancel",null)
            .show()
    }
}