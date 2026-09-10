package com.example.news.data.background

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.news.domain.usecase.UpdateSubscribedArticlesUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class RefreshDataWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val updateSubscribedArticlesUseCase: UpdateSubscribedArticlesUseCase
): CoroutineWorker(
    context,
    workerParameters
){
    override suspend fun doWork(): Result {
        Log.d(".data.background.RefreshDataWorker", "Start")
        updateSubscribedArticlesUseCase()
        Log.d(".data.background.RefreshDataWorker", "Finish")
        return Result.success()
    }

}