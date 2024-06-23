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
- OPTIONS requests still showing CORS errors, although GET requests work fine. > Adding "Allowed Headers" resolved it.
- Cannot handle /callback from Backlog API yet -> for some reason, the routes have to be prefixed with '/api'.

# TODOs

- Request Validation
- API versioning
- Scalability
- Responsiveness
- JavaScript's datetime mess

### For Robustness and Scalability
- Refactor the code <---
    -   Refactor the code,
    -   Run all tests again,
    -   Write the documentation.

### For Maintainability
- Expand unit tests <--- DONE
- Add specifications and end-to-end tests <--- DONE

### For Compatibility
- Containerize the application <--- DONE, should be checked again after refactorings

### Further Features
- Make the webapp more responsive
- Validate Requests
- Version APIs

### Further Learning
- Scalability
- OAuth2