# Java Weather Dashboard 🌦️ (Architectural Refactor)
**Status: 🚧 Work in Progress (Active Refactoring)**

📌 **Project Overview**
This repository documents the transition of a Java Desktop application from an IDE-dependent academic project to a professional, standalone software product. The application interfaces with the wttr.in REST API to provide detailed, multi-day weather forecasts.

🎓 **Origin & Context**
The project was initially developed as a graded assignment for the PLH24 module at the Hellenic Open University (HOU). The original version relied heavily on NetBeans auto-generated GUI code and manual dependency management.

🛠️ **Refactoring Objectives (In-Progress)**
The current development phase focuses on deep architectural improvements and best practices:

  **▸ Standardized Build System:** Migrated the entire project to Maven to automate dependency management (OkHttp, Gson, iText, Derby) and ensure IDE independence.

  **▸ GUI Decoupling:** Removing NetBeans Matisse "Guarded Blocks" and replacing auto-generated GroupLayout with manual GridBagLayout and BorderLayout for absolute UI control.

  **▸ Advanced Concurrency:** Implementing SwingWorker to decouple long-running API calls and database operations from the Event Dispatch Thread (EDT), ensuring a non-blocking, responsive user experience.

  **▸ Data Structure Optimization:** Refactoring complex UI views (e.g., 68+ text fields) by implementing multi-dimensional array data binding to reduce boilerplate code and improve maintainability.

  **▸ Persistence Layer:** Managing an embedded Apache Derby database for local data storage and search history.

🚀 **Technical Stack**
  - **Runtime:** Java 17 (LTS)

  - **Build Tool:** Maven

  - **GUI Framework:** Java Swing (Manual Layout Management)

  - **Networking:** OkHttp 4.x

  - **Data Interchange:** JSON (via Google Gson)

  - **Database:** Apache Derby (Embedded Mode)

*Note: As this project is currently under active refactoring, the source code represents a transitionary state between legacy implementation and modern Java standards.*

<img width="524" height="341" alt="Screenshot 2026-03-06 182826" src="https://github.com/user-attachments/assets/4965eb84-d5fe-4489-960d-71132b2abf54" />  
<img width="585" height="457" alt="Screenshot 2026-03-06 182921" src="https://github.com/user-attachments/assets/7629c457-3ca0-411a-a11d-b67513877399" />
<img width="1146" height="709" alt="Screenshot 2026-03-06 182958" src="https://github.com/user-attachments/assets/f48be604-4fa6-45a5-b5f6-196a13db291b" />
