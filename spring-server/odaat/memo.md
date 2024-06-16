# Connecting with Backlog

FE  - User click "Sync Backlog" button
    - Show "Loading"
    - Call /sync/backlog
BE  - /sync/backlog endpoint is called
    - **Backlog OAuth Permission** DONE
    - Fetch projects -> convert into Project DONE
    - If Project doesn't exist in DB or is changed, create/update DONE
    - Fetch issues -> convert into task for each project DONE
    - Query incompleted tasks of that refId and sum durationHr DONE
    - If currTimeLeft < timeLeft: create tasks and insert DONE
    - If currTimeLeft > timeLeft: mark outstanding tasks as completed DONE
    - When updated -> Check the task and if it has an issue, update actualHours in the issue. DONE
FE  - If the sync was successful, refresh the page
    - Otherwise, show an error

# TODOs

- Auth
- Request Validation
- API versioning
- Scalability
- Security