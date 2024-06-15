import { FormEvent, useEffect, useState } from "react";
import Navbar from "./Navbar";
import { NewProjButton } from "./common";

import arrowIcon from '../assets/images/arrow.svg';
import menuIcon from '../assets/images/menu.svg';

import { PROJECT_API, ProjectData } from "../conf";
import { menuBtnStyle, numberOrNull } from "../utils";
import { TaskWrapper } from "./TaskWrapper";

/* Projects Page */
export default function Projects() {

    // Data of all projects
    const [projects, setProjects] = useState<ProjectData[]>([])
    // Whether to show the NewProject modal
    const [showNewProj, setShowNewProj] = useState(false)
    // Index of the currently-being-edited project
    const [editedProject, setEditedProject] = useState<number | null>(null)

    // Fetch all projects
    useEffect(() => {
        // API: QUERY ALL PROJECTS
        fetch(PROJECT_API + '/get', {
            method: 'GET'
        })
            .then(res => res.json())
            .then(data => {
                setProjects(data)
            })
            .catch(err => console.log(err))
    }, [])

    // When "Add Project" button is clicked, show NewProject modal
    const addProject = () => {
        setShowNewProj(true)
    }

    // When "Delete" button is clicked, delete the project
    const removeProject = (idx: number) => {
        // API: DELETE PROJECT OF A GIVEN TITLE
        fetch(PROJECT_API + '/delete/' + projects[idx]['id'], {
            method: 'DELETE'
        })
            .then(res => {
                if (res.ok) {
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

    // When "Cancel" button from NewProject modal is clicked, modal is reset
    const closeNewProjectModal = () => {
        if (editedProject !== null) setEditedProject(null)
        setShowNewProj(false)
    }

    // Re-render projects after CREATE/UPDATE
    const updateProjectData = (proj: ProjectData) => {

        const newProjects = [...projects]

        if (editedProject !== null) {

            newProjects[editedProject] = proj
            setEditedProject(null)

        } else {

            newProjects.push(proj)

        }

        // 3. Update the UI
        setProjects(newProjects)
        setShowNewProj(false)
    }

    return (
        <div className="md:max-h-screen py-10 px-20">

            {showNewProj &&
                <NewProjectModal
                    data={editedProject !== null ? projects[editedProject] : null}
                    cancelHandler={closeNewProjectModal}
                    projectSetter={updateProjectData}
                />
            }

            <Navbar active="projects" />
            <header
                className="flex justify-between mt-10 px-5 py-3 bg-accent2"
            >
                <h2 className="text-lg capitalize">Projects</h2>
                <NewProjButton clickHandler={addProject} />
            </header>
            <main className="grid grid-cols-2 gap-10 py-5 h-100 overflow-scroll">
                {projects.map((proj, i) => (
                    <ProjectBlock
                        data={proj}
                        editor={() => editProject(i)}
                        remover={() => removeProject(i)}
                    />
                ))}
            </main>
        </div>
    )
}

/* Container of Each Project */
function ProjectBlock(
    { data, editor, remover }:
        { data: ProjectData, editor: () => void, remover: () => void }
) {

    // Expand or collapse Tasks data
    const [showTasks, setShowTasks] = useState(false)
    // Whether to show the Project menu
    const [showMenu, setShowMenu] = useState(false)

    return (
        <div key={data.id}>
            <div className="bg-white px-10 py-5 relative">
                <img
                    onClick={() => setShowMenu(!showMenu)}
                    className="cursor-pointer ml-auto"
                    src={menuIcon} alt="Menu" />

                {showMenu &&
                    <div className="
                        absolute top-14 right-5 px-3 py-1 flex flex-col
                        z-10 bg-primary shadow rounded-lg overflow-hidden
                    ">
                        <button
                            onClick={() => { setShowMenu(false); editor(); }}
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

                <h3 className="font-bold text-xl ">
                    {data.name}
                </h3>
                <div className="flex justify-between text-xs">
                    <p>{data.estimatedHr ? data.estimatedHr : 0} h</p>
                    {
                        data.endTime
                            ? <p className="text-gray">
                                {data.endTime.toString()}
                            </p>
                            : <p className="text-red-500">
                                {data.dueTime?.toString()}
                            </p>
                    }
                </div>
            </div>
            <div className="bg-light px-10">
                <p className="py-5" dangerouslySetInnerHTML={
                    { __html: data.description }
                }></p>
                <hr className="border-gray" />
                <button
                    onClick={() => setShowTasks(!showTasks)}
                    className="flex gap-5 py-5">
                    <img
                        className={
                            showTasks ? 'rotate-90' : ''
                        } src={arrowIcon} alt="More Tasks" />
                    <span>Tasks</span>
                </button>

                {showTasks && <TaskWrapper project={data} />}

            </div>
        </div>
    )
}

/* Modal to CREATE/UPDATE Projects */
export function NewProjectModal(
    { data, cancelHandler, projectSetter }
        : { data: ProjectData | null, cancelHandler: () => void, projectSetter: (p: ProjectData) => void }
) {

    // When "Confirm" button from NewProject modal is clicked, data is saved
    const confirmAddingProject = (e: FormEvent) => {
        e.preventDefault();

        // 1. Build a Project with data from NewProject modal's form
        const newData: any = {}
        const form = e.target as HTMLFormElement;
        const formData = new FormData(form);
        formData.forEach((val, key) => {
            newData[key] = val;
        })
        newData.status = 'CREATED'

        // Cast the duration into a double value
        // newData['duration'] = numberOrNull(newData['duration'])

        const url = PROJECT_API + (data ? `/update/${data.id}` : '/add');
        const method = data ? 'PUT' : 'POST';
        
        // API: UPDATE PROJECT OF A GIVEN TITLE
        // API: CREATE A PROJECT
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
            }).then(project => {
                projectSetter(project)
            })
            .catch(err => console.log(err))
    }

    // Styles
    const input = "col-span-3 px-2 py-1 border border-light"
    const label = "font-medium uppercase text-sm"
    const button = "rounded-lg text-white px-5 py-2 text-sm uppercase font-medium"

    return (
        <div className="
            absolute top-0 left-0 w-screen min-h-screen
            bg-[#000a] backdrop-blur z-20
        ">
            <form onSubmit={confirmAddingProject} className="
                grid grid-cols-4 items-center gap-x-8 gap-y-3
                w-fit m-auto my-5 2xl:my-10
                bg-white p-10 rounded-lg 
            ">

                <label className={label}>Category</label>
                <input className={input} name="categoryId" value={1}></input>

                <label className={label}>Title</label>
                {
                    data
                        ? <input className={input + ' border-none'} type="text" name="name" defaultValue={data.name} />
                        : <input
                            className={input} type="text" name="name"
                        />
                }

                <label className={label}>Due</label>
                <input
                    className={input}
                    type="datetime-local"
                    name="dueTime"
                    defaultValue={data ? data.endTime.toString() : ''}
                />

                <label className={label}>Time Est.</label>
                <input
                    className={input}
                    type="number" step={0.1}
                    name="estimatedHr"
                    defaultValue={data ? data.estimatedHr : 1}
                />

                <label className={label}>Time Est.</label>
                <input
                    className={input}
                    type="number" step={0.1}
                    name="dailyHr"
                    defaultValue={data ? data.estimatedHr : 1}
                />

                <label className={label}>Priority</label>
                <select
                    className={input + " capitalize"}
                    name="priority"
                >   {
                        ['LOWEST', 'LOW', 'MEDIUM', 'HIGH', 'HIGHEST'].map((p, idx) => (
                            <option key={idx} value={p}>{p}</option>
                        ))
                    }
                </select>

                <label className={label}>Description</label>
                <textarea
                    rows={6}
                    name="description"
                    className={input + ' col-span-4'}
                    defaultValue={data ? data.description : ''}></textarea>

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