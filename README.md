
#AI-Powered Interview Question Generator

An AI-driven web application that generates role-specific interview questions and answers based on job role and experience level.
Built using Java Spring Boot, HTML/CSS/JavaScript, and OpenRouter API.

🚀 Features

Role-Specific Q&A – Generates tailored interview questions based on job role and experience.

AI-Powered – Integrates with OpenRouter API for intelligent question generation.

Responsive Frontend – Built with HTML, CSS, and JavaScript for a seamless experience.

JSON Parsing – Uses Jackson to structure AI-generated output in a clear, readable format.

Prompt Engineering – Improves accuracy, contextual relevance, and variety of questions.

🛠 Tech Stack

Backend: Java, Spring Boot

Frontend: HTML, CSS, JavaScript

AI Integration: OpenRouter API

Data Processing: Jackson (JSON Parsing)

📂 Project Structure

/src

   /main
   
      /java
      
         /controller      # API endpoints
         
         /service         # AI integration logic
         
      /resources
      
         application.properties  # API keys & config
         
/public

   index.html             # Frontend UI
   
   script.js              # Frontend logic
   
   style.css              # Styling

⚙️ Setup & Installation
1️⃣ Clone the Repository
git clone https://github.com/your-username/QuestionGenerator.git

cd QuestionGenerator

2️⃣ Backend Setup (Spring Boot)

Open the project in IntelliJ IDEA or VS Code.

Add your OpenRouter API Key to application.properties:

openrouter.api.key=YOUR_API_KEY


Run the Spring Boot application:

mvn spring-boot:run

3️⃣ Frontend Setup

Open index.html in your browser, or serve via Live Server extension.
