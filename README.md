# InstaCar

**InstaCar** is a car renting system designed to make vehicle rental easy, fast, and efficient.

## Table of Contents

- [About](#about)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Creators](#creators)

## About

InstaCar is a web-based application for managing car rentals. It provides an intuitive interface for users to browse, book, and manage rental cars, as well as administrative tools for managing fleet inventory and reservations.

## Features

- User registration and authentication
- Browse available cars with filters
- Book and manage reservations
- Admin dashboard for car and booking management
- Responsive design for desktop and mobile

## Tech Stack

- **Frontend:** HTML, CSS, Bootstrap, JavaScript, Thymeleaf
- **Backend:** Java, Spring Boot
- **Build Tools:** Maven, Makefile
- **Contenerization:** Docker
- **DB & Cache:** PostgreSQL, Redis
- **Utilities:** Flyway, Lombok

## Getting Started

### Prerequisites

- Docker and docker compose

### Installation

1. **Clone the repository:**
   ```sh
   git clone https://github.com/sgorski00/InstaCar.git
   cd InstaCar
   ```

2. **(Optionally) Create your prod profile:**
   - copy `/src/main/reseources/applicaiton-dev.properties` to `/src/main/reseources/applicaiton-prod.properties`
   - configure OAuth2 client and mail serer connection
   - change value in `spring.profiles.default` to `prod` in main `application.properties` file

3. **Run the application:**
   ```sh
   make up
   ```

## Usage

- Visit `http://localhost` in your browser
- Browse available cars
- You must register a new account or log in to make reservation
- You must be logged in as Adminsitrator to see the Admin Panel

## Creators

- [sgorski00](https://github.com/sgorski00)
- [jgrzymislawski](https://github.com/jgrzymislawski)
- [lukasz1991](https://github.com/lukaszsz1991)

---

*Happy renting!*
