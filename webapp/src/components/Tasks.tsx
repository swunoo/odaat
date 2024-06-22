import { Navbar } from "./Navbar";

import calendarIcon from '../assets/images/calendar.svg';

import { useContext, useState } from "react";
import { ProjectData } from "../conf";
import { formatTime, MILLIS_A_DAY } from "../utils";
import { SvgChevronLeft, SvgChevronRight } from "./common";
import { NewProjectModal } from "./Projects";
import { TaskWrapper } from "./TaskWrapper";
import { AuthContext } from "../App";
import { About } from "./About";

/* Tasks Page */
export default function Tasks(){

    // Whether to show the NewProject modal
    const [showNewProj, setShowNewProj] = useState(false);
    // To render newly added project titles after adding NewProject on TaskPage
    const [newProj, setnewProj] = useState<ProjectData | undefined>(undefined);
    // Whether the user is authenticated
    const auth = useContext(AuthContext);

    // After adding a new project, it is reflected in TaskWrapper
    const onProjectCreate = (p: ProjectData) => {
        setnewProj(p);
        setShowNewProj(false);
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
            {  auth?.authenticated
               ? <TaskContainer newProj={newProj} setShowNewProj={setShowNewProj} />
               : <About />
            }
        </div>
    )
}

/* Container for the header and each Task */
export function TaskContainer(
    {newProj, setShowNewProj}
    : {newProj?: ProjectData, setShowNewProj: (s: boolean)=>void}
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
                    ">{formatTime(date, 'full')}</h3>
                    <div className="flex gap-3">
                        <SvgChevronLeft clickHandler={()=>prevDay()}/>
                        <SvgChevronRight clickHandler={()=>nextDay()}/>
                    </div>
                </div>
            </nav>
            <main className="
                bg-white max-h-65 min-h-96 2xl:max-h-75 px-5 overflow-scroll
            ">
                <TaskWrapper date={date} addProject={addProject} newProj={newProj} />
            </main>
        </div>
    )
}