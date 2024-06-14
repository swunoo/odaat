/* Methods and component of TaskBlocks, used by both Tasks.tsx and Projects.tsx */

import { useEffect, useState } from "react"
import menuIcon from '../assets/images/menu.svg'
import { PROJECT_API, ProjectData, TASK_API, TaskData, TaskRequest } from "../conf"
import { dateToString, getValue, menuBtnStyle, numberOrNull } from "../utils"
import { NewButton, NewTaskButton } from "./common"

export function TaskWrapper(
    { project, date, addProject, newProjTitle }
        : { project?: ProjectData, date?: Date, addProject?: () => void, newProjTitle?: string }
) {

    const [tasks, setTasks] = useState<TaskData[]>([])

    const [projectList, setProjectList] = useState<ProjectData[]>([])

    useEffect(() => {
        // API: QUERY ALL TASKS OF A GIVEN PROJECT
        let taskQuery = TASK_API + '/get?'
        if (project) taskQuery += 'projectId=' + project.id
        if (date) taskQuery += '&date=' + dateToString(date)

        fetch(taskQuery, {
            method: 'GET'
        })
            .then(res => res.json())
            .then(data => setTasks(data))
            .catch(err => console.log(err))

    }, [date])

    useEffect(() => {
        // API: GET ALL PROJECTS
        fetch(PROJECT_API + '/getIdName', {
            method: 'GET'
        })
            .then(res => res.json())
            .then(data => setProjectList(data))
            .catch(err => console.log(err))
    }, [])

    // TODO:
    // useEffect(() => {
    //     if(newProjTitle) setProjectList([...projectList, newProjTitle])
    // }, [newProjTitle])

    const initTask: TaskData = {
        id: 0,
        project: project ?? projectList[0],
        description: 'Task Name',
        status: 'PLANNED',
        priority: 'LOW',
        startTime: new Date(),
        durationHr: 1,
        createdAt: new Date(),
        updatedAt: new Date()
    }

    // When "Add Task" button is clicked, init a new Task
    const addTask = () => {
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
            method: 'DELETE'
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
        <div className="">
            <div>
                {
                    tasks.map((t, idx) => (
                        <TaskBlock
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
        { initData: TaskData, projects?: ProjectData[], addProject?: () => void, remover: () => void, taskSetter: (task: TaskData) => void, isTaskPage: boolean }) {

    // Data of the Task
    const [data, setData] = useState<TaskData>(initData)
    // Whether to show Task menu
    const [showMenu, setShowMenu] = useState(false)
    // Whether the Task is a new Task, or an existing one under update
    const [isNew, setIsNew] = useState(data.id === 0)
    // Whether in EDIT mode
    const [edit, setEdit] = useState(isNew)

    useEffect(() => {
        setData(initData)
    }, [initData])

    // When the "Done" checkbox is toggled, status is updated
    const changeCompletion = () => {
        const newStatus = data.status === 'COMPLETED' ? 'PLANNED' : 'COMPLETED'
        // API: UPDATE THE STATUS OF A TASK
        fetch(TASK_API + '/updateStatus/' + data.id + '?status=' + newStatus, {
            method: 'PUT'
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
            startTime: getValue('task-input-date') ? new Date((getValue('task-input-date')) as string) : data.startTime,
            durationHr: (Number)(getValue('task-input-duration'))
        }

        // API: CREATE A TASK
        // API: UPDATE A TASK
        const url = TASK_API + (isNew ? `/add` : `/update/${data.id}`);
        const method = isNew ? 'POST' : 'PUT';
        const callback = isNew ? taskSetter : setData;

        fetch(url, {
            method: method,
            body: JSON.stringify(newData),
            headers: {
                "Content-Type": "application/json"
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
                // Add project name to task
                task.project.name = projects?.find(proj => proj.id === task.project.id)?.name
                callback(task)
            })
            .catch(err => console.log(err))

        setIsNew(false)
        setEdit(false)
    }

    return (
        <div
            key={data.id}
            className={
                "border-b border-light py-3 grid grid-cols-12" +
                (data.status === 'COMPLETED' ? ' bg-light opacity-50 -mx-5 px-5' : '')
            }>

            {// attr: Task IDs
                // Task IDs are shown only in TaskPage
                isTaskPage && <p className="text-gray">{data.id}</p>
            }
            {/* mode: EDIT */
                edit
                    ? (
                        <>

                            {// attr: Project and Date
                                // Projects are shown only in TaskPage, Dates only in ProjectPage
                                isTaskPage

                                    // on TaskPage
                                    ? <div className="col-span-2 flex flex-col gap-2">
                                        <select id="task-input-proj" className="
                                            h-fit mr-5 px-3 py-1 border-r-4 border-light
                                        ">
                                            {projects && projects.map((proj, idx) => (
                                                proj === data.project
                                                    ? <option key={idx} value={proj.id} selected>{proj.name}</option>
                                                    : <option key={idx} value={proj.id}>{proj.name}</option>
                                            ))}
                                        </select>
                                        {addProject && <NewButton label="New Project" clickHandler={addProject} />}
                                    </div>

                                    // on Project Page
                                    : <input id="task-input-date" className="col-span-3 h-fit text-xs bg-transparent pr-3 mt-3" type="date" defaultValue={data.startTime.toString()} />
                            }

                            {/* attr: other attributes are shown in both TaskPage and ProjectPage */}
                            <textarea
                                id="task-input-task"
                                className={
                                    "border px-3 py-1 " + (isTaskPage ? 'border-light col-span-6' : 'border-gray col-span-5 bg-transparent')
                                }
                                defaultValue={data.description}></textarea>
                            <div className="flex items-center h-fit">
                                <input id="task-input-duration" className="
                        text-right px-3 py-1 w-20 h-fit bg-transparent
                    " type="number"
                                    // onKeyDown={numberInputKeyDown} 
                                    defaultValue={data.durationHr ?? 0} />
                                <span>h</span>
                            </div>
                            <div
                                className={"flex flex-col w-fit ml-auto h-fit gap-2 " + (isTaskPage ? 'col-span-2' : 'col-span-3')}
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
                                    ? <span className="col-span-2">
                                        <span className="border border-gray px-3 py-1 text-sm font-medium">
                                            {data.project.name}
                                        </span>
                                    </span>
                                    : <p className="col-span-2 text-xs py-1">{data.startTime.toString()}</p>
                            }

                            {/* attr: other attributes are shown in both TaskPage and ProjectPage */}
                            <span className="col-span-6"
                                dangerouslySetInnerHTML={{ __html: data.description }}
                            ></span>
                            <span className={
                                "text-right" + (isTaskPage ? '' : ' col-span-2')
                            }> {data.durationHr + ' h'} </span>
                            <div
                                className={"flex justify-end relative col-span-2 "
                                    + (data.status !== 'COMPLETED' ? 'self-baseline ' : '')
                                    + (isTaskPage ? "gap-8" : "gap-3")}
                            >
                                <input
                                    onChange={changeCompletion}
                                    className="w-5 cursor-pointer"
                                    type="checkbox"
                                    checked={data.status === 'COMPLETED'}
                                />

                                { // controls: Menu can be toggled if the Task is not "done"
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