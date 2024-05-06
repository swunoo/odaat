# Required APIs

## v1

| endpoint | method | payload | response | description |
| --- | --- | --- | --- | --- |
| /project/getAll | GET | | 200 Project[] <br/> 500 Error | Get all data of all projects |
| /project/getTitles | GET | | 200 String[] <br/> 500 Error | Get titles of all projects |
| /project/add | POST | NewProject | 200 Project <br/> 400 Error <br/> 500 Error | Create a new project |
| /project/update | PUT | Project | 200 Project <br/> 400 Error <br/> 500 Error | Update an existing project |
| /project/delete/:title | DELETE | | 200 <br/> 400 Error <br/> 500 Error | Delete an existing project |
| /task/getAll?date=:date | GET | | 200 Task[] <br/> 500 Error | Get all tasks on a date |
| /task/getAll?project=:project | GET | | 200 Task[] <br/> 500 Error | Get all tasks of a project |
| /task/add | POST | NewTask | 200 Task <br/> 400 Error <br/> 500 Error | Create a new task |
| /task/update | PUT | Task | 200 Task <br/> 400 Error <br/> 500 Error | Update an existing task |
| /task/delete/:id | DELETE | | 200 <br/> 400 Error <br/> 500 Error | Delete an existing task |
| /task/updateStatus/:id?status=:status | PUT | | 200 Task <br/> 400 Error <br/> 500 Error | Update the status of an existing task |
