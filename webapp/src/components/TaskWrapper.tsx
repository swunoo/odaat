/* Methods and component of TaskBlocks, used by both Tasks.tsx and Projects.tsx */

import { useEffect, useState } from "react"
import menuIcon from '../assets/images/menu.svg'
import { PROJECT_API, ProjectData, TASK_API, TaskData, TaskRequest } from "../conf"
import { combineDateAndTimeInput, formatTime, getTimeRange, getValue, menuBtnStyle } from "../utils"
import { NewButton, NewTaskButton, VoidFunc } from "./common"
import { useCookies } from "react-cookie"

export function TaskWrapper(
    { project, date, addProject, newProj }
        : { project?: ProjectData, date?: Date, addProject?: VoidFunc, newProj?: ProjectData }
) {

    const [cookies] = useCookies(['XSRF-TOKEN']); // <.>

    const [tasks, setTasks] = useState<TaskData[]>([])

    const [projectList, setProjectList] = useState<ProjectData[]>([])

    useEffect(() => {
        // API: QUERY ALL TASKS OF A GIVEN PROJECT
        let taskQuery = TASK_API + '/get?'
        if (project) taskQuery += 'projectId=' + project.id
        if (date) taskQuery += '&date=' + date.toISOString().split('T')[0];

        fetch(taskQuery, {
            method: 'GET', credentials: 'include'
        })
            .then(res => res.json())
            .then(data => setTasks(data))
            .catch(err => console.log(err))

    }, [date])

    useEffect(() => {
        // API: GET ALL PROJECTS
        fetch(PROJECT_API + '/getIdName', {
            method: 'GET', credentials: 'include'
        })
            .then(res => res.json())
            .then(data => setProjectList(data))
            .catch(err => console.log(err))
    }, [])

    useEffect(() => {
        if(newProj) setProjectList([...projectList, newProj])
    }, [newProj])

    const initTask: TaskData = {
        id: 0,
        project: project ?? projectList[0],
        description: 'Task Name',
        status: 'PLANNED',
        priority: 'LOW',
        startTime: date ?? new Date(),
        durationHr: 1,
        createdAt: date ?? new Date(),
        updatedAt: date ?? new Date()
    }

    // When "Add Task" button is clicked, init a new Task
    const addTask = () => {
        if(!initTask.project) {
            alert('Please add a project first.')
            return;
        }
        setTasks([...tasks, initTask])
    }

    // After adding a task, it is updated in the block.
    const changeTask = (idx: number, task: TaskData) => {
        const newTasks = [...tasks]
        newTasks[idx] = task;
        setTasks(newTasks);
    }

    // When "Delete" button from a Task is clicked, delete it
    const removeTask = (idx: number) => {
        const taskId = tasks[idx]['id']
        // For cancelling "Create New", it is not yet saved in the database
        if (taskId === 0) {
            setTasks(tasks.filter((_, i) => i !== idx))
            return;
        }

        // API: DELETE A TASK
        fetch(TASK_API + '/delete/' + taskId, {
            method: 'DELETE', credentials: 'include',
            headers: { 'X-XSRF-TOKEN': cookies['XSRF-TOKEN'] } 
        })
            .then(res => {
                if (res.ok) {
                    setTasks(tasks.filter((_, i) => i !== idx))
                }
                else {
                    console.log(res.text());
                    alert("Unable to delete")
                }
            }).catch(err => console.log(err))
    }

    return (
        <div className={project ? "overflow-scroll" : ""}>
            <div>
                {
                    tasks.map((t, idx) => (
                        <TaskBlock
                            key={t.id}
                            initData={t}
                            remover={() => removeTask(idx)}
                            taskSetter={(task) => changeTask(idx, task)}
                            isTaskPage={!project}
                            projects={projectList}
                            addProject={addProject}
                        />
                    ))
                }
            </div>

            <div className={
                "flex justify-end"
                + (!project ? ' absolute top-2 right-2' : ' py-10')
            }>
                <NewTaskButton clickHandler={addTask} />
            </div>
        </div>
    )
}

/* Component of Each Task */
export function TaskBlock(
    { initData, projects, addProject, remover, taskSetter, isTaskPage }:
        { initData: TaskData, projects?: ProjectData[], addProject?: VoidFunc, remover: VoidFunc, taskSetter: (task: TaskData) => void, isTaskPage: boolean }) {

    const [cookies] = useCookies(['XSRF-TOKEN']); // <.>

    // Data of the Task
    const [data, setData] = useState<TaskData>(initData)
    // Whether to show Task menu
    const [showMenu, setShowMenu] = useState(false)
    // Whether the Task is a new Task, or an existing one under update
    const [isNew, setIsNew] = useState(data.id === 0)
    // Whether in EDIT mode
    const [edit, setEdit] = useState(isNew)

    const taskTimeStyle = "text-right " + (isTaskPage ? "col-span-2" : "col-span-4");

    useEffect(() => {
        setData(initData)
    }, [initData])

    // When the "COMPLETED" checkbox is toggled, status is updated
    const changeCompletion = () => {
        const newStatus = data.status === 'COMPLETED' ? 'PLANNED' : 'COMPLETED'
        // API: UPDATE THE STATUS OF A TASK
        fetch(TASK_API + '/updateStatus/' + data.id + '?status=' + newStatus, {
            method: 'PUT', credentials: 'include',
            headers: { 'X-XSRF-TOKEN': cookies['XSRF-TOKEN'] } 
        })
            .then(res => {
                if (res.ok) {
                    setData({ ...data, status: newStatus })
                }
                else {
                    console.log(res.text());
                    alert("Unable to update")
                }
            }).catch(err => console.log(err))
    }

    // When the "Cancel" button on EDIT mode is clicked, data is not saved.
    const cancelEdit = () => {
        if (isNew) remover();
        else setEdit(false);
    }

    // When the "Confirm" button on EDIT mode is clicked, data is saved.
    const confirmEdit = () => {

        const newData: TaskRequest = {
            projectId: getValue('task-input-proj') ? (Number)(getValue('task-input-proj')) : data.project.id,
            description: getValue('task-input-task') ?? data.description,
            status: data.status ?? 'PLANNED',
            priority: data.priority ?? 'MEDIUM',
            startTime: combineDateAndTimeInput('task-input-startTimeDate', 'task-input-startTimeTime', data.startTime),
            durationHr: (Number)(getValue('task-input-duration'))
        }

        // API: CREATE A TASK
        // API: UPDATE A TASK
        const url = TASK_API + (isNew ? `/add` : `/update/${data.id}`);
        const method = isNew ? 'POST' : 'PUT';

        fetch(url, {
            method: method,
            body: JSON.stringify(newData),
            credentials: 'include',
            headers: {
                "Content-Type": "application/json",
                'X-XSRF-TOKEN': cookies['XSRF-TOKEN']
            }
        })
            .then(res => {
                if (res.ok) {
                    return res.json()
                }
                else {
                    console.log(res.text());
                    throw new Error()
                }
            }).then(task => {
                // Add project name to task, as the response includes only the projectId
                if(projects){
                    const projData = projects.find(proj => proj.id === task.project.id);
                    task.project.name = projData ? projData.name : 'New Project'
                } else {
                    task.project.name = initData.project.name
                }

                // If we are on the Task page and the task is still on the same date, the task is shown on the page.
                // Otherwise, that task is not displayed
                if (!isTaskPage || (formatTime(task.startTime) === formatTime(data.startTime))) {
                    if (isNew) taskSetter(task)
                    else setData(task)
                } else {
                    remover()
                }
            })
            .catch(err => console.log(err))

        setIsNew(false)
        setEdit(false)
    }

    return (
        <div
            className={
                "border-b border-light py-3 grid grid-cols-16 gap-5" +
                (data.status === 'COMPLETED' ? ' bg-light opacity-50 -mx-5 px-5' : '')
            }>

            {// attr: Task IDs
                // Task IDs are shown only in TaskPage
                isTaskPage && !edit && <p className="text-gray">{data.id}</p>
            }
            {/* mode: EDIT */
                edit
                    ? (
                        <>
                            {// attr: Projects are shown only in TaskPage
                                isTaskPage

                                    // on TaskPage
                                    && <div className="col-span-3 flex flex-col gap-2">
                                        <select id="task-input-proj" className="
                                            h-fit mr-5 px-3 py-1 border-r-4 border-light
                                        " defaultValue={data.project.id}
                                        >
                                            {projects && projects.map((proj, idx) => (
                                                <option key={idx} value={proj.id}>{proj.name}</option>
                                            ))}
                                        </select>
                                        {addProject && <NewButton label="New Project" clickHandler={addProject} />}
                                    </div>
                            }

                            {/* attr: other attributes are shown in both TaskPage and ProjectPage */}
                            <textarea
                                id="task-input-task"
                                className={
                                    "border px-3 py-1 " + (isTaskPage ? 'border-light col-span-6' : 'border-gray col-span-8 bg-transparent')
                                }
                                defaultValue={data.description}></textarea>
                            <input id="task-input-startTimeDate" className="
                                    text-right px-3 py-1 h-fit bg-transparent col-span-3
                                " type="date"
                                defaultValue={data.startTime.toString()}
                            // onKeyDown={numberInputKeyDown} 
                            />
                            <input id="task-input-startTimeTime" className="
                                    text-right px-3 py-1 h-fit bg-transparent col-span-2
                                " type="time"
                                defaultValue={data.startTime.toString()}
                            />
                            <div className="flex items-center h-fit">
                                <input id="task-input-duration" className="
                                    text-right px-3 py-1 w-20 h-fit bg-transparent
                                " type="number"
                                    // onKeyDown={numberInputKeyDown} 
                                    defaultValue={data.durationHr ?? 0} />
                                <span>h</span>
                            </div>
                            <div
                                className={"flex justify-end w-fit ml-auto h-fit gap-2 col-span-full"}
                            >
                                <button
                                    onClick={confirmEdit}
                                    className={
                                        "py-1 rounded bg-accent2 hover:bg-primary "
                                        + (isTaskPage ? 'px-5 text-sm' : 'px-1 text-xs')
                                    }>Confirm</button>
                                <button
                                    onClick={cancelEdit}
                                    className={
                                        "py-1 rounded bg-gray text-white hover:bg-dark "
                                        + (isTaskPage ? 'px-5 text-sm' : 'px-1 text-xs')
                                    }>Cancel</button>
                            </div>
                        </>
                    )

                    /* mode: VIEW */
                    : (
                        <>

                            {// attr: Project and Date
                                isTaskPage
                                    ? <p className="col-span-4">
                                        <span className="border border-gray block px-3 py-1 text-sm font-medium overflow-hidden text-ellipsis max-w-fit">
                                            {data.project.name}
                                        </span>
                                    </p>
                                    : <p className="col-span-2 text-xs py-1">{formatTime(data.startTime)}</p>
                            }

                            {/* attr: description */}
                            <span className="col-span-7"
                                dangerouslySetInnerHTML={{ __html: data.description }}
                            ></span>

                            {/* attr: time (duration or start-end) */}
                            {
                                data.startTime !== null
                                    ? <span className={taskTimeStyle}>
                                        {getTimeRange(data.startTime, data.durationHr)}
                                    </span>
                                    : <span className={taskTimeStyle}> {data.durationHr + ' h'} </span>
                            }

                            {/* attr: completion status */}
                            <div
                                className={"flex justify-end relative "
                                    + (data.status !== 'COMPLETED' ? 'self-baseline ' : '')
                                    + (isTaskPage ? "gap-8 col-span-2" : "gap-3 col-span-3")}
                            >
                                <input
                                    onChange={changeCompletion}
                                    className="w-5 cursor-pointer"
                                    type="checkbox"
                                    checked={data.status === 'COMPLETED'}
                                />

                                { // controls: Menu can be toggled if the Task is not "COMPLETED"
                                    data.status !== 'COMPLETED' &&
                                    <>
                                        <img
                                            onClick={() => setShowMenu(!showMenu)}
                                            className="cursor-pointer"
                                            src={menuIcon} alt="Menu"
                                        />

                                        { // controls: Menu
                                            showMenu &&
                                            <div className="
                                absolute top-8 right-0 flex flex-col
                                z-10 bg-primary shadow rounded-lg overflow-hidden
                            ">
                                                <button
                                                    onClick={() => { setShowMenu(false); setEdit(true); }}
                                                    className={menuBtnStyle()}
                                                >Edit</button>
                                                <button
                                                    onClick={() => { setShowMenu(false); remover(); }}
                                                    className={menuBtnStyle('delete')}
                                                >Delete</button>
                                                <button
                                                    onClick={() => setShowMenu(false)}
                                                    className={menuBtnStyle('cancel')}
                                                >Cancel</button>
                                            </div>
                                        }
                                    </>
                                }
                            </div>
                        </>
                    )
            }
        </div>
    )
}