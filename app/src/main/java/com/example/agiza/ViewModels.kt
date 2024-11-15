/*
package com.example.agiza

import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavBackStackEntry
import androidx.savedstate.SavedStateRegistryOwner
import com.example.agiza.components.login.LoginViewModel


@Composable
inline fun <reified VM : ViewModel> viewModel(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    savedStateRegistryOwner: SavedStateRegistryOwner = LocalSavedStateRegistryOwner.current
): VM {
    val activity = LocalContext.current.let {
        var ctx = it
        while (ctx is ContextWrapper) {
            if (ctx is ComponentActivity) {
                return@let ctx
            }
            ctx = ctx.baseContext
        }
        throw IllegalStateException("Expected activity context for creating a ViewModelFactory")
    }


    return androidx.lifecycle.viewmodel.compose.viewModel(
        viewModelStoreOwner = viewModelStoreOwner,
        factory = ViewModelFactory(
            owner = savedStateRegistryOwner,
            defaultArgs = (savedStateRegistryOwner as? NavBackStackEntry)?.arguments,
            dependenciesContainer = (activity.application as JetpackComposeApp).container,
        )
    )
}

class ViewModelFactory(
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle?,
    private val dependenciesContainer: ApplicationContainer,
) : AbstractSavedStateViewModelFactory(
    owner,
    defaultArgs
) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return when (modelClass) {

            LoginViewModel::class.java -> LoginViewModel(dependenciesContainer.repository)

            else -> throw RuntimeException("Unknown viewmodel $modelClass")
        } as T
    }
}
*/
