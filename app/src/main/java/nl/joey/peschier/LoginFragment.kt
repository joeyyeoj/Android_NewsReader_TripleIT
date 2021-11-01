package nl.joey.peschier

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {

    private val service = RetrofitClientInstance.retrofitInstance?.create(GetArticleService::class.java)
    private val utils = Utils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AppPreferences.init(view.context)
        var action = arguments?.getInt("ACTION")!!
        super.onViewCreated(view, savedInstanceState)
        val button = view.findViewById<Button>(R.id.loginbutton)
        val usernameInput = view.findViewById<EditText>(R.id.usernameInput)
        val passwordInput = view.findViewById<EditText>(R.id.passwordInput)

        if(action == 1){
            button.setText(getString(R.string.login))
            button.setOnClickListener {
                login(usernameInput.text.toString(), passwordInput.text.toString())
            }
        }
        else if(action == 2){
            button.setText(getString(R.string.register))
            button.setOnClickListener {
                register(usernameInput.text.toString(), passwordInput.text.toString())
            }
        }
    }


    fun login(username: String, password: String){
        var loginCall = service?.login(User(username, password))
        if (loginCall != null) {
            loginCall.enqueue(object: Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if(response.body() != null){
                        AppPreferences.isLogin = true
                        AppPreferences.token = response.body()?.AuthToken.toString()
                        var bundle = Bundle().apply {
                            putInt("FEED_ID", 0)
                        }
                        val algemeenFragment = AlgemeenFragment()
                        algemeenFragment.arguments = bundle
                        parentFragmentManager.beginTransaction().replace(R.id.fragment_container, algemeenFragment).commit()
                        val activity = activity as MainActivity
                        activity.setActionBarTitle(getString(R.string.laatste))
                        utils.hideSoftKeyboard(activity as Activity)
                        utils.updateDrawerLayoutAfterLogin(activity as Activity)
                    }
                    else {
                        Toast.makeText(context, "Verkeerde gebruikersnaam/wachtwoord", Toast.LENGTH_LONG).show()
                        utils.hideSoftKeyboard(activity as Activity)
                    }
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(context, "Er is iets misgegaan met het inloggen :(", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    fun register(username: String, password: String){
        var loginCall = service?.register(User(username, password))
        if (loginCall != null) {
            loginCall.enqueue(object: Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if(response.body() != null){
                        var bundle = Bundle().apply {
                            putInt("ACTION", 1)
                        }
                        val loginFragment = LoginFragment()
                        loginFragment.arguments = bundle
                        parentFragmentManager.beginTransaction().replace(R.id.fragment_container, loginFragment).commit()
                        val activity = activity as MainActivity
                        activity.setActionBarTitle(getString(R.string.login))
                        utils.hideSoftKeyboard(activity as Activity)
                    }
                    else {
                        Toast.makeText(context, "Ongeldige gegevens", Toast.LENGTH_LONG).show()
                        utils.hideSoftKeyboard(activity as Activity)
                    }
                }
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(context, "Er is iets misgegaan met het registeren :(", Toast.LENGTH_LONG).show()
                }
            })
        }
    }


}