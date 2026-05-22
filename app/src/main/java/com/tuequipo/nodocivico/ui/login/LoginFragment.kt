package com.tuequipo.nodocivico.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tuequipo.nodocivico.R

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etName = view.findViewById<EditText>(R.id.etName)
        val etNeighborhood = view.findViewById<EditText>(R.id.etNeighborhood)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val name = etName.text.toString().trim()
            val neighborhood = etNeighborhood.text.toString().trim()

            if (name.isEmpty()) { etName.error = "Ingresa tu nombre"; return@setOnClickListener }
            if (neighborhood.isEmpty()) { etNeighborhood.error = "Ingresa tu barrio"; return@setOnClickListener }

            // GUARDAR EN SHAREDPREFS (Lógica offline de sesión)
            val sharedPref = requireActivity().getSharedPreferences("NodoCivicoPrefs", android.content.Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("USER_NAME", name)
                putString("USER_NEIGHBORHOOD", neighborhood)
                apply()
            }

            findNavController().navigate(R.id.action_login_to_home)
        }
    }
}