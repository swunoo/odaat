import { FormEvent, FormEventHandler, useState } from "react";
import Navbar from "./Navbar";
import { AddButton, NewButton } from "./common";

import arrowIcon from '../assets/images/arrow.svg'
import menuIcon from '../assets/images/menu.svg'

import { TaskBlock } from "./Tasks";
import { menuBtnStyle, numberInputKeyDown } from "../utils";


type ProjectData = {
    id: string,
    title: string,
    duration: string,
    completedAt: string | null,
    deadline: string,
    priority: 'low' | 'medium' | 'high',
    description: string
}

export default function Projects(){

    // PH
    const mockData: ProjectData[] = [
        {
            id: '001',
            title: 'Learn Scala',
            duration: '10',
            completedAt: null,
            deadline: '2024-05-15',
            priority: 'high',
            description: 'Lorem ipsum, dolor sit amet consectetur adipisicing elit. Explicabo quis officia corporis ullam repudiandae voluptatibus nobis quam quo architecto amet, minus sint aliquid similique eius ipsum vero cumque cum dolore illo veritatis? Eos qui rerum veritatis voluptas asperiores, iste ullam vero optio fugiat vitae iure magnam ratione delectus ipsum at nulla quasi distinctio modi ducimus, dolore libero corporis suscipit animi ut.'
        }, {
            id: '002',
            title: 'Build Scala',
            duration: '15',
            completedAt: null,
            deadline: '2024-05-25',
            priority: 'medium',
            description: 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Id inventore quos iure non blanditiis natus possimus eveniet a aperiam. Officiis!'
        }, {
            id: '005',
            title: 'Build React',
            duration: '1',
            completedAt: '2023-10-15',
            deadline: '2024-08-15',
            priority: 'low',
            description: 'Lorem ipsum, dolor sit amet consectetur adipisicing elit. Maiores, ipsam!'
        }
    ]

    const [projects, setProjects] = useState(mockData)
    const [status, setStatus] = useState('ongoing')
    const [showNewProj, setShowNewProj] = useState(false)
    const [editedProject, setEditedProject] = useState<number | null>(null)


    const statusBtn = (s: string) => (
        (s === status)
        ? <button className="uppercase text-sm underline cursor-default">{s}</button>
        : <button
        onClick={() => setStatus(s)}
        className="uppercase text-sm">
            {s}
        </button>
    )

    const addProject = () => {
        setShowNewProj(true)
    }

    const removeProject = (idx: number) => {
        // PH: Remove task from database if not new.
        setProjects(projects.filter((_, i) => i !== idx))
    }

    const editProject = (idx: number) => {
        console.log(idx);
        
        setEditedProject(idx)
        
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
        if(editedProject !== null){
            confirmedProj.title = projects[editedProject]['title']
            projects[editedProject] = confirmedProj
            setEditedProject(null)
            setProjects(projects)
        } else {
            setProjects([...projects, confirmedProj])
        }
        setShowNewProj(false)
    }

    const closeNewProjectModal = () => {
        if(editedProject) setEditedProject(null)
        setShowNewProj(false)
    }

    return (
        <div className="md:max-h-screen py-10 px-20">
            
            {showNewProj && 
                <NewProjectModal
                    data={editedProject !== null ? projects[editedProject] : null}
                    cancelHandler={closeNewProjectModal}
                    confirmHandler={confirmAddingProject}
                />
            }

            <Navbar active="projects"/>
            <header
            className="flex justify-between mt-10 px-5 py-3 bg-accent2"
            >
                <h2 className="text-lg capitalize">{status} Projects</h2>
                <div className="flex gap-20">
                    <div className="flex gap-5">
                        {statusBtn('ongoing')}
                        |
                        {statusBtn('completed')}
                    </div>
                    <AddButton label="New Project" clickHandler={addProject} />
                </div>
            </header>
            <main className="grid grid-cols-2 gap-10 py-5 h-100 overflow-scroll">
                {projects.map((proj, i) => (
                    <ProjectBlock data={proj} editor={()=>editProject(i)} remover={()=>removeProject(i)} />
                ))}
            </main>
        </div>
    )
}

function ProjectBlock(
    {data, editor, remover}:
    {data: ProjectData, editor: ()=>void, remover: ()=>void}
){

    // PH 
    const mockTasks = [
        {
            id: '0015',
            project: data.title,
            task: 'Take the 2 hour course',
            duration: '2',
            status: 'done',
            date: '2023-10-15'
        }, {
            id: '0020',
            project: data.title,
            task: 'Build the backend',
            duration: '10',
            status: 'inprog',
            date: '2024-6-20'
        }, {
            id: '0023',
            project: data.title,
            task: 'Write the frontend',
            duration: '0.5',
            status: 'inprog',
            date: '2024-1-1'
        },
    ]

    const [showTasks, setShowTasks] = useState(false)
    const [tasks, setTasks] = useState(mockTasks)
    const [showMenu, setShowMenu] = useState(false)

    const initTask = {
        id: '',
        project: data.title,
        task: 'Task Name',
        duration: '1',
        status: 'inprog',
        date: '2024-05-03'
    }

    const addTask = () => {
        setTasks([...tasks, initTask])
    }

    const removeTask = (idx: number) => {
        // PH: Remove task from database if not new.
        setTasks(tasks.filter((_, i) => i !== idx))
    }

    return (
        <div>
            <div className="bg-white px-10 py-5 relative">


                    <img 
                    onClick={() => setShowMenu(!showMenu)} 
                    className="cursor-pointer ml-auto" 
                    src={menuIcon} alt="Menu" />

                    { showMenu &&
                    <div className="
                        absolute top-14 right-5 px-3 py-1 flex flex-col
                        z-10 bg-primary shadow rounded-lg overflow-hidden
                    ">
                        <button onClick={()=>{setShowMenu(false); editor();}} className={menuBtnStyle()}>Edit</button>
                        <button onClick={()=>{setShowMenu(false); remover();}} className={menuBtnStyle('delete')}>Delete</button>
                        <button onClick={()=>setShowMenu(false)} className={menuBtnStyle('cancel')}>Cancel</button>
                    </div>
                    }

                <h3 className="font-bold text-xl ">
                    {data.title}
                </h3>
                <div className="flex justify-between text-xs">
                    <p>{data.duration ? data.duration : 0} h</p>
                    {
                    data.completedAt
                    ? <p className="text-gray">
                        {data.completedAt}
                    </p> 
                    : <p className="text-red-500">
                        {data.deadline}
                    </p>
                    }
                </div>
            </div>
            <div className="bg-light px-10">
                <p className="py-5">{data.description}</p>
                <hr />
                <button
                onClick={() => setShowTasks(!showTasks)}
                className="flex gap-5 py-5">
                    <img
                    
                    className={
                        showTasks ? 'rotate-90' : ''
                    } src={arrowIcon} alt="More Tasks" />
                    <span>Tasks</span>
                </button>

                {
                showTasks &&
                <>
                <div className="">
                    {
                        tasks.map((t, idx) => (
                            <TaskBlock data={t} remover={()=>removeTask(idx)} displayProjs={false}/>
                        ))
                    }
                </div>

                <div className="flex justify-end py-10">
                    <AddButton label="New Task" clickHandler={addTask} />
                </div>
                </>}

            </div>
        </div>
    )
}

export function NewProjectModal(
    {data, confirmHandler, cancelHandler}:
    {data: ProjectData | null,confirmHandler: (e: FormEvent)=>void, cancelHandler: ()=>void}
){

    const input = "col-span-3 px-2 py-1 border border-light"
    const label = "font-medium uppercase text-sm"
    const button = "rounded-lg text-white px-5 py-2 text-sm uppercase font-medium"

    return (
        <div className="
            absolute top-0 left-0 w-screen min-h-screen
            bg-[#000a] backdrop-blur z-20
        ">
            <form onSubmit={confirmHandler} className="
                grid grid-cols-4 items-center gap-x-8 gap-y-3
                w-fit m-auto my-5 2xl:my-10
                bg-white p-10 rounded-lg 
            ">
                <label className={label}>Title</label>
                {
                    data
                    ? <input className={input + ' border-none'} type="text" name="title" value={data.title} disabled/>
                    : <input className={input} type="text" name="title" />
                }
                <label className={label}>Time Est.</label>
                <input className={input} onKeyDown={numberInputKeyDown}  type="number" step={0.1} name="duration" placeholder={data?data.duration:''}/>
                <label className={label}>Deadline</label>
                <input className={input} type="date" name="deadline"  placeholder={data?data.deadline:''}/>
                <label className={label}>Priority</label>
                <select className={input + " capitalize"} name="priority">
                    {
                        ['low', 'medium', 'high'].map(p => (
                            <option value={p}>{p}</option>
                        ))
                    }
                </select>
                <label className={label}>Description</label>
                <textarea rows={6} name="description" className={input + ' col-span-4'} >{data?data.description:''}</textarea>

                <div className="col-span-4 flex justify-end gap-3 mt-10">
                    <button type="submit" className={button + " bg-secondary"}>Confirm</button>
                    <button type="button" onClick={cancelHandler} className={button + " bg-gray"}>Cancel</button>
                </div>
            </form>
        </div>
    )
}