package com.example.lexiapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.domain.useCases.CategoriesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoriesUseCases: CategoriesUseCases
): ViewModel() {
    private var _categoriesSavedSuccessful = MutableLiveData(false)
    val categoriesSavedSuccessful: LiveData<Boolean>
    get() = _categoriesSavedSuccessful


    fun saveCategoriesFromPatient(categories: List<String>){
        viewModelScope.launch(Dispatchers.IO){
            val currentMail = categoriesUseCases.getEmailCurrentUser()

            currentMail?.let { email ->
                categoriesUseCases.saveCategories(email, categories)

                if(categoriesUseCases.getCategoriesFromPatient(email).isNotEmpty()){
                    withContext(Dispatchers.Main){
                        _categoriesSavedSuccessful.value = true
                    }
                }
            }
        }
    }

}