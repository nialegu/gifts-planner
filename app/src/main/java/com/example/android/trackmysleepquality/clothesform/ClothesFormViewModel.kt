package com.example.android.trackmysleepquality.clothesform

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.android.trackmysleepquality.database.AppDatabaseDao
import com.example.android.trackmysleepquality.database.Gift
import com.example.android.trackmysleepquality.database.Plan
import com.example.android.trackmysleepquality.database.PlanGifts
import com.example.android.trackmysleepquality.database.PlanReceiver
import com.example.android.trackmysleepquality.database.PlanReceiverGifts
import com.example.android.trackmysleepquality.database.Receiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClothesFormViewModel(
    private val dao: AppDatabaseDao,
    application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    var currentPlan = MutableLiveData<PlanReceiverGifts?>()
    fun getPlanById(id: Long){
        uiScope.launch {
            currentPlan.value = getById(id)
        }
    }
    private suspend fun getById(id: Long) : PlanReceiverGifts? {
        return withContext(Dispatchers.IO) {
            dao.getPlanById(id)
        }
    }

    var receivers = dao.getAllReceivers()

    /*var currentClothesItem = MutableLiveData<Clothes?>()
    fun getClothesById(id: Long){
        uiScope.launch {
            currentClothesItem.value = getById(id)
        }
    }
    private suspend fun getById(id: Long) : Clothes? {
        return withContext(Dispatchers.IO) {
            dao.getSingleClothes(id)
        }
    }*/

    fun updateCurrentPlan(plan: PlanReceiverGifts) {
        uiScope.launch {
            update(plan)
        }
    }
    private suspend fun update(plan: PlanReceiverGifts) {
        withContext(Dispatchers.IO) {
            dao.updatePlan(plan.plan)

            plan.gifts.map {
                dao.updateGift(it)
                val pg = PlanGifts(
                    plan.plan.pId,
                    it.gId
                )
                dao.updatePlanGifts(pg)
            }

            val pr = PlanReceiver(
                plan.plan.pId,
                plan.receiver.rId
            )
            dao.updatePlanReceiver(pr)
        }
    }

    /*fun updateCurrentClothes(clothes: Clothes){
        uiScope.launch {
            update(clothes)
        }
    }*/
    /*private suspend fun update(clothes: Clothes) {
        withContext(Dispatchers.IO) {
            dao.updateClothes(clothes)
            Log.i("sadsadsad", clothes.toString())
        }
    }*/

    fun insertNewPlan(plan: Plan, receiver: Receiver, gifts: List<Gift>){
        uiScope.launch {
            insertPlanWithGift(insertPlan(plan, receiver), gifts)
        }
    }
    private suspend fun insertPlan(plan: Plan, receiver: Receiver): Long {
        return withContext(Dispatchers.IO) {
            val newPlanId = dao.insertPlan(plan)
            val newReceiverId = dao.insertReceiver(receiver)

            val pr = PlanReceiver(
                newPlanId,
                newReceiverId,
            )
            dao.insertPlanReceiver(pr)
            newPlanId
        }
    }

    private suspend fun insertPlanWithGift(planId: Long, gifts: List<Gift>){
        withContext(Dispatchers.IO) {
            gifts.map {
                val newGiftId = dao.insertGift(it)
                val pg = PlanGifts(
                    planId,
                    newGiftId,
                )
                dao.insertPlanGift(pg)
            }
        }
    }

    /*fun insertNewClothes(clothes: Clothes){
        uiScope.launch {
            insert(clothes)
        }
    }
    private suspend fun insert(clothes: Clothes) {
        withContext(Dispatchers.IO) {
            dao.insertClothes(clothes)
        }
    }*/
}