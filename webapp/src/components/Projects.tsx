import { useState } from "react";
import Navbar from "./Navbar";
import { AddButton } from "./common";

import arrowIcon from '../assets/images/arrow.svg'
import { TaskBlock } from "./Tasks";


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

    }

    return (
        <div className="md:max-h-screen py-10 px-20">
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
                {projects.map(proj => <ProjectBlock data={proj} />)}
            </main>
        </div>
    )
}

function ProjectBlock({data}: {data: ProjectData}){

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
            <div className="bg-white px-10 py-5 flex justify-between items-center">
                <h3 className="font-bold text-xl ">
                    {data.title}
                </h3>
                {
                data.completedAt
                ? <p className="text-gray text-xs">
                    {data.completedAt}
                </p> 
                : <p className="text-red-500 text-xs">
                    {data.deadline}
                </p>
                }
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
                    <AddButton label="New Project" clickHandler={addTask} />
                </div>
                </>}

            </div>
        </div>
    )
}