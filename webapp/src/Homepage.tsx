import logo from './assets/images/logo.png'

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
        </div>
    )
}