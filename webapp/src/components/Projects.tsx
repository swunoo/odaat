import { FormEvent, useEffect, useState } from "react";
import Navbar from "./Navbar";
import { AddButton } from "./common";

import arrowIcon from '../assets/images/arrow.svg';
import menuIcon from '../assets/images/menu.svg';

import { dateToString, menuBtnStyle, numberInputKeyDown, numberOrNull } from "../utils";
import { TaskBlock, TaskData } from "./Tasks";
import { PROJECT_API, TASK_API } from "../conf";


type ProjectData = {
    id: string,
    title: string,
    duration: string,
    completedAt: string | null,
    deadline: string,
    priority: 'low' | 'medium' | 'high',
    description: string
}

/* Projects Page */
export default function Projects(){

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

    // Data of all projects
    const [projects, setProjects] = useState<ProjectData[]>([])
    // Whether to show the NewProject modal
    const [showNewProj, setShowNewProj] = useState(false)
    // Index of the currently-being-edited project
    const [editedProject, setEditedProject] = useState<number | null>(null)

    // Fetch all projects
    useEffect(() => {
        // API: QUERY ALL PROJECTS
        fetch(PROJECT_API + '/getAll', {
            method: 'GET'
        })
            .then(res => res.json())
            .then(data => setProjects(data))
            .catch(err => console.log(err))
    }, [])

    // When "Add Project" button is clicked, show NewProject modal
    const addProject = () => {
        setShowNewProj(true)
    }

    // When "Delete" button is clicked, delete the project
    const removeProject = (idx: number) => {
        // API: DELETE PROJECT OF A GIVEN TITLE
        fetch(PROJECT_API + '/delete/' + projects[idx]['title'], {
            method: 'DELETE'
        })
            .then(res => {
                if(res.status === 200) {
                    setProjects(projects.filter((_, i) => i !== idx))
                }
                else {
                    console.log(res.text());
                    alert("Unable to delete")
                }
            }).catch(err => console.log(err))
    }

    // When "Edit" button is clicked, NewProject modal is open with that project's data
    const editProject = (idx: number) => {
        setEditedProject(idx)
        setShowNewProj(true)
    }

    // When "Confirm" button from NewProject modal is clicked, data is saved
    const confirmAddingProject = (e: FormEvent) => {
        e.preventDefault();

        // 1. Build a Project with data from NewProject modal's form
        const confirmedProj: any = {}
        const form = e.target as HTMLFormElement;
        const formData = new FormData(form);
        formData.forEach((val, key) => {
            confirmedProj[key] = val;
        })

        // Cast the duration into a double value
        confirmedProj['duration'] = numberOrNull(confirmedProj['duration'])

        // Append the completedat attribute

        // 2A. Send a PUT request for project updates
        if(editedProject !== null){
            // API: UPDATE PROJECT OF A GIVEN TITLE
            const currProject = projects[editedProject];
            confirmedProj.title = currProject.title // title is immutable
            confirmedProj.completedAt = currProject.completedAt ?? null

            fetch(PROJECT_API + '/update', {
                method: 'PUT',
                body: JSON.stringify(confirmedProj),
                headers: {
                    "Content-Type": "application/json"
                }
            })
                .then(res => {
                    if(res.status === 200) {
                        projects[editedProject] = confirmedProj

                        // 3. Update the UI
                        setProjects(projects)
                        setEditedProject(null)
                    }
                    else {
                        console.log(res.text());
                        alert("Unable to update")
                    }
                }).catch(err => console.log(err))

        // 2B. Send a POST request for project creations
        } else {
            confirmedProj.completedAt = null

            // API: CREATE A PROJECT
            fetch(PROJECT_API + '/add', {
                method: 'POST',
                body: JSON.stringify(confirmedProj),
                headers: {
                    "Content-Type": "application/json"
                }
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

        // 4. Close the modal
        setShowNewProj(false)
    }

    // When "Cancel" button from NewProject modal is clicked, modal is reset
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
                <h2 className="text-lg capitalize">Projects</h2>
                <AddButton label="New Project" clickHandler={addProject} />
            </header>
            <main className="grid grid-cols-2 gap-10 py-5 h-100 overflow-scroll">
                {projects.map((proj, i) => (
                    <ProjectBlock
                        data={proj}
                        editor={()=>editProject(i)}
                        remover={()=>removeProject(i)} 
                    />
                ))}
            </main>
        </div>
    )
}

/* Container of Each Project */
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

    // All Tasks belonging to this Project
    const [tasks, setTasks] = useState<TaskData[]>([])
    // Expand or collapse Tasks data
    const [showTasks, setShowTasks] = useState(false)
    // Whether to show the Project menu
    const [showMenu, setShowMenu] = useState(false)

    useEffect(() => {
        // API: QUERY ALL TASKS OF A GIVEN PROJECT
        fetch(TASK_API + '/get?project=' + data.title, {
            method: 'GET'
        })
            .then(res => res.json())
            .then(data => setTasks(data))
            .catch(err => console.log(err))
        
    }, [])

    const initTask = {
        id: 0,
        project: data.title,
        task: 'Task Name',
        duration: 1,
        status: 'inprog',
        date: dateToString(new Date())
    }

    // When "Add Task" button is clicked, init a new Task
    const addTask = () => {
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

    // When "Delete" button from a Task is clicked, delete it
    const removeTask = (idx: number) => {

        const taskId = tasks[idx]['id']

        // For cancelling "Create New", it is not yet saved in the database
        if(taskId === 0){ 
            setTasks(tasks.filter((_, i) => i !== idx))
            return;
        }

        // API: DELETE A TASK
        fetch(TASK_API + '/delete/' + taskId, {
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

    return (
        <div key={data.title}>
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
                        <button 
                            onClick={()=>{setShowMenu(false); editor();}}
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
                            <TaskBlock 
                                initData={t} 
                                remover={()=>removeTask(idx)}
                                taskSetter={(task)=>changeTask(idx, task)}
                                isTaskPage={false}
                            />
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

/* Modal to CREATE/UPDATE Projects */
export function NewProjectModal(
    {data, confirmHandler, cancelHandler}:
    {data: ProjectData | null,confirmHandler: (e: FormEvent)=>void, cancelHandler: ()=>void}
){

    // Styles
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
                    : <input
                        className={input} type="text" name="title" 
                    />
                }

                <label className={label}>Time Est.</label>
                <input 
                    className={input} 
                    // onKeyDown={numberInputKeyDown}  
                    type="number" step={0.1} 
                    name="duration" 
                    defaultValue={data?data.duration:''}
                />

                <label className={label}>Deadline</label>
                <input 
                    className={input} 
                    type="date" 
                    name="deadline"  
                    defaultValue={data?data.deadline:''}
                />

                <label className={label}>Priority</label>
                <select 
                    className={input + " capitalize"} 
                    name="priority"
                >   {
                        ['low', 'medium', 'high'].map((p, idx) => (
                            <option key={idx} value={p}>{p}</option>
                        ))
                    }
                </select>

                <label className={label}>Description</label>
                <textarea 
                    rows={6} 
                    name="description" 
                    className={input + ' col-span-4'} 
                    defaultValue={data?data.description:''}></textarea>

                <div className="col-span-4 flex justify-end gap-3 mt-10">
                    <button 
                        type="submit" 
                        className={button + " bg-secondary"}
                    >Confirm</button>
                    <button 
                        type="button" 
                        onClick={cancelHandler} 
                        className={button + " bg-gray"}
                    >Cancel</button>
                </div>

            </form>
        </div>
    )
}