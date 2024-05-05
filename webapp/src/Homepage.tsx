import logo from './assets/images/logo.png'
import ss_tasks from './assets/images/screencap_tasks.png'

export function Homepapge(){
    return (
        <div className="
            h-screen w-screen bg-accent2 py-10
        ">
            <div className="flex justify-center">
                <img className='w-20' src={logo} alt="Logo" />
            </div>

            <div className="relative  my-10 ">
                <div className=" absolute top-4 border-t border-light w-screen"></div>
                <div className="flex justify-center h-fit">
                    <h3 className="
                        text-lg text-secondary
                        relative w-fit
                        bg-accent2 px-5
                    ">
                        A simple, free, and powerful tool for personal task management
                    </h3>
                </div>
            </div>

            <div className="grid grid-cols-3 px-32 gap-20">
                <img className='col-span-2 shadow-lg m-auto' src={ss_tasks} alt="Screenshot" />
                <div className="">
                    <h2>Lorem</h2>
                    <p>Ipsum</p>
                </div>
            </div>
        </div>
    )
}