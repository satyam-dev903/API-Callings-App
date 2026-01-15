package com.satyam15.apicallings.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satyam15.apicallings.api.ApiResult
import com.satyam15.apicallings.data.User
import com.satyam15.apicallings.repository.UserRepo
import kotlinx.coroutines.launch


class UserVM : ViewModel() {

    private val repo = UserRepo()
    private val PAGE_SIZE=5
    private var currPage=1
    private val _userState = MutableLiveData<ApiResult<List<User>>>()
    val userState: LiveData<ApiResult<List<User>>> = _userState
    private val localUser=mutableListOf<User>()
    private val filteredUser = mutableListOf<User>()
    private val displayList = mutableListOf<User>()


    fun refreshUsers() {
        currPage = 1
        displayList.clear()
        filteredUser.clear()
        fetchUsers()
    }
    fun fetchUsers() {
        viewModelScope.launch {
            _userState.value = ApiResult.Loading
            when (val result = repo.getUsersRepo()) {
                is ApiResult.Success -> {
                    localUser.clear()
                    localUser.addAll(result.data)
                    searchUser("")
                }
                is ApiResult.Error -> {
                    _userState.value = ApiResult.Error(result.msg)
                }
                else -> {}
            }
        }
    }
    fun resetPagination(){
        currPage=1
        displayList.clear()
        loadNextPage()
    }
    fun loadNextPage(){
        if (filteredUser.isEmpty()){
            _userState.value = ApiResult.Success(emptyList())
            return
        }
        val start = (currPage-1)*PAGE_SIZE
        val end=minOf(start+PAGE_SIZE,filteredUser.size)
        if (start >= end) return
        displayList.addAll(filteredUser.subList(start,end))
        _userState.value = ApiResult.Success(displayList.toList())
        currPage++
    }
    fun searchUser(query:String){
        filteredUser.clear()
        if (query.isBlank()) {
            filteredUser.addAll(localUser)
        } else {
            filteredUser.addAll(
            localUser.filter {
                it.name.contains(query, true) ||
                        it.email.contains(query, true)
                }
            )
        }
        resetPagination()
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            _userState.value = ApiResult.Loading
            val newIdx = (localUser.maxOfOrNull { it.id } ?: 0 ) + 1
            val newUser=user.copy(id=newIdx)
            when (val result = repo.addUserRepo(user)) {
                is ApiResult.Success -> {
                    localUser.add(newUser)
                    searchUser("")
                    _userState.value= ApiResult.Success(localUser.toList())
                }
                is ApiResult.Error -> _userState.value = ApiResult.Error(result.msg)
                else -> {}
            }
        }
    }

    fun updateUser(id: Int, user: User) {
        viewModelScope.launch {
            _userState.value = ApiResult.Loading
            when (val result = repo.updateUserRepo(id, user)) {
                is ApiResult.Success -> {
                    val index = localUser.indexOfFirst { it.id == id }
                    if (index != -1) {
                        localUser[index] = user.copy(id = id)
                        searchUser("")
                        _userState.value = ApiResult.Success(localUser.toList())
                    }
                }
                is ApiResult.Error -> _userState.value = ApiResult.Error(result.msg)
                else -> {}
            }
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            _userState.value = ApiResult.Loading
            when (val result = repo.deleteUserRepo(user.id)) {
                is ApiResult.Success -> {
                    localUser.remove(user)
                    searchUser("")
                    _userState.value= ApiResult.Success(localUser.toList())
                }
                is ApiResult.Error -> _userState.value = ApiResult.Error(result.msg)
                else -> {}
            }
        }
    }
}