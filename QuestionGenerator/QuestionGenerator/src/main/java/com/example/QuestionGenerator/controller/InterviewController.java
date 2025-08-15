package com.example.QuestionGenerator.controller;

import com.example.QuestionGenerator.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class InterviewController {

    @Autowired
    private AIService aiService;
   

@PostMapping("/generate")
public ResponseEntity<String> generate(@RequestParam String role, @RequestParam String experience) {
    try {
        String apiResponse = aiService.generateQuestions(role, experience);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(apiResponse);
        String content = root.path("choices").get(0).path("message").path("content").asText();

        return ResponseEntity.ok(content.trim());
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error generating questions: " + e.getMessage());
    }
}

   /*  @PostMapping("/generate")
    public ResponseEntity<String> generate(@RequestParam String role, @RequestParam String experience) {
        try {
            String result = aiService.generateQuestions(role, experience);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error generating questions: " + e.getMessage());
        }
    } */
}


/*package com.example.QuestionGenerator.controller;

import com.example.QuestionGenerator.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class InterviewController {

    @Autowired
    private AIService aiService;

    @PostMapping("/generate")
    public ResponseEntity<String> generate(@RequestParam String role, @RequestParam String experience) {
        try {
            String result = aiService.generateQuestions(role, experience);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error generating questions: " + e.getMessage());
        }
    }
}
*/

/*package com.example.QuestionGenerator.controller;

import com.example.QuestionGenerator.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class InterviewController {

    @Autowired
    private AIService aiService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateQuestions(
            @RequestParam String role,
            @RequestParam String experience) {

        try {
            // Get the plain Q&A text from AI
            String aiResult = aiService.generateQuestions(role, experience);

            // Convert plain text to HTML for display
            String html = convertToHtml(aiResult);

            return ResponseEntity.ok()
                    .header("Content-Type", "text/html")
                    .body(html);

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .header("Content-Type", "text/html")
                    .body("<h3>Error generating questions: " + e.getMessage() + "</h3>");
        }
    }

    // Converts the Q&A text into styled HTML
    private String convertToHtml(String text) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><title>Interview Questions</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; }");
        html.append("h2 { color: #2c3e50; }");
        html.append("li { margin-bottom: 15px; }");
        html.append(".answer { margin-top: 5px; color: #34495e; }");
        html.append("</style></head><body>");
        html.append("<h2>Generated Interview Questions</h2>");
        html.append("<ol>");

        // Split into Q&A pairs based on pattern "n. Question"
        String[] lines = text.split("\n");
        String currentQuestion = null;
        StringBuilder currentAnswer = new StringBuilder();

        for (String line : lines) {
            line = line.trim();
            if (line.matches("^\\d+\\.\\s+.*")) { // New question
                if (currentQuestion != null) {
                    html.append("<li><strong>").append(currentQuestion).append("</strong>");
                    html.append("<div class='answer'>").append(currentAnswer.toString().trim()).append("</div></li>");
                }
                currentQuestion = line.substring(line.indexOf('.') + 1).trim();
                currentAnswer.setLength(0);
            } else if (line.startsWith("Ans.")) {
                currentAnswer.append(line.replace("Ans.", "").trim()).append(" ");
            } else {
                currentAnswer.append(line).append(" ");
            }
        }

        // Add the last Q&A
        if (currentQuestion != null) {
            html.append("<li><strong>").append(currentQuestion).append("</strong>");
            html.append("<div class='answer'>").append(currentAnswer.toString().trim()).append("</div></li>");
        }

        html.append("</ol>");
        html.append("</body></html>");
        return html.toString();
    }
}
*/

/*package com.example.QuestionGenerator.controller;

import com.example.QuestionGenerator.service.AIService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class InterviewController {

    @Autowired
    private AIService aiService;

    @PostMapping("/generate")
    public ResponseEntity<String> generate(@RequestParam String role, @RequestParam String experience) {
        try {
            String rawResult = aiService.generateQuestions(role, experience);

            // Parse JSON to get AI's message content
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(rawResult);
            String aiText = root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            // Format the extracted text
            String cleanedResult = formatQuestions(aiText);
            return ResponseEntity.ok(cleanedResult);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error generating questions: " + e.getMessage());
        }
    }

    private String formatQuestions(String raw) {
        String[] lines = raw.split("\\r?\\n");
        StringBuilder formatted = new StringBuilder();
        int count = 1;
        String currentQ = "";
        String currentA = "";

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (!line.startsWith("Ans.") && !line.isEmpty()) {
                if (!currentQ.isEmpty() && !currentA.isEmpty()) {
                    formatted.append(count).append(". ").append(currentQ).append("\n");
                    formatted.append("Ans. ").append(currentA).append("\n\n");
                    count++;
                    currentQ = "";
                    currentA = "";
                }
                currentQ = line.replaceAll("^\\d+\\.\\s*", "");
            }
            else if (line.startsWith("Ans.")) {
                currentA = line.replaceFirst("Ans\\.\\s*", "");
            }
            else if (!currentA.isEmpty()) {
                currentA += " " + line;
            }
        }

        if (!currentQ.isEmpty() && !currentA.isEmpty()) {
            formatted.append(count).append(". ").append(currentQ).append("\n");
            formatted.append("Ans. ").append(currentA).append("\n\n");
        }

        return formatted.toString().trim();
    }
}
*/


/*package com.example.QuestionGenerator.controller;

import com.example.QuestionGenerator.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class InterviewController {

    @Autowired
    private AIService aiService;

    @PostMapping("/generate")
    public ResponseEntity<String> generate(@RequestParam String role, @RequestParam String experience) {
        try {
            String rawResult = aiService.generateQuestions(role, experience);
            String cleanedResult = formatQuestions(rawResult);
            return ResponseEntity.ok(cleanedResult);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error generating questions: " + e.getMessage());
        }
    }

    
    private String formatQuestions(String raw) {
        String[] lines = raw.split("\\r?\\n");
        StringBuilder formatted = new StringBuilder();
        int count = 1;
        String currentQ = "";
        String currentA = "";

        for (String line : lines) {
            line = line.trim();

            // Skip empty lines
            if (line.isEmpty()) continue;

            // Detect a question line (starts with number. or doesn't start with Ans.)
            if (!line.startsWith("Ans.") && !line.isEmpty()) {
                // If there's an unfinished Q/A, save it
                if (!currentQ.isEmpty() && !currentA.isEmpty()) {
                    formatted.append(count).append(". ").append(currentQ).append("\n");
                    formatted.append("Ans. ").append(currentA).append("\n\n");
                    count++;
                    currentQ = "";
                    currentA = "";
                }
                currentQ = line.replaceAll("^\\d+\\.\\s*", "");
            }
            // Detect answer line
            else if (line.startsWith("Ans.")) {
                currentA = line.replaceFirst("Ans\\.\\s*", "");
            }
            // Handle answer text that wraps onto multiple lines
            else if (!currentA.isEmpty()) {
                currentA += " " + line;
            }
        }

        // Append the last Q/A
        if (!currentQ.isEmpty() && !currentA.isEmpty()) {
            formatted.append(count).append(". ").append(currentQ).append("\n");
            formatted.append("Ans. ").append(currentA).append("\n\n");
        }

        return formatted.toString().trim();
    }
}
*/



/*package com.example.QuestionGenerator.controller;


import com.example.QuestionGenerator.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class InterviewController {

    @Autowired
    private AIService aiService;

    @PostMapping("/generate")
    public ResponseEntity<String> generate(@RequestParam String role, @RequestParam String experience) {
        try {
            String result = aiService.generateQuestions(role, experience);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error generating questions: " + e.getMessage());
        }
    }
} */

