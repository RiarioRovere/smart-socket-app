package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.databinding.FragmentFirstBinding
import org.json.JSONObject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

private var _binding: FragmentFirstBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val state = State(false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

      _binding = FragmentFirstBinding.inflate(inflater, container, false)
      return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadState()

        binding.buttonFirst.setOnClickListener {
            state.enabled = !state.enabled

            val volleyQueue = Volley.newRequestQueue(context)
            val url = "${HOST}/state/set"
            val request = JsonObjectRequest(
                Request.Method.POST,
                url,
                JSONObject().put("enabled", state.enabled),
                { response ->
                    state.enabled = response.getBoolean("enabled")
                    binding.buttonFirst.text = if (state.enabled) "Выключить" else "Включить"
                },
                { error -> System.err.println(error)
                }

            )
            volleyQueue.add(request)
            System.err.println("I was clicked")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadState() {
        val volleyQueue = Volley.newRequestQueue(context)
        val url = "${HOST}/state/get"
        val request = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                state.enabled = response.getBoolean("enabled")
                binding.buttonFirst.text = if (state.enabled) "Выключить" else "Включить"
            },
            { error -> System.err.println(error)
            }

        )
        volleyQueue.add(request)
    }

    companion object {
        private val HOST = "http://51.250.104.235:80"
    }
}

data class State(
    var enabled: Boolean
)
