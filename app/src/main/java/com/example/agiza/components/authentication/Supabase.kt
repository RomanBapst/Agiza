import android.content.Context
import com.auth0.android.jwt.Claim
import com.auth0.android.jwt.JWT
import com.example.agiza.components.authentication.AuthenticationData
import com.example.agiza.components.authentication.AuthenticationDataImpl
import com.example.agiza.components.authentication.AuthenticationService
import com.example.agiza.components.authentication.AuthenticationState
import com.example.agiza.components.authentication.Role
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.Postgrest

class SupabaseAuthentication(val supabase : SupabaseClient) : AuthenticationService {


    override suspend fun authenticate(context: Context) {
    }

    override suspend fun initCredentials() {
        try {
            supabase.auth.sessionStatus.collect {
                when (it) {
                    is SessionStatus.Authenticated -> {
                        println("dbg: authenticated")
                        val token = JWT(it.session.accessToken)

                        val roleClaim = token.getClaim("user_role")

                        when(roleClaim.asString()) {
                           "admin" -> {
                                  authenticationData.roleWriter.value = Role.ADMIN
                           }
                            "user" -> {
                                authenticationData.roleWriter.value = Role.USER
                            }
                            "agent" -> {
                                authenticationData.roleWriter.value = Role.AGENT
                            }
                        }


                        authenticationData.authenticationStateWriter.value =
                            AuthenticationState.Authenticated
                    }

                    is SessionStatus.Initializing -> {
                        authenticationData.authenticationStateWriter.value =
                            AuthenticationState.Authenticating
                    }

                    is SessionStatus.NotAuthenticated -> {
                        authenticationData.authenticationStateWriter.value =
                            AuthenticationState.Unauthenticated
                    }

                    is SessionStatus.RefreshFailure -> {
                        authenticationData.authenticationStateWriter.value =
                            AuthenticationState.Unauthenticated
                    }
                }
            }
        } catch (exception : Exception) {
            println(exception.message)
        }
    }

    override suspend fun logout(context: Context) {
        try {
            supabase.auth.signOut()
            authenticationData.authenticationStateWriter.value = AuthenticationState.Unauthenticated
        } catch (exception : Exception) {
            println(exception.message)
        }
    }

    override suspend fun loginWithEmailAndPassword(email: String, password: String) {
        try {
            authenticationData.authenticationStateWriter.value = AuthenticationState.Authenticating
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
        } catch (exception : Exception) {
            println(exception.message)
            authenticationData.authenticationStateWriter.value = AuthenticationState.Unauthenticated
        }
    }

    override val authenticationData = AuthenticationDataImpl()

}