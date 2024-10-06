package com.nitesh.app.sdgsclassroom.aichatbot

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.nitesh.app.sdgsclassroom.R
import com.nitesh.app.sdgsclassroom.databinding.ActivityAiChatBinding
import okhttp3.Callback
import okhttp3.RequestBody
import okhttp3.ResponseBody

class AiChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAiChatBinding
    private lateinit var chatAdapter: AiChatAdapter
    private val messages = mutableListOf<Pair<String, String>>()
    private val apiKey = "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"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAiChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        chatAdapter = AiChatAdapter(messages)
        binding.chatRecyclerView.adapter = chatAdapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true // Start from the bottom
        }

        binding.sendButton.setOnClickListener {
            val question = binding.questionEditText.text.toString()
            if (question.isNotEmpty()) {
                addMessageToChat("User", question)
                binding.questionEditText.text.clear()

                askAiQuestion(question) { response ->
                    runOnUiThread {
                        addMessageToChat("AI", response)
                    }
                }
            }
        }
    }

    private fun addMessageToChat(sender: String, message: String) {
        messages.add(Pair(sender, message))
        chatAdapter.notifyItemInserted(messages.size - 1)
        binding.chatRecyclerView.scrollToPosition(messages.size - 1)
    }

    private fun askAiQuestion(question: String, onResponse: (String) -> Unit) {
        val retrofit = ApiInstance.getRetrofitInstance()
        val apiService = retrofit.create(OpenAiApiService::class.java)

        val jsonObject = JsonObject().apply {
            addProperty("model", "text-davinci-003") // Use "gpt-4" for GPT-4
            addProperty("prompt", question)
            addProperty("max_tokens", 150)
            addProperty("temperature", 0.7)
        }


        val body = RequestBody.create("application/json".toMediaTypeOrNull(), jsonObject.toString())
        val call = apiService.askQuestion(body, "Bearer $apiKey")

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.string()?.let { result ->
                        val jsonResponse = JsonParser.parseString(result).asJsonObject
                        val textResponse = jsonResponse.getAsJsonArray("choices")[0]
                            .asJsonObject.get("text").asString
                        onResponse(textResponse.trim())
                    }
                } else {
                    onResponse("Failed to get response")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onResponse("Error: ${t.message}")
            }
        })
    }
}


    }
}