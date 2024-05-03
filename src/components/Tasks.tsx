import Navbar from "./Navbar";

import menuIcon from '../assets/images/menu.svg'
import calendarIcon from '../assets/images/calendar.svg'

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
            status: 'onhold'
        }, {
            id: '0023',
            project: 'Build React',
            task: 'Write the frontend',
            duration: '0.5',
            status: 'inprog'
        },
    ]

    return (
        <div className="my-10 px-20">
            <nav className="
                flex justify-between px-5 py-3 bg-accent2
            ">
                <div className="flex gap-3 items-center">
                    <img src={calendarIcon} alt="Move to Dates" />
                    <h3 className="
                        text-dark text-xl font-medium
                    ">{date}</h3>
                </div>
                <button className="
                    bg-white px-5 py-1 rounded text-sm shadow-lg
                ">
                    Add Task
                </button>
            </nav>
            <main className="
                bg-white min-h-96
            ">
                {data.map(d => <TaskBlock data={d} />)}
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
function TaskBlock({data}: {data: TaskData}){

    const checkBox = (isDone: boolean) => (
        isDone
        ? <input className="w-5" type="checkbox" checked/>
        : <input className="w-5" type="checkbox"/>
    )

    return (
        <div className="
            border-b border-light px-5 py-3
            grid grid-cols-12
        ">
            <p className="text-gray">
                {data.id}
            </p>
            <p className="col-span-2">
                <span className="border border-gray px-3 py-1 text-sm font-medium">
                    {data.project}
                </span>
            </p>
            <p className="col-span-6">
                {data.task}
            </p>
            <p className="text-right">
                {data.duration + ' h'}
            </p>
            <div className="flex gap-8 col-span-2 justify-end">
                {checkBox(data.status === 'done')}
                <img src={menuIcon} alt="Menu" />
            </div>

        </div>
    )
}