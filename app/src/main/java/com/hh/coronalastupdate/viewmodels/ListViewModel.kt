package com.hh.coronalastupdate.viewmodels

import android.app.Application
import android.util.Log

import androidx.lifecycle.*
import com.hh.coronalastupdate.db.getDatabase
import com.hh.coronalastupdate.models.Country
import com.hh.coronalastupdate.models.Global
import com.hh.coronalastupdate.repositories.Repository
import kotlinx.coroutines.launch
import java.io.IOException


class ListViewModel(application: Application) : AndroidViewModel(application) {
    enum class ApiStatus { LOADING, ERROR, DONE }

   private val repository = Repository(getDatabase(application))

    val countryList = repository.listData
    val global =repository.globalData



    val _status = MutableLiveData<ApiStatus>()
    val status : LiveData<ApiStatus>
    get() = _status



    val _navigateToSelectedCountry = MutableLiveData<Country>()
    val navigateToSelectedCountry : LiveData<Country>
        get() = _navigateToSelectedCountry

init {
    refreshDataFromRepository()

}


     fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                repository.getData()

                _status.value=ApiStatus.DONE

            } catch (networkError: IOException) {
                Log.e("error" , networkError.message.toString())
                if(countryList.value.isNullOrEmpty())
                    _status.value =ApiStatus.ERROR

            }
        }
    }

    fun displayDetails(country: Country) {
        _navigateToSelectedCountry.value = country
    }


    fun displayDetailsComplete() {
        _navigateToSelectedCountry.value = null
    }

}