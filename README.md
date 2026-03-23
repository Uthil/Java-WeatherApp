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

**Status: ⚙️ In Progress — Core functionality complete, architectural improvements ongoing**

## 📸 Screenshots

<p align="center">
  <img width="517" height="332" alt="main" src="https://github.com/user-attachments/assets/ad1e675f-9f23-4565-ba99-0537c68b9f42" />
  <br>
  <strong>Main Menu</strong>
</p>

<p align="center">
  <img width="287" height="394" alt="search" src="https://github.com/user-attachments/assets/6967c298-209d-44ec-baf8-fc2446b45afe" />
  <br>
  <strong>Search by: City</strong>
</p>

<p align="center">
  <img width="1146" height="709" alt="results-full" src="https://github.com/user-attachments/assets/f48be604-4fa6-45a5-b5f6-196a13db291b" />
  <br>
  <strong>Results Window — Full Parameters View</strong><br>
  <em>Original layout displaying all available weather parameters</em>
</p>

<p align="center">
  <img width="886" height="781" alt="NEW" src="https://github.com/user-attachments/assets/a3b7710c-fc45-411a-b86b-76a00e3bed36" />
  <br>
  <strong>Results Window — New Stylized View</strong><br>
  <em>Redesigned layout with core parameters. A "Details" button per day is planned for extended weather data.</em>
</p>

<p align="center">
  <img width="890" height="782" alt="save" src="https://github.com/user-attachments/assets/a28ef8d5-a8fa-4462-a6fa-c79536a8dd33" />
  <br>
  <strong>Saving Weather Data</strong>
</p>

<p align="center">
  <img width="1920" height="1032" alt="editsavedData" src="https://github.com/user-attachments/assets/5cf2ff37-8cb6-4291-a141-f118e8138ead" />
  <br>
  <strong>Editing Saved Data</strong><br>
  <em>Currently using the original layout — restyling planned in upcoming update</em>
</p>

<p align="center">
  <img width="1920" height="1032" alt="deleteSavedData" src="https://github.com/user-attachments/assets/ee610071-1fae-4b2a-a1a7-17983524307f" />
  <br>
  <strong>Deleting City Data</strong>
</p>

<p align="center">
  <img width="888" height="785" alt="confirmedDeletion" src="https://github.com/user-attachments/assets/2ebe8d76-d376-4f58-af3a-7341feb0d3fe" />
  <br>
  <strong>Deletion Confirmed</strong>
</p>
