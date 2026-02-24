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
