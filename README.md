# 🌊 Ocean View Resort - Room Reservation System

## About The Project
This is a computerized Room Reservation Management System developed for the front-desk staff of Ocean View Resort in Galle. The system replaces the old manual paper-based booking method. It is designed to be very fast, secure, and user-friendly. 

To strictly follow the university assignment guidelines, this backend was built using **Pure Java EE (Servlets and JSP)** without using any high-level frameworks like Spring Boot or Hibernate. 

## 🚀 Key Features
* **Role-Based Authentication:** Two user roles (Admin and Staff). Passwords are encrypted using the SHA-256 hashing algorithm before saving to the database.
* **Double Booking Prevention:** A custom SQL logic runs before saving a booking to check if the room is already occupied for the selected dates. It blocks overlapping bookings automatically.
* **Automated Bill Calculation:** The total bill is not calculated using Java. Instead, a **MySQL Stored Procedure** is used to calculate the date differences and room rates accurately inside the database.
* **Interactive Dashboard:** A Single Page Application (SPA) style dashboard. It uses AJAX (Fetch API) and Google GSON to load data dynamically without refreshing the web page.
* **Audit Trailing:** The system tracks which staff member added or edited a reservation using session tracking and displays it on the printed bill receipt.
* **Staff Management:** Admin users have a special control panel to securely register new staff members or remove old accounts.

## 🛠️ Built With (Tech Stack)
* **Frontend:** HTML5, CSS3, Bootstrap 5, SweetAlert2, JavaScript (Fetch API)
* **Backend:** Pure Java EE (Servlets, JSP)
* **Database:** MySQL 8.0 (JDBC)
* **Testing:** JUnit 5 (Test-Driven Development)
* **Data Format:** JSON (Google Gson Library)
* **Server:** Apache Tomcat v10+
* **Build Tool:** Maven

## 🧩 Design Patterns Used
Even though no frameworks were used, the code is highly organized using standard software design patterns:
1. **MVC (Model-View-Controller):** Code is cleanly separated into Model classes, JSP Views, and Servlet Controllers.
2. **DAO (Data Access Object):** All SQL queries are isolated inside separate DAO classes (`ReservationDAO`, `UserDAO`) to hide database logic from the Servlets.
3. **Singleton Pattern:** The `DBConnection` class uses the Singleton pattern to ensure only one database connection is created, which saves memory and prevents server crashes.

## ⚙️ Getting Started / Installation

### 1. Database Setup
1. Open MySQL Workbench.
2. Create a database named `ocean_view_db`.
3. Run the provided SQL script to create the `users`, `rooms`, and `reservations` tables, and to generate the `CalculateBill` stored procedure.
4. The default rooms (Single, Double, Suite) and default admin account will be inserted automatically.

### 2. Project Setup
1. Clone this repository to your local machine.
2. Open the project in IntelliJ IDEA (or any Java IDE).
3. Go to `src/main/java/com/oceanviewsystem/util/DBConnection.java` and update the database username and password if necessary.
4. Reload the Maven project to download the dependencies (MySQL Connector, Gson, JUnit).

### 3. Run the Application
1. Add a Local Tomcat Server configuration in your IDE.
2. Build the project and deploy the `.war` file to the Tomcat server.
3. Open your browser and go to `http://localhost:8080/OceanViewSystem/`.

## 🔐 Default Login Credentials
* **Admin Account:** Username: `admin` | Password: `admin123`
* **Staff Account:** Username: `staff1` | Password: `staff123`

## 🧪 Testing
Test-Driven Development (TDD) was used to ensure the system works perfectly. The `src/test/java` folder contains several JUnit 5 test classes. They automatically test:
* Database Singleton Connection
* Password Hashing logic
* Database CRUD operations
* Room Availability (Double Booking logic)
