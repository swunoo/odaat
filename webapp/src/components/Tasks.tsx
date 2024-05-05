import Navbar from "./Navbar";

import menuIcon from '../assets/images/menu.svg'
import calendarIcon from '../assets/images/calendar.svg'

import { FormEvent, useEffect, useState } from "react";
import { dateToString, getValue, menuBtnStyle, MILLIS_A_DAY, numberInputKeyDown, numberOrNull } from "../utils";
import { AddButton, NewButton, SvgChevronLeft, SvgChevronRight } from "./common";
import { NewProjectModal } from "./Projects";
import { PROJECT_API, TASK_API } from "../conf";

export type TaskData = {
    id: number,
    project: string,
    task: string,
    duration: null | number, 
    status: string,
    date: string
}

/* Tasks Page */
export default function Tasks(){

    return (
        <div className="md:max-h-screen py-10 px-20">
            <Navbar active="tasks"/>
            <TaskContainer />
        </div>
    )
}

/* Container for the header and each Task */
function TaskContainer(){

    const mockTasks = [
        {
            id: '0015',
            project: 'Learn Scala',
            task: 'Take the 2 hour course',
            duration: '2',
            status: 'done',
            date: dateToString(new Date())
        }, {
            id: '0020',
            project: 'Build Scala',
            task: 'Build the backend',
            duration: '10',
            status: 'inprog',
            date: dateToString(new Date())
        }, {
            id: '0023',
            project: 'Build React',
            task: 'Write the frontend',
            duration: '0.5',
            status: 'inprog',
            date: dateToString(new Date())
        },
    ]
    const mockProj = mockTasks.map(d => d.project)

    // Date of the Tasks to display
    const [date, setDate] = useState(new Date())
    // All Tasks for a given date
    const [tasks, setTasks] = useState<TaskData[]>([])
    // All Projects
    const [projects, setProjects] = useState<string[]>([])
    // Whether to show the NewProject modal
    const [showNewProj, setShowNewProj] = useState(false)

    useEffect(() => {
        // API: GET ALL TASKS OF A GIVEN DATE
        fetch(TASK_API + '/get?date=' + dateToString(date), {
            method: 'GET'
        })
            .then(res => res.json())
            .then(data => setTasks(data))
            .catch(err => console.log(err))
        
    }, [date])
    useEffect(() => {
        // API: GET ALL PROJECTS
        fetch(PROJECT_API + '/getTitles', {
            method: 'GET'
        })
            .then(res => res.json())
            .then(data => setProjects(data))
            .catch(err => console.log(err))
    }, [])

    // When "Add Task" button is clicked, init a new Task
    const addTask = () => {
        const initTask = {
            id: 0,
            project: projects[0],
            task: 'Task Name',
            duration: 1,
            status: 'inprog',
            date: dateToString(date)
        }
        setTasks([...tasks, initTask])
    }

    // When "Delete" button is clicked on a Task, remove it
    const removeTask = (idx: number) => {
        // API: REMOVE A TASK
        fetch(TASK_API + '/delete/' + tasks[idx]['id'], {
            method: 'DELETE'
        })
            .then(res => {
                if(res.status === 200) {
                    setTasks(tasks.filter((_, i) => i !== idx))
                }
                else {
                    console.log(res.text());
                    alert("Unable to delete")
                }
            }).catch(err => console.log(err))
    }

    // When "Add Project" button is clicked, show NewProject modal
    const addProject = () => {
        setShowNewProj(true)
    }

    // When "Confirm" button from NewProject modal is clicked, data is saved
    const confirmAddingProject = (e: FormEvent) => {
        e.preventDefault();
        const form = e.target as HTMLFormElement;
        const formData = new FormData(form);
        const confirmedProj: any = {}
        formData.forEach((val, key) => {
            confirmedProj[key] = val;
        })

        // API: CREATE A PROJECT
        fetch(PROJECT_API + '/add', {
            method: 'POST',
            body: JSON.stringify(confirmedProj),
            
        })
            .then(res => {
                if(res.status === 200) {
                    // 3. Update the UI
                    setProjects([...projects, confirmedProj])
                }
                else {
                    console.log(res.text());
                    alert("Unable to create. Title must be unique.")
                }
            }).catch(err => console.log(err))
    }

    // When the left chevron is clicked, decrement the date
    const prevDay = () => {
        setDate(new Date(date.getTime() - MILLIS_A_DAY))
    }

    // When the right chevron is clicked, increment the date
    const nextDay = () => {
        setDate(new Date(date.getTime() + MILLIS_A_DAY))
    }

    return (
        <div className="my-10 mx-20 shadow">
            {showNewProj && 
                <NewProjectModal
                    data={null}
                    cancelHandler={() => setShowNewProj(false)}
                    confirmHandler={confirmAddingProject}
                />
            }

            <nav className="
                flex justify-between px-5 py-3 bg-accent2
            ">
                <div className="flex gap-8 items-center">
                    <img src={calendarIcon} alt="Change Date" />
                    <h3 className="
                        text-dark text-lg font-medium
                    ">{dateToString(date).replace(/-/g, ' ')}</h3>
                    <div className="flex gap-3">
                        <SvgChevronLeft clickHandler={()=>prevDay()}/>
                        <SvgChevronRight clickHandler={()=>nextDay()}/>
                    </div>
                </div>
                <AddButton label="New Task" clickHandler={addTask} />
            </nav>
            <main className="
                bg-white min-h-96 px-5 
            ">
                {tasks.map((t, idx) => (
                    <TaskBlock
                        initData={t}
                        projects={projects} 
                        addProject={addProject} 
                        remover={()=>removeTask(idx)} 
                        isTaskPage={true}
                    />
                ))}
            </main>
        </div>
    )
}

/* Component of Each Task, used in both the TaskPage and ProjectPage */
export function TaskBlock(
    {initData, projects, addProject, remover, isTaskPage}: 
    {initData: TaskData, projects?: string[], addProject?: ()=>void, remover: ()=>void, isTaskPage: boolean}){

    // Data of the Task
    const [data, setData] = useState<TaskData>(initData)
    // Whether to show Task menu
    const [showMenu, setShowMenu] = useState(false)
    // Whether the Task is a new Task, or an existing one under update
    const [isNew, setIsNew] = useState(data.id === 0)
    // Whether in EDIT mode
    const [edit, setEdit] = useState(isNew)
    
    // When the "Done" checkbox is toggled, status is updated
    const changeCompletion = () => {
        const newStatus = data.status === 'done' ? 'inprog' : 'done'
        // API: UPDATE THE STATUS OF A TASK
        fetch(TASK_API + '/updateStatus/' + data.id + '?status=' + newStatus, {
            method: 'PUT'
        })
        .then(res => {
            if(res.status === 200) {
                setData({...data, status: newStatus})
            }
            else {
                console.log(res.text());
                alert("Unable to update")
            }
        }).catch(err => console.log(err))
    }

    // When the "Cancel" button on EDIT mode is clicked, data is not saved.
    const cancelEdit = () => {
        if(isNew) remover();
        else setEdit(false);
    }

    // When the "Confirm" button on EDIT mode is clicked, data is saved.
    const confirmEdit = () => {

        const newData = {
            id: data.id,
            project: isTaskPage ? getValue('task-input-proj') : data.project,
            duration: numberOrNull(getValue('task-input-duration')),
            task: getValue('task-input-task'),
            status: data.status,
            date: isTaskPage ? data.date : getValue('task-input-date')
        }

        if(isNew){
            // API: CREATE A TASK
            fetch(TASK_API + '/add', {
                method: 'POST',
                body: JSON.stringify(newData),
                headers: {
                    "Content-Type": "application/json"
                }
            })
            .then(res => {
                if(res.status === 200) {
                    return res.json()
                }
                else {
                    console.log(res.text());
                    throw new Error()
                }
            }).then(data => {
                newData.id = data.id
                setData(newData)
            })
            .catch(err => console.log(err))
            
        } else {
            // API: UPDATE A TASK
            fetch(TASK_API + '/update/' + data.id, {
                method: 'PUT',
                body: JSON.stringify(newData),
                headers: {
                    "Content-Type": "application/json"
                }
            })
            .then(res => {
                if(res.status === 200) {
                    setData(newData)

                }
                else {
                    console.log(res.text());
                    alert("Unable to update")
                }
            }).catch(err => console.log(err))
        }

        setIsNew(false)
        setEdit(false)
    }

    return (
        <div 
            key={data.id}
            className={
                "border-b border-light py-3 grid grid-cols-12" + 
                (data.status === 'done' ? ' bg-light opacity-50 -mx-5 px-5' : '')
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
                                    ? <option key={idx} value={proj} selected>{proj}</option>
                                    : <option key={idx} value={proj}>{proj}</option>
                                ))}
                        </select>
                        {addProject && <NewButton label="New Project" clickHandler={addProject} />}
                    </div>

                    // on Project Page
                    : <input id="task-input-date" className="col-span-3 h-fit text-xs bg-transparent pr-3 mt-3" type="date" defaultValue={data.date}/>
                }

                {/* attr: other attributes are shown in both TaskPage and ProjectPage */}
                <textarea 
                    id="task-input-task" 
                    className={
                        "border px-3 py-1 " + (isTaskPage ? 'border-light col-span-6' : 'border-gray col-span-5 bg-transparent')
                    } 
                    defaultValue={data.task}></textarea>
                <div className="flex items-center h-fit">
                    <input id="task-input-duration" className="
                        text-right px-3 py-1 w-20 h-fit bg-transparent
                    " type="number" 
                    // onKeyDown={numberInputKeyDown} 
                    defaultValue={data.duration ?? 0}/>
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
                            {data.project}
                        </span>
                    </span>
                    : <p className="col-span-2 text-xs self-center">{data.date}</p>
                }

                {/* attr: other attributes are shown in both TaskPage and ProjectPage */}
                <span className="col-span-6"> {data.task} </span>
                <span className={
                    "text-right" + (isTaskPage ? '' : ' col-span-2')
                    }> {data.duration + ' h'} </span>
                <div 
                    className={"flex justify-end relative col-span-2 " + (isTaskPage ? "gap-8" : "gap-3")}
                >
                    <input
                        onChange={changeCompletion} 
                        className="w-5 cursor-pointer"
                        type="checkbox"
                        checked={data.status === 'done'}
                    />

                    { // controls: Menu can be toggled if the Task is not "done"
                        data.status !== 'done' && 
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
                                {/* { // Show extra options on TaskPage
                                    isTaskPage &&
                                    <>
                                        <button className={menuBtnStyle()}>Postpone 1 day</button>
                                        <button className={menuBtnStyle()}>Put on Hold</button>
                                    </>
                                } */}

                                <button 
                                    onClick={()=>{setShowMenu(false); setEdit(true);}} 
                                    className={menuBtnStyle()}
                                >Edit</button>
                                <button 
                                    onClick={()=>{setShowMenu(false); remover();}} 
                                    className={menuBtnStyle('delete')}
                                >Delete</button>
                                <button 
                                    onClick={()=>setShowMenu(false)} 
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