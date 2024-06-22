import { Navbar } from "./Navbar"

import logo from '../assets/images/logo.png';
import tmp from '../assets/images/cover/tmp.jpeg'
import tmp2 from '../assets/images/cover/tmp2.jpeg'
import { Carousel, Footer } from "./common"
import { USER_API } from "../conf"

export function About() {

    const login = () => {
        window.location.href = USER_API;
    }

    return (
        <div className="md:max-h-screen max-w-5xl m-auto py-10 px-20">
                <img className='h-8 m-auto mt-3 mb-10' src={logo} alt="Logo" />
                <h3 className="text-center">
                    <span className="font-bold text-secondary">M</span>
                    anage your work | 
                    <span className="pl-1 font-bold text-secondary">S</span>
                    ync your projects | 
                    <span className="pl-1 font-bold text-secondary">G</span>
                    enerate daily tasks
                </h3>
                <Carousel images={[tmp, tmp2]} />
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