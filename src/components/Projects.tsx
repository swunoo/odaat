import { useState } from "react";
import Navbar from "./Navbar";
import { AddButton } from "./common";


type ProjectData = {
    id: string,
    title: string,
    duration: string,
    completedAt: string | null,
    deadline: string,
    priority: 'low' | 'medium' | 'high'
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
            priority: 'high'
        }, {
            id: '002',
            title: 'Build Scala',
            duration: '15',
            completedAt: null,
            deadline: '2024-05-25',
            priority: 'medium'
        }, {
            id: '005',
            title: 'Build React',
            duration: '1',
            completedAt: '2023-10-15',
            deadline: '2024-08-15',
            priority: 'low'
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
            className="flex justify-between mt-10 px-5 py-3 bg-secondary"
            >
                <h2 className="text-lg text-white capitalize">{status} Projects</h2>
                <div className="flex gap-20">
                    <div className="flex gap-5 text-white">
                        {statusBtn('ongoing')}
                        |
                        {statusBtn('completed')}
                    </div>
                    <AddButton label="New Project" clickHandler={addProject} />
                </div>
            </header>
            <main className="flex flex-wrap gap-10">
                {projects.map(proj => <ProjectBlock data={proj} />)}
            </main>
        </div>
    )
}

function ProjectBlock({data}: {data: ProjectData}){
    return (
        <div className="">

        </div>
    )
}