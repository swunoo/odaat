# ODAAT - Personal Task Management Tool 

## Tech Stack
- Scala (Play framework)
- JavaScript/TypeScript (React)
- MySQL

## Features

#### Tasks
![Task Page](./screenshots/task.png)
- Keep track of all tasks.
- Oragnize tasks based on date and project.
- View detailed tasks for each day.

#### Projects
![Project Page](./screenshots/project.png)
- Turn your long-term goals into doable chunks.
- Create, update, and prioritize work.
- Manage tasks based on different projects.

### Progress
![Progress Page](./screenshots/progress.png)
- Update your progress with a single click.
- Track completion dates and deadlines for each project.
- Adjust daily tasks by moving them between dates.

## Setup
You can run the application with the following steps.
1. Download or clone this repository
2. Do either of the following

#### Option 1: Using Docker
1. Install [docker](https://docs.docker.com/engine/install/) and [docker compose](https://docs.docker.com/compose/install/) on your machine.
2. Run `docker-compose up`

#### Option 2: Using Bash
1. Make [MySQL](https://www.mysql.com/) available on your machine.
2. Setup database and user credentials in accordance with [application configurations](https://github.com/swunoo/odaat/blob/main/server/odaat-server/conf/application.conf).
3. Make the shell script executable by running `chmod +x run.sh`
4. Run the shell script with `./run.sh`

## Further Development
The following features are to be added soon.
- Cloud deployment with authentication
- Daily/monthly report generation
- Calendar view