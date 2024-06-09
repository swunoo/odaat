# Design (V1.0, 2024/06/08)

- Usually, basic design documents, detailed design documents, screen design documents, etc. are created.
- However, to speed up development for this project, I will only make brief documentation for UI, API, and Database designs.

## UI Design
- Designs for three main screens are as follows.
- These are just to visualize the idea with the details subject to change.

*Design available upon request*

## API Design
### Endpoints
| ID | Method | Endpoint | Body | Responses |
| --- | --- | --- | --- | --- |
| C01 | GET | /get |  | 200 Category[] |
| C02 | GET | /detail/:id |  | 200 Category, 400 |
| C03 | POST | /add | Category | 200 Category, 400 |
| C04 | PUT | /update/:id | Category | 200 Category, 400 |
| C05 | DELETE | /delete/:id |  | 200, 400 |
| T01 | GET | /get?{date&project&status} |  | 200 Task[] |
| T02 | GET | /detail/:id |  | 200 Task, 400 |
| T03 | POST | /add | Task | 200 Task, 400 |
| T04 | PUT | /update/:id | Task | 200 Task, 400 |
| T06 | PUT | /change/:id?{status} |  | 200 Task, 400 |
| T07 | PUT | /change/:id?{priority} |  | 200 Task, 400 |
| T08 | DELETE | /delete/:id |  | 200, 400 |
| P01 | GET | /get?{status&program} |  | 200 Project[] |
| P02 | GET | /detail/:id |  | 200 Project, 400 |
| P03 | POST | /add | Project | 200 Project, 400 |
| P04 | PUT | /update/:id | Project | 200 Project, 400 |
| P05 | PUT | /change/:id?{status} |  | 200 Project, 400 |
| P06 | PUT | /change/:id?{priority} |  | 200 Project, 400 |
| P07 | DELETE | /delete/:id |  | 200, 400 |
| Bg01 | GET | /sync |  | 200, 403 |


### Entities
| Endpoint ID | Entity | Prefix |
| --- | --- | --- |
| C* | Category | /category |
| P* | Project | /project |
| T* | Task | /task |
| Bg* | Backlog | /backlog |

## Database Design
- Since the data is relational, I want ACID properties, and ease of use, I choose a RDBMS (MySQL).

### Entities

#### Uzer
| Column | Type | Constraints |
| --- | --- | --- |
| id | int | PK, AUTO_INCREMENTED |
| name | varchar(32) | DEFAULT "Anon" |
| email | varchar(255) | NOT NULL |
| password | varchar(72) | NOT NULL |
| is_deleted | boolean | DEFAULT "false" |
| deleted_at | datetime | |
| created_at | datetime | AUTO_CREATED |
| updated_at | datetime | AUTO_UPDATED |

#### Category
| Column | Type | Constraints |
| --- | --- | --- |
| id | int | PK, AUTO_INCREMENTED |
| uzer_id | int | FK id on uzer |
| name | varchar(64) | NOT NULL |
| created_at | datetime | AUTO_CREATED |
| updated_at | datetime | AUTO_UPDATED |

#### Project
| Column | Type | Constraints |
| --- | --- | --- |
| id | int | PK, AUTO_INCREMENTED |
| uzer_id | int | FK id on uzer |
| category_id | int | FK id on category |
| name | varchar(64) | DEFAULT "now()" |
| description | text | DEFAULT "" |
| status | project_status | DEFAULT "created" |
| priority | priority_level | DEFAULT "low" |
| start_time | datetime | |
| end_time | datetime | |
| due_time | datetime | |
| estimated_hr | double | |
| daily_hr | double | |
| is_deleted | boolean | DEFAULT "false" |
| deleted_at | datetime | |
| created_at | datetime | AUTO_CREATED |
| updated_at | datetime | AUTO_UPDATED |

#### Task
| Column | Type | Constraints |
| --- | --- | --- |
| id | int | PK, AUTO_INCREMENTED |
| uzer_id | int | FK id on uzer |
| project_id | int | FK id on project |
| description | text | DEFAULT "" |
| status | task_status | DEFAULT "created" |
| priority | priority_level | DEFAULT "low" |
| start_time | datetime | |
| duration_hr | int | DEFAULT "2" |
| created_at | datetime | AUTO_CREATED |
| updated_at | datetime | AUTO_UPDATED |

### Custom Types
| No. | Type | Possible Values |
| --- | --- | --- |
| 1 | project_status | CREATED, STARTED, COMPLETED, PAUSED, STOPPED |
| 2 | task_status | GENERATED, PLANNED, COMPLETED |
| 3 | priority_level | LOWEST, LOW, MEDIUM, HIGH, HIGHEST |