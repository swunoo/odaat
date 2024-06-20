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

## Issues
- updating issue through PATCH doesn't work.
org.springframework.web.client.ResourceAccessException: I/O error on PATCH request for {URL}: Invalid HTTP method: PATCH

# Auth

- Login/Logout from React using auth0 is working.
- OPTIONS requests still showing CORS errors, although GET requests work fine.

# TODOs

- Request Validation
- API versioning
- Scalability
