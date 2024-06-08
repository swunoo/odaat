【English follows】

# Design (V1.0, 2024/06/08)

- 通常、基本設計書、詳細設計書、画面設計書などが作成されます。
- しかし、このプロジェクトの開発を迅速に進めるために、UI、API、およびデータベース設計の簡潔なドキュメントのみを作成します。

## UI設計
- 以下は、3つの主要画面の設計です。
- これらはアイデアを視覚化するためのものであり、詳細は変更される可能性があります。

### 1. プロジェクト管理画面
- ユーザーが自分のプロジェクトを管理するための画面です。
- プロジェクトの追加、編集、削除が可能です。

### 2. タスク管理画面
- 特定のプロジェクトに属するタスクを管理する画面です。
- タスクの追加、編集、削除が可能です。

### 3. 日々のタスク画面
- その日のタスクリストを表示する画面です。
- タスクの開始/終了時刻や期間を設定できます。

## API設計
| Entity | Method | Endpoint | Body | Responses |
| --- | --- | --- | --- | --- |
| Program | GET | /get | NA | 200 Program[], 404 |
| Program | GET | /detail/:id | NA | 200 Program, 404, 400 |
| Program | POST | /add | Program | 200 Program, 400 |
| Program | PUT | /update/:id | Program | 200 Program, 400 |
| Program | DELETE | /delete/:id | NA | 200, 400 |

| Task | GET | /get?{date|project|status} | NA | 200 Task[] |
| Task | GET | /detail/:id | NA | 200 Task, 400 |
| Task | POST | /add | Task | 200 Task, 400 |
| Task | PUT | /update/:id | Task | 200 Task, 400 |
| Task | PUT | /confirm/:id | NA | 200 Task, 400 |
| Task | PUT | /change/:id?{status} | NA | 200 Task, 400 |
| Task | PUT | /change/:id?{priority} | NA | 200 Task, 400 |
| Task | DELETE | /delete/:id | NA | 200, 400 |

| Project | GET | /get?{status|program} | NA | 200 Project[] |
| Project | GET | /detail/:id | NA | 200 Project, 400 |
| Project | POST | /add | Project | 200 Project, 400 |
| Project | PUT | /update/:id | Project | 200 Project, 400 |
| Project | DELETE | /delete/:id | NA | 200, 400 |

## データベース設計
- データはリレーショナルであり、ACID特性と使いやすさを求めて、RDBMS（MySQL）を選択します。
- エンティティとER図は以下の通りです。

#### Program（プログラム）
| カラム | タイプ | 制約 |
| --- | --- | --- |
| id | long | PK, AUTO_INCREMENTED |
| userid | long | FK id on user |
| name | varchar(64) | NOT NULL |
| created_at | datetime | AUTO_CREATED, NON_EDITABLE |
| updated_at | datetime | AUTO_UPDATED, NON_EDITABLE |

#### Task（タスク）
| カラム | タイプ | 制約 |
| --- | --- | --- |
| id | long | PK, AUTO_INCREMENTED |
| program_id | long | FK id on program |
| name | varchar(64) | NOT NULL |
| description | text |  |
| due_date | datetime |  |
| status | varchar(32) | NOT NULL |
| created_at | datetime | AUTO_CREATED, NON_EDITABLE |
| updated_at | datetime | AUTO_UPDATED, NON_EDITABLE |

#### User（ユーザー）
| カラム | タイプ | 制約 |
| --- | --- | --- |
| id | long | PK, AUTO_INCREMENTED |
| name | varchar(64) | NOT NULL |
| email | varchar(128) | UNIQUE, NOT NULL |
| password | varchar(128) | NOT NULL |
| created_at | datetime | AUTO_CREATED, NON_EDITABLE |
| updated_at | datetime | AUTO_UPDATED, NON_EDITABLE |

---

# Design (V1.0, 2024/06/08)

- Usually, basic design documents, detailed design documents, screen design documents, etc. are created.
- However, to speed up development for this project, I will only make brief documentation for UI, API, and Database designs.

## UI Design
- Designs for three main screens are as follows.
- These are just to visualize the idea with the details subject to change.

### 1. Project Management Screen
- A screen for users to manage their projects.
- Users can add, edit, and delete projects.

### 2. Task Management Screen
- A screen to manage tasks that belong to a specific project.
- Users can add, edit, and delete tasks.

### 3. Daily Tasks Screen
- A screen to display the task list for the day.
- Users can set start/end times or durations for each task.

## API Design
| Entity | Method | Endpoint | Params | Successful Response |
| --- | --- | --- | --- | --- |
| Program | GET | /getPrograms |  | Program[] |
| Task | GET | /getTasks |  | Task[] |

## Database Design
- Since the data is relational, I want ACID properties, and ease of use, I choose a RDBMS (MySQL).
- Entities and ER Diagram are as follows.

#### Program
| Column | Type | Constraints |
| --- | --- | --- |
| id | long | PK, AUTO_INCREMENTED |
| userid | long | FK id on user |
| name | varchar(64) | NOT NULL |
| created_at | datetime | AUTO_CREATED, NON_EDITABLE |
| updated_at | datetime | AUTO_UPDATED, NON_EDITABLE |

#### Task
| Column | Type | Constraints |
| --- | --- | --- |
| id | long | PK, AUTO_INCREMENTED |
| program_id | long | FK id on program |
| name | varchar(64) | NOT NULL |
| description | text |  |
| due_date | datetime |  |
| status | varchar(32) | NOT NULL |
| created_at | datetime | AUTO_CREATED, NON_EDITABLE |
| updated_at | datetime | AUTO_UPDATED, NON_EDITABLE |

#### User
| Column | Type | Constraints |
| --- | --- | --- |
| id | long | PK, AUTO_INCREMENTED |
| name | varchar(64) | NOT NULL |
| email | varchar(128) | UNIQUE, NOT NULL |
| password | varchar(128) | NOT NULL |
| created_at | datetime | AUTO_CREATED, NON_EDITABLE |
| updated_at | datetime | AUTO_UPDATED, NON_EDITABLE |