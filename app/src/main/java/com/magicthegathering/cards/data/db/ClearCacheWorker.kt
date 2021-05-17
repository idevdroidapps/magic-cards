package com.magicthegathering.cards.data.db

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.magicthegathering.cards.presentation.fragments.SearchFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ClearCacheWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val db = CardsDatabase.getInstance(applicationContext)
            val cardDao = db.cardsDao
            val cardKeysDao = db.remoteKeysDao

            cardDao.clearAllCards()
            cardKeysDao.clearRemoteKeys()

            val sharedPref = applicationContext.getSharedPreferences(
                applicationContext.packageName + "_preferences",
                Context.MODE_PRIVATE
            )
            with(sharedPref.edit()) {
                putString("last_search_query", "")
                commit()
            }

        } catch (e: Exception) {
            Result.failure()
        }
        Result.success()
    }
}