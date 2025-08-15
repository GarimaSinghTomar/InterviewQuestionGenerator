package com.example.QuestionGenerator.service;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class AIService {

    @Value("${openrouter.api.key}")
    private String apiKey;

    public String generateQuestions(String role, String experience) throws IOException {
        String prompt = String.format(
            "Generate exactly 10 interview questions and sample answers for a %s with %s experience. " +
            "Output ONLY in the following plain text format, with each question and answer starting on a new line:\n\n" +
            "1. [Type: Technical/HR] Question: <question here>\n" +
            "   Answer: <answer here>\n" +
            "2. [Type: Technical/HR] Question: <question here>\n" +
            "   Answer: <answer here>\n" +
            "... up to 10\n\n" +
            "Do not include extra commentary or headings.",
            role, experience
        );

        OkHttpClient client = new OkHttpClient();

        String json = "{ \"model\": \"openai/gpt-oss-20b:free\", " +
                "\"messages\": [{\"role\": \"user\", \"content\": \"" + prompt.replace("\"", "\\\"") + "\"}] }";

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url("https://openrouter.ai/api/v1/chat/completions")
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }
}



//correct version
/*package com.example.QuestionGenerator.service;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class AIService {

    @Value("${openrouter.api.key}")
    private String apiKey;

    public String generateQuestions(String role, String experience) throws IOException {
        String prompt = String.format(
                "Generate exactly 10 interview questions and answers for a %s with %s experience. " +
                        "Follow this format EXACTLY:\n" +
                        "1. <question>\n" +
                        "Ans. <answer>\n" +
                        "2. <question>\n" +
                        "Ans. <answer>\n" +
                        "... until 10.\n\n" +
                        "Rules:\n" +
                        "- Do NOT add bullet points, extra spaces, or markdown.\n" +
                        "- Each question number must be followed by a space and the question text.\n" +
                        "- 'Ans.' must be on its own new line after the question.\n" +
                        "- No headings, explanations, or extra commentary.",
                role, experience
        );

        OkHttpClient client = new OkHttpClient();

        String json = "{ \"model\": \"openai/gpt-oss-20b:free\", " +
                "\"messages\": [{\"role\": \"user\", \"content\": \"" + prompt.replace("\"", "\\\"") + "\"}] }";

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url("https://openrouter.ai/api/v1")
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }
}
*/


/*
package com.example.QuestionGenerator.service;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.json.JSONObject;
import java.io.IOException;

@Service
public class AIService {

    @Value("${open router.api.key}")
    private String apiKey;

    private static final String AI_URL = "https://openrouter.ai/api/v1/chat/completions";

    public String generateQuestions(String role, String experience) throws IOException {

        String prompt = String.format(
            "Generate exactly 10 interview questions and answers for a %s with %s experience. " +
            "Format exactly as:\n" +
            "1. <question>\n" +
            "Ans. <answer>\n" +
            "...\n" +
            "10. <question>\n" +
            "Ans. <answer>",
            role, experience
        );

        OkHttpClient client = new OkHttpClient();

        String jsonBody = "{ \"model\": \"openai/gpt-oss-20b:free\", " +
                "\"messages\": [{\"role\": \"user\", \"content\": \"" + prompt.replace("\"", "\\\"") + "\"}] }";

        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(AI_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "empty response";
                throw new IOException("AI API call failed: HTTP " + response.code() + ", body: " + errorBody);
            }

            String rawJson = response.body().string();

            // Extract only the AI's text response from the JSON
            JSONObject jsonObj = new JSONObject(rawJson);
            String aiText = jsonObj
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

            return aiText.trim(); // Only return AI text, no HTML
        }
    }
}
*/

/*
package com.example.QuestionGenerator.service;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class AIService {

    @Value("${openrouter.api.key}")
    private String apiKey;

    private static final String AI_URL = "https://openrouter.ai/api/v1";

    public String generateQuestions(String role, String experience) throws IOException {

        String prompt = String.format(
            "Generate exactly 10 interview questions and answers for a %s with %s experience. " +
            "Follow this format EXACTLY:\n" +
            "1. <question>\n" +
            "Ans. <answer>\n" +
            "... until 10.\n" +
            "Rules:\n" +
            "- Do NOT add bullet points, markdown, or extra commentary.\n" +
            "- Each question number must be followed by a space and the question text.\n" +
            "- 'Ans.' must be on its own new line after the question.",
            role, experience
        );

        OkHttpClient client = new OkHttpClient();

        String jsonBody = "{ \"model\": \"openai/gpt-oss-20b:free\", " +
                "\"messages\": [{\"role\": \"user\", \"content\": \"" + prompt.replace("\"", "\\\"") + "\"}] }";

        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(AI_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                // Log response body for debugging
                String respBody = response.body() != null ? response.body().string() : "empty response";
                throw new IOException("AI API call failed: HTTP " + response.code() + ", body: " + respBody);
            }

            // Return the raw JSON string (controller will parse it)
            return response.body().string();
        }
    }
}
*/






/*package com.example.QuestionGenerator.service;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class AIService {

    @Value("${openrouter.api.key}")
    private String apiKey;

    public String generateQuestions(String role, String experience) throws IOException {
        String prompt = String.format(
            "Generate exactly 10 interview questions and sample answers for a %s with %s experience. " +
            "Output ONLY in the following plain text format, with each question and answer starting on a new line:\n\n" +
            "1. [Type: Technical/HR] Question: <question here>\n" +
            "   Answer: <answer here>\n" +
            "2. [Type: Technical/HR] Question: <question here>\n" +
            "   Answer: <answer here>\n" +
            "... up to 10\n\n" +
            "Do not include extra commentary or headings.",
            role, experience
        );

        OkHttpClient client = new OkHttpClient();

        String json = "{ \"model\": \"openai/gpt-oss-20b:free\", " +
                "\"messages\": [{\"role\": \"user\", \"content\": \"" + prompt.replace("\"", "\\\"") + "\"}] }";

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url("https://openrouter.ai/api/v1/chat/completions")
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }
}
*/

/*package com.example.QuestionGenerator.service;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class AIService {

    @Value("${openrouter.api.key}")
    private String apiKey;

    public String generateQuestions(String role, String experience) throws IOException {
        String prompt = String.format(
    "Generate exactly 10 interview questions and sample answers for a %s with %s experience. " +
    "Output ONLY in the following plain text format, with each question on a new line:\n\n" +
    "1. [Type: Technical/HR] Question: <question here>\n" +
    "   Answer: <answer here>\n" +
    "2. [Type: Technical/HR] Question: <question here>\n" +
    "   Answer: <answer here>\n" +
    "... up to 10\n\n" +
    "Do not include extra commentary.",
    role, experience
);


        OkHttpClient client = new OkHttpClient();

        String json = "{ \"model\": \"openai/gpt-oss-20b:free\", " +
                      "\"messages\": [{\"role\": \"user\", \"content\": \"" + prompt.replace("\"", "\\\"") + "\"}] }";

        RequestBody body = RequestBody.create(
            json,
            MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url("https://openrouter.ai/api/v1/chat/completions")
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }
}
*/


/*package com.example.QuestionGenerator.service;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AIService {

    @Value("${openrouter.api.key}")
    private String apiKey;

    public String generateQuestions(String role, String experience) throws IOException {
        String prompt = String.format(
            "Generate 10 technical and HR interview questions with sample answers for a %s with %s experience.",
            role, experience
        );

        OkHttpClient client = new OkHttpClient();

        String json = "{ \"model\": \"openai/gpt-oss-20b:free\", " +
                      "\"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}] }";

        RequestBody body = RequestBody.create(
        json,
        MediaType.parse("application/json")
);

        Request request = new Request.Builder()
                .url("https://openrouter.ai/api/v1/chat/completions")
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
    */

