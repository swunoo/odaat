import Navbar from "./Navbar";

import calendarIcon from '../assets/images/calendar.svg';

import { useState } from "react";
import { dateToString, MILLIS_A_DAY } from "../utils";
import { SvgChevronLeft, SvgChevronRight } from "./common";
import { NewProjectModal } from "./Projects";
import { TaskWrapper } from "./TaskWrapper";
import { ProjectData } from "../conf";

/* Tasks Page */
export default function Tasks(){

    // Whether to show the NewProject modal
    const [showNewProj, setShowNewProj] = useState(false)
    // To render newly added project titles after adding NewProject on TaskPage
    const [newProjTitle, setNewProjTitle] = useState<string | undefined>(undefined)

    // After adding a new project, it is reflected in TaskWrapper
    const onProjectCreate = (p: ProjectData) => {
        setNewProjTitle(p.title)
        setShowNewProj(false)
    }

    return (
        <div className="md:max-h-screen py-10 px-20">
            {showNewProj && 
                <NewProjectModal
                    data={null}
                    cancelHandler={() => setShowNewProj(false)}
                    projectSetter={(p)=> onProjectCreate(p)}
                />
            }

            <Navbar active="tasks"/>
            <TaskContainer newProjTitle={newProjTitle} setShowNewProj={setShowNewProj} />
        </div>
    )
}

/* Container for the header and each Task */
function TaskContainer(
    {newProjTitle, setShowNewProj}
    : {newProjTitle?: string, setShowNewProj: (s: boolean)=>void}
){

    // Date of the Tasks to display
    const [date, setDate] = useState(new Date())

    // When "Add Project" button is clicked, show NewProject modal
    const addProject = () => {
        setShowNewProj(true)
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
        <div className="my-10 mx-20 shadow relative">

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
                {/* <AddButton label="New Task" clickHandler={addTask} /> */}
            </nav>
            <main className="
                bg-white h-96 px-5 overflow-scroll
            ">
                <TaskWrapper date={date} addProject={addProject} newProjTitle={newProjTitle} />
            </main>
        </div>
    )
}