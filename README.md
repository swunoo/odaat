# odaat

Personal task management tool, built with
- Scala (Play framework)
- JavaScript/TypeScript (React)
- MySQL

<ul>
    <li><a href='#features'> Features </a></li>
    <li><a href='#setup'> Setup for Demo </a></li>
</ul>

<h2 id="features"> Features </h2>

### Task Management
- Keep track of your daily tasks.
- Create and update tasks for each day.

### Projects
- Organize your tasks into projects.

<h2 id="setup"> Setup </h2>

You can run the application with the following steps.
1. Download or clone this repository
2. Do either of the following

### Option 1: Using Docker
1. Install [docker](https://docs.docker.com/engine/install/) and [docker compose](https://docs.docker.com/compose/install/) on your machine.
2. Run `docker-compose up`

### Option 2: Using Shell
1. Make [MySQL](https://www.mysql.com/) available on your machine.
2. Setup database and user credentials in accordance with [application configurations](https://github.com/swunoo/odaat/blob/main/server/odaat-server/conf/application.conf).
3. Make the shell script executable by running `chmod +x run.sh`
4. Run the shell script with `./run.sh`