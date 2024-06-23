import logo from '../assets/images/logo.png';
import projects_create from '../assets/images/cover/projects_create.png'
import projects_sync from '../assets/images/cover/projects_sync.png'
import projects from '../assets/images/cover/projects.png'
import tasks_create from '../assets/images/cover/tasks_create.png'
import tasks from '../assets/images/cover/tasks.png'
import { Carousel, Footer } from "./common"
import { USER_API } from "../conf"

/*
    # About Page
    Shown at "/" when the user is unauthenticated.
*/
export function About() {

    const carouselImgs = [tasks, tasks_create, projects, projects_create, projects_sync];

    const login = () => window.location.href = USER_API;

    return (
        <div className="md:max-h-screen m-auto py-10 px-20">
                <img className='h-8 m-auto mt-3 mb-10' src={logo} alt="Logo" />
                <h3 className="text-center">
                    <span className="font-bold text-secondary">M</span>
                    anage your work | 
                    <span className="pl-1 font-bold text-secondary">S</span>
                    ync your projects | 
                    <span className="pl-1 font-bold text-secondary">G</span>
                    enerate daily tasks
                </h3>
                <Carousel images={carouselImgs} />
                <div className="flex justify-center my-10">
                    <button
                        className="bg-secondary text-white font-bold shadow px-5 py-2 rounded hover:bg-primary hover:text-black hover:text-white"
                        onClick={login}>
                            Log In
                    </button>
                </div>
                <Footer />
        </div>
    )
}