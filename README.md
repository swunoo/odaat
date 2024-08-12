![Logo](./readme_images/demo/logo.png)

---
# Odaat Version 1.0.0 | 2024 June 24
---

## Table of Contents
- Application
- Functionalities
- Technologies
- Setup
- Further Development

## Application
- A personal management application that can be used to organize your work into **projects**, **tasks**, and **daily todos**.
- "Projects" are long term work, or repetitive activities. They can include both professional and personal work. Some examples would be *"Create a Marketing Campaign"*, *"Build MyApp"*, or *"Excersise Daily"*.
- "Tasks" are small chunks of work that can be done in one sitting towards the completion of a certain project. Some examples would be *"Design the Key Visual"*, *"Write Unit Tests"*, or *"Go for a Walk"*.
- With the aim of consistently making incremental improvements, the application is named **"Odaat"**, or *"One Day at a Time"*.

## Functionalities
- User can login / signup / logout with an email+password, or with a google account.
![Login Page](./readme_images/demo/login.png)

- User can create/update/delete projects.
![Projects Page](./readme_images/demo/projects.png)
![Create Project](./readme_images/demo/projects_create.png)

- User can create/update/delete tasks.
![Tasks Page](./readme_images/demo/tasks.png)
![Create Tasks](./readme_images/demo/tasks_create.png)

- User can synchronize projects and issues they have on Backlog with Odaat, via oauth2, to automatically generate projects and tasks.

## Technologies
- Backend:  Java (Spring Boot, Spring Security, Spring Data)
- Frontend: TypeScript (React)
- UI:       TailwindCSS
- Testing:  JUnit, Jest, Playwright
- Database: MySQL
- Others:   Docker, Flyway, auth0

## Setup
### Requirements
- JDK
- Node
- MySQL

### Steps
- Create an `application.properties` file under `/spring-server/odaat/src/main/resources`.
- Populate the file with sample properties from `/spring-server/odaat/src/main/resources/application.properties.example`.
- Fill in necessary values.
- Run `./run-dev.sh`
- Web application can be accessed on `localhost:5173`.

### Testing
- Unit and integration tests can be executed with `./run-test.sh`.

## Further Development
### Frontend
- Improve responsiveness.
- Handle different `Date` formats.
- Separate logic and UI.

### Backend
- Validate create/update requests.
- Version APIs.
- Improve error messages.

### Features
- Sync local data to Backlog.
- Generate tasks automatically when a Project is created.
- Visualize progress.