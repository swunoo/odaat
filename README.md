# ODAAT - Personal Task Management Tool

<div style="display: flex; flex-direction: row; gap: 100px;">
    <img style="height:18px; padding-right: 5px;" src="./readme_images/scala.png" />
    <p style="padding-left: 20px; padding-right: 5px;">Scala</p>
    <img style="height:18px; " src="./readme_images/play.png" />
    <p style="padding-left: 20px; padding-right: 5px;">Play</p>
    <img style="height:18px; " src="./readme_images/js.png" />
    <p style="padding-left: 20px; padding-right: 5px;">JavaScript</p>
    <img style="height:18px; " src="./readme_images/ts.png" />
    <p style="padding-left: 20px; padding-right: 5px;">TypeScript</p>
    <img style="height:18px; " src="./readme_images/react.png" />
    <p style="padding-left: 20px; padding-right: 5px;">React</p>
    <img style="height:18px; " src="./readme_images/mysql.png" />
    <p style="padding-left: 20px; padding-right: 5px;">MySQL</p>
    <img style="height:18px; " src="./readme_images/docker.png" />
    <p style="padding-left: 20px; padding-right: 5px;">Docker</p>
</div>

## Tech Stack
- Scala (Play framework)
- JavaScript/TypeScript (React)
- MySQL

## Features

### Tasks
![Task Page](./readme_images/task.png)
- Keep track of all tasks.
- Oragnize tasks based on date and project.
- View detailed tasks for each day.

### Projects
![Project Page](./readme_images/project.png)
- Turn your long-term goals into doable chunks.
- Create, update, and prioritize work.
- Manage tasks based on different projects.

### Progress
![Progress Page](./readme_images/progress.png)
- Update your progress with a single click.
- Track completion dates and deadlines for each project.
- Adjust daily tasks by moving them between dates.

## Setup
You can run the application by:
1. Downloading this repository
2. Executing either of the following options

#### Option 1: Using Docker
1. Install [docker](https://docs.docker.com/engine/install/) and [docker compose](https://docs.docker.com/compose/install/) on your machine.
2. Run `docker compose up -d`
3. Go to `http://localhost:9090/`

#### Option 2: Using Bash
1. Make [MySQL](https://www.mysql.com/) available on your machine.
2. Setup database and user credentials in accordance with [application configurations](https://github.com/swunoo/odaat/blob/main/server/odaat-server/conf/application.conf).
3. Make the script executable by running `chmod +x run.sh`
4. Run the script with `./run.sh`
3. Go to `http://localhost:5173/`

## Further Development
The following features are to be added soon.
- Cloud deployment with authentication
- Daily/monthly report generation
- Calendar view
