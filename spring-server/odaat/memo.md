# Connecting with Backlog

FE  - User click "Sync Backlog" button
    - Show "Loading"
    - Call /sync/backlog
BE  - /sync/backlog endpoint is called
    - **Backlog OAuth Permission**
    - Fetch projects -> convert into Project
    - If Project doesn't exist in DB or is changed, create/update
    - Fetch issues -> convert into { refId: timeLeft } for each project
    - Query incompleted tasks of that refId and sum durationHr
    - If currTimeLeft < timeLeft: create tasks and insert
    - If currTimeLeft > timeLeft: mark outstanding tasks as completed
    - When updated -> Check the task and if it has an issue, update actualHours in the issue.
FE  - If the sync was successful, refresh the page
    - Otherwise, show an error

# TODOs

- Request Validation
- API versioning