import Navbar from "./Navbar";

import menuIcon from '../assets/images/menu.svg'
import calendarIcon from '../assets/images/calendar.svg'

import { FormEvent, useState } from "react";
import { menuBtnStyle, numberInputKeyDown } from "../utils";
import { AddButton, getValue, NewButton, SvgChevronLeft, SvgChevronRight } from "./common";
import { NewProjectModal } from "./Projects";

export default function Tasks(){
    return (
        <div className="md:max-h-screen py-10 px-20">
            <Navbar active="tasks"/>
            <TaskContainer/>
        </div>
    )
}

function TaskContainer(){

    // PH
    const date = new Date().toISOString().split('T')[0].replace(/-/g, ' ');
    const data = [
        {
            id: '0015',
            project: 'Learn Scala',
            task: 'Take the 2 hour course',
            duration: '2',
            status: 'done',
            date: date
        }, {
            id: '0020',
            project: 'Build Scala',
            task: 'Build the backend',
            duration: '10',
            status: 'inprog',
            date: date
        }, {
            id: '0023',
            project: 'Build React',
            task: 'Write the frontend',
            duration: '0.5',
            status: 'inprog',
            date: date
        },
    ]
    const mockProj = data.map(d => d.project)

    const [tasks, setTasks] = useState(data)
    const [projects, setProjects] = useState(mockProj)
    const [showNewProj, setShowNewProj] = useState(false)

    const addTask = () => {
        const initTask = {
            id: '',
            project: projects[0],
            task: 'Task Name',
            duration: '1',
            status: 'inprog',
            date: date
        }
        setTasks([...tasks, initTask])
    }

    const removeTask = (idx: number) => {
        // PH: Remove task from database if not new.
        setTasks(tasks.filter((_, i) => i !== idx))
    }

    const addProject = () => {
        setShowNewProj(true)
    }

    const confirmAddingProject = (e: FormEvent) => {
        e.preventDefault();
        const form = e.target as HTMLFormElement;
        const formData = new FormData(form);
        const confirmedProj: any = {}
        formData.forEach((val, key) => {
            confirmedProj[key] = val;
        })

        // PH: Add projects to database
        setProjects([...projects, confirmedProj.title])
        setShowNewProj(false)
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
                    <img src={calendarIcon} alt="Move to Dates" />
                    <h3 className="
                        text-dark text-lg font-medium
                    ">{date}</h3>
                    <div className="flex gap-3">
                        <SvgChevronLeft />
                        <SvgChevronRight />
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

type TaskData = {
    id: string,
    project: string,
    task: string,
    duration: string, 
    status: string,
    date: string
}
export function TaskBlock(
    {initData, projects, addProject, remover, isTaskPage}: 
    {initData: TaskData, projects?: string[], addProject?: ()=>void, remover: ()=>void, isTaskPage: boolean}){

    const [data, setData] = useState(initData)
    const [showMenu, setShowMenu] = useState(false)
    const [isNew, setIsNew] = useState(data.id.length === 0)
    const [edit, setEdit] = useState(isNew)
    
    const changeCompletion = () => {
        const newStatus = data.status === 'done' ? 'inprog' : 'done'
        // PH: Update
        setData({...data, status: newStatus})
    }

    const cancelEdit = () => {
        if(isNew) remover();
        else setEdit(false);
    }

    const confirmEdit = () => {

        const newData = {
            id: data.id,
            project: isTaskPage ? getValue('task-input-proj') : data.project,
            duration: getValue('task-input-duration'),
            task: getValue('task-input-task'),
            status: data.status,
            date: isTaskPage ? data.date : getValue('task-input-date')
        }

        if(isNew){
            // PH: Create
            newData.id = '0010'
        } else {
            // PH: Update
        }

        setData(newData)
        setIsNew(false)
        setEdit(false)
    }

    return (
        <div className={
            "border-b border-light py-3 grid grid-cols-12" + (data.status === 'done' ? ' bg-light opacity-50 -mx-5 px-5' : '')
        }>
            {isTaskPage
            && <p className="text-gray">{data.id}</p>}
            {
                edit
                ? (
                <>
                {
                isTaskPage
                ? <div className="col-span-2 flex flex-col gap-2">
                    <select id="task-input-proj" className="
                        h-fit mr-5 px-3 py-1 border-r-4 border-light
                    ">
                        {projects && projects.map((proj) => (
                                proj === data.project
                                ? <option value={proj} selected>{proj}</option>
                                : <option value={proj}>{proj}</option>
                            ))}
                    </select>
                    {addProject && <NewButton label="New Project" clickHandler={addProject} />}
                </div>
                : <input id="task-input-date" className="col-span-3 h-fit text-xs bg-transparent pr-3 mt-3" type="date" defaultValue={data.date}/>
                }
                <textarea id="task-input-task" className={
                    "border px-3 py-1 " + (isTaskPage ? 'border-light col-span-6' : 'border-gray col-span-5 bg-transparent')
                } defaultValue={data.task}></textarea>
                <div className="flex items-center h-fit">
                    <input id="task-input-duration" className="
                        text-right px-3 py-1 w-20 h-fit bg-transparent
                    " type="number" 
                    // onKeyDown={numberInputKeyDown} 
                    defaultValue={data.duration}/>
                    <span>h</span>
                </div>
                <div className={"flex flex-col w-fit ml-auto h-fit gap-2 " + (isTaskPage ? 'col-span-2' : 'col-span-3')}>
                    <button 
                    onClick={confirmEdit}
                    className={
                        "py-1 rounded bg-accent2 hover:bg-primary "
                        + (isTaskPage ? 'px-5 text-sm' : 'px-1 text-xs')
                    }>Confirm</button>
                    {<button
                    onClick={cancelEdit}
                    className={
                        "py-1 rounded bg-gray text-white hover:bg-dark "
                        + (isTaskPage ? 'px-5 text-sm' : 'px-1 text-xs')
                    }>Cancel</button>}
                </div>
                </>
                ) 
                : (
                <>
                {!isTaskPage &&
                <p className="col-span-2 text-xs self-center">{data.date}</p>
                }
                {isTaskPage &&
                <span className="col-span-2">
                    <span className="border border-gray px-3 py-1 text-sm font-medium">
                        {data.project}
                    </span>
                </span>}
                <span className="col-span-6"> {data.task} </span>
                <span className="text-right"> {data.duration + ' h'} </span>
                <div className={"flex justify-end relative " + (isTaskPage ? "gap-8 col-span-2" : "gap-3 col-span-3")}>
                    <input
                    onChange={changeCompletion} 
                    className="w-5 cursor-pointer"
                    type="checkbox"
                    checked={data.status === 'done'}/>
                    {data.status !== 'done' && 
                    <>
                    <img 
                        onClick={() => setShowMenu(!showMenu)} 
                        className="cursor-pointer" 
                        src={menuIcon} alt="Menu" />
                    { showMenu &&
                    <div className="
                        absolute top-8 right-0 flex flex-col
                        z-10 bg-primary shadow rounded-lg overflow-hidden
                    ">
                        {/* {
                            isTaskPage &&
                            <>
                                <button className={menuBtnStyle()}>Postpone 1 day</button>
                                <button className={menuBtnStyle()}>Put on Hold</button>
                            </>
                        } */}


                        <button onClick={()=>{setShowMenu(false); setEdit(true);}} className={menuBtnStyle()}>Edit</button>
                        <button onClick={()=>{setShowMenu(false); remover();}} className={menuBtnStyle('delete')}>Delete</button>
                        <button onClick={()=>setShowMenu(false)} className={menuBtnStyle('cancel')}>Cancel</button>
                    </div>
                    }
                    </>}
                    
                </div>
                </>
                )
            }

        </div>
    )
}