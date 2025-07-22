package com.mahdi.rostamipour.chatgram

import android.app.Application
import com.mahdi.rostamipour.chatgram.data.repositoryImpl.SocketRepositoryImpl
import com.mahdi.rostamipour.chatgram.data.repositoryImpl.UserRepositoryImpl
import com.mahdi.rostamipour.chatgram.data.service.ApiService
import com.mahdi.rostamipour.chatgram.data.service.MyPreferences
import com.mahdi.rostamipour.chatgram.domain.usecase.SocketUseCase
import com.mahdi.rostamipour.chatgram.domain.usecase.UserUseCase
import com.mahdi.rostamipour.chatgram.presenter.viewModel.SocketViewModel
import com.mahdi.rostamipour.chatgram.presenter.viewModel.UserViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application(){

    override fun onCreate() {
        super.onCreate()

        MyPreferences.init(this)

        val appModule = module{

            single { ApiService() }

            single { UserUseCase(UserRepositoryImpl(get())) }
            single { SocketUseCase(SocketRepositoryImpl(get())) }

            viewModel { UserViewModel(get()) }
            viewModel { SocketViewModel(get()) }

        }

        startKoin {
            androidContext(this@App)
            modules(appModule)
        }

    }
}