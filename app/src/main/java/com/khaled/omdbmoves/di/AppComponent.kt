package com.khaled.omdbmoves.di

import com.khaled.omdbmoves.binding.BindingAdapters
import com.khaled.omdbmoves.di.context.OmdbApplication
import com.khaled.omdbmoves.di.lifecycle.ApplicationLifeCycleListenerImpl
import com.khaled.omdbmoves.di.viewModel.ViewModelFactoryProvider
import com.khaled.omdbmoves.di.viewModel.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        ViewModelModule::class,
        ActivitiesBuilderModule::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: OmdbApplication): Builder

        fun build(): AppComponent
    }

    fun inject(omdbApplication: OmdbApplication)

    fun inject(instance: ApplicationLifeCycleListenerImpl)

    fun inject(instance: BindingAdapters)

    fun inject(instance: ViewModelFactoryProvider)

}
