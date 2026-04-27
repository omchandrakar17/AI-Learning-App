# AI Powered Personalized Learning App

A smart desktop application built using JavaFX that helps users learn any topic using AI Chat, Images, and Videos. The app also stores user history and saved content using SQLite database.

--------------------------------------------------

Features:

- User Login & Signup
- AI Chat System
- Image Learning Section
- Video Learning Section
- Learning History Tracking
- Save Important Chats
- SQLite Database Integration
- Modern UI using JavaFX

--------------------------------------------------

Technologies Used:

- Java
- JavaFX
- SQLite
- HTTP API
- CSS

--------------------------------------------------

Project Structure:

src/
  database
  session
  ui
  services
  resources

--------------------------------------------------

Requirements:

- Java JDK 17+
- IntelliJ IDEA
- JavaFX SDK
- SQLite JDBC Driver

--------------------------------------------------

How to Run:

1. Download or Clone Project
   git clone https://github.com/your-username/your-repo-name.git

2. Open in IntelliJ IDEA

3. Setup JavaFX:
   - Download from https://openjfx.io
   - Add VM Options:
     --module-path "PATH_TO_FX/lib" --add-modules javafx.controls,javafx.fxml

4. Add SQLite JDBC:
   - Download jar file
   - Add to project libraries

5. Run:
   - Open Mainapp.java
   - Click Run

--------------------------------------------------

Database Info:

Database file:
ai_learning.db

Tables:
- users
- history
- saved

--------------------------------------------------

Important Notes:

- If login/signup not working:
  Delete ai_learning.db and run again

- Do not share API keys

--------------------------------------------------

Future Improvements:

- MongoDB Integration
- Cloud Sync
- Mobile App (APK)
- Better AI Personalization

--------------------------------------------------

Support:

If you like this project, give it a star on GitHub.

--------------------------------------------------

Made By:
Om Chandrakar
