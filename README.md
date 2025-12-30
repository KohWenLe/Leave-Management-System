# Leave Management System 🗒️💻

This repository contains a **Java-based Leave Management System** developed as part of Object-oriented Programming course. The system provides a graphical user interface for employees to apply for leave and for administrators to manage leave records.

---

## Overview

The Leave Application System is a desktop application built using **Java Swing**. It supports two user roles:

- **Employee** – apply for leave and manage personal leave records
- **Admin** – manage leave records of all employees

The system uses **file handling** for data storage and focuses on object-oriented design principles.

---

## Features

### Employee
- Register and login
- Apply for leave
- View personal leave records
- Edit or delete pending leave applications

### Admin
- Login authentication
- View leave records of all employees
- Edit leave details and update leave status
- Delete leave records
- Add leave records for registered employees

---

## System Characteristics

- Role-based access (Employee / Admin)
- Auto-generated User ID and Leave ID
- Input validation and error handling
- Simple and intuitive Java Swing GUI
- File-based data storage (text files)

---

## Technologies Used

- Java
- Java Swing (GUI)
- Object-Oriented Programming concepts
- File handling for data persistence

---

## Project Scope

- Employee leave application management
- Admin leave approval and record management
- CRUD operations on leave records
- Academic system modelling (use case, class, and state diagrams)

---

## How to Run the Application

### Prerequisites

* Java Development Kit (JDK) installed
* Windows operating system
* Command Prompt access

### Steps to Run

1. Open **Command Prompt**.
2. Navigate to the project directory:

   ```
   cd [path_to_project_folder]
   ```
3. Run the application:

   ```
   javac Login.java
   java Login
   ```

---

## Login Information

### Employee Login

* Employees must **register an account first** before logging in.
* Registered user credentials are stored in the `registerInfo.txt` file.

### Admin Login

Use the following default admin credentials:

* **User ID:** `@dmin666`
* **Password:** `@dminPa$$word`

---

Once logged in, the system will automatically load the appropriate interface based on the user role (Employee or Admin).
<img width="570" height="610" alt="image" src="https://github.com/user-attachments/assets/1924a54d-d547-4f69-8fe4-01e38762ce41" />


Thank you for using the **BreakBuddy Leave Application System**.

## Notes

This project is developed for **educational purposes** to demonstrate object-oriented design, Java Swing GUI development, and basic file handling. It is not intended for production use.
