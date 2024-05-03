import Navbar from "./Navbar";

import menuIcon from '../assets/images/menu.svg'
import calendarIcon from '../assets/images/calendar.svg'
import plusIcon from '../assets/images/plus.svg'
import { useState } from "react";
import { numberInputKeyDown } from "../utils";
import { AddButton } from "./common";

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
    const date = new Date().toISOString().split('T')[0];
    const data = [
        {
            id: '0015',
            project: 'Learn Scala',
            task: 'Take the 2 hour course',
            duration: '2',
            status: 'done'
        }, {
            id: '0020',
            project: 'Build Scala',
            task: 'Build the backend',
            duration: '10',
            status: 'inprog'
        }, {
            id: '0023',
            project: 'Build React',
            task: 'Write the frontend',
            duration: '0.5',
            status: 'inprog'
        },
    ]
    const projects = data.map(d => d.project)

    const initTask = {
        id: '',
        project: projects[0],
        task: 'Task Name',
        duration: '1',
        status: 'inprog'
    }

    const [tasks, setTasks] = useState(data)

    const addTask = () => {
        setTasks([...tasks, initTask])
    }

    const removeTask = (idx: number) => {
        // Remove task from database if not new.
        setTasks(tasks.filter((_, i) => i !== idx))
    }

    return (
        <div className="my-10 mx-20 shadow">
            <nav className="
                flex justify-between px-5 py-3 bg-accent2
            ">
                <div className="flex gap-3 items-center">
                    <img src={calendarIcon} alt="Move to Dates" />
                    <h3 className="
                        text-dark text-xl font-medium
                    ">{date}</h3>
                </div>
                <AddButton label="New Task" clickHandler={addTask} />
            </nav>
            <main className="
                bg-white min-h-96
            ">
                {tasks.map((t, idx) => <TaskBlock data={t} projects={projects} remover={()=>removeTask(idx)} />)}
            </main>
        </div>
    )
}

type TaskData = {
    id: string,
    project: string,
    task: string,
    duration: string, 
    status: string
}
function TaskBlock({data, projects, remover}: {data: TaskData, projects: string[], remover: ()=>void}){

    const menuBtnStyle = (option?: string) => {

        let hover = 'hover:bg-accent2';
        if(option === 'delete') hover = 'hover:bg-red-500 hover:text-white'
        else if(option === 'cancel') hover = 'hover:bg-gray hover:text-white'

        return 'cursor-pointer border-b border-white px-5 py-1 ' + hover;
    } 

    const checkBox = (isDone: boolean) => (
        isDone
        ? <input className="w-5" type="checkbox" checked/>
        : <input className="w-5" type="checkbox"/>
    )

    const isNew = data.id.length === 0
    const [showMenu, setShowMenu] = useState(false)
    const [edit, setEdit] = useState(isNew)
    const [isComplete, setIsComplete] = useState(data.status === 'done')

    const changeCompletion = () => {
        setIsComplete(!isComplete)
    }

    const cancelEdit = () => {
        if(isNew) remover();
        else setEdit(false);
    }

    return (
        <div className={
            "border-b border-light px-5 py-3 grid grid-cols-12" + (isComplete ? ' bg-light opacity-50' : '')
        }>
            <p className="text-gray">
                {data.id}
            </p>
            {
                edit
                ? (
                <>
                <div className="col-span-2 flex flex-col gap-2">
                    <select className="
                        h-fit mr-5 px-3 py-1 border-r-4 border-light
                    ">
                        {projects.map((proj) => (
                                proj === data.project
                                ? <option value={proj} selected>{proj}</option>
                                : <option value={proj}>{proj}</option>
                            ))}
                    </select>
                    <button className="flex gap-2 items-center w-fit hover:underline text-xs uppercase">
                        <img src={plusIcon} alt="New Project" />
                        <span>New Project</span>
                    </button>
                </div>
                <textarea className="
                    col-span-6 border-light border px-3 py-1
                ">{data.task}</textarea>
                <div className="flex items-center h-fit">
                    <input className="
                        text-right px-3 py-1 w-20 h-fit
                    " type="number" onKeyDown={numberInputKeyDown} placeholder={data.duration}/>
                    <span>h</span>
                </div>
                <div className="flex flex-col w-fit ml-auto h-fit col-span-2 gap-2 text-sm">
                    <button className="
                        px-5 py-1 rounded bg-accent2 hover:bg-primary
                    ">Confirm</button>
                    {<button
                    onClick={cancelEdit}
                    className="
                        px-5 py-1 rounded bg-gray text-white hover:bg-dark
                    ">Cancel</button>}
                </div>
                </>
                ) 
                : (
                <>
                <span className="col-span-2">
                    <span className="border border-gray px-3 py-1 text-sm font-medium">
                        {data.project}
                    </span>
                </span>
                <span className="col-span-6"> {data.task} </span>
                <span className="text-right"> {data.duration + ' h'} </span>
                <div className="flex gap-8 col-span-2 justify-end relative">
                    <input
                    onChange={changeCompletion} 
                    className="w-5 cursor-pointer"
                    type="checkbox"
                    checked={isComplete}/>
                    {!isComplete && 
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
                        <button className={menuBtnStyle()}>Postpone 1 day</button>
                        <button className={menuBtnStyle()}>Put on Hold</button>
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