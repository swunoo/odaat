import Navbar from "./Navbar";

import menuIcon from '../assets/images/menu.svg'
import calendarIcon from '../assets/images/calendar.svg'

import { FormEvent, useEffect, useState } from "react";
import { dateToString, getValue, menuBtnStyle, MILLIS_A_DAY, numberOrNull } from "../utils";
import { AddButton, NewButton, SvgChevronLeft, SvgChevronRight } from "./common";
import { NewProjectModal } from "./Projects";
import { PROJECT_API, TASK_API } from "../conf";
import { TaskWrapper } from "./TaskWrapper";

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

    // After adding a task, it is updated in the block.
    const changeTask = (idx: number, task: TaskData) => {
        const newTasks = [...tasks]
        newTasks[idx] = task;
        console.log('updated');
        console.log(task);
        console.log(newTasks);
        
        setTasks(newTasks);
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
                <TaskWrapper date={date} />
            </main>
        </div>
    )
}