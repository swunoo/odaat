import { useState } from 'react';
import plusIcon from '../assets/images/plus.svg'

/* Types */
export type VoidFunc = () => void;

/* Components */

export function Button(
    { label, clickHandler }:
        { label: string, clickHandler: VoidFunc }
) {
    return (<button
        onClick={clickHandler}
        className="
        bg-accent2 px-5 py-1 rounded text-sm hover:bg-secondary hover:text-white
    ">{label}</button>
    )
}

export function NewProjButton(
    { clickHandler }:
        { clickHandler: VoidFunc }
) {
    return (
        <button
            onClick={clickHandler}
            className="
                bg-white px-5 py-2 rounded text-sm shadow-lg hover:bg-secondary hover:text-white
        ">New Project</button>
    )
}

export function NewTaskButton(
    { clickHandler }:
        { clickHandler: VoidFunc }
) {
    return (
        <button
            onClick={clickHandler}
            className="
                bg-secondary text-white px-5 py-2 rounded text-sm shadow-lg hover:bg-secondary hover:text-white
        ">New Task</button>
    )
}

export function NewButton(
    { label, clickHandler }:
        { label: string, clickHandler: VoidFunc }
) {
    return (
        <button
            onClick={clickHandler}
            className="flex gap-2 items-center w-fit hover:underline text-xs uppercase">
            <img src={plusIcon} alt="New Project" />
            <span>{label}</span>
        </button>
    )
}

const svgBtnStyle = "h-fit bg-secondary fill-primary hover:bg-primary hover:fill-secondary rounded cursor-pointer"

export function SvgChevronLeft(
    { clickHandler }: { clickHandler: VoidFunc }
) {
    return (
        <div onClick={clickHandler} className={svgBtnStyle}>
            <svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24"><path d="M560-240 320-480l240-240 56 56-184 184 184 184-56 56Z" /></svg>
        </div>
    )
}

export function SvgChevronRight(
    { clickHandler }: { clickHandler: VoidFunc }
) {
    return (
        <div onClick={clickHandler} className={svgBtnStyle}>
            <svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24"><path d="M504-480 320-664l56-56 240 240-240 240-56-56 184-184Z" /></svg>
        </div>
    )
}

export function LoadingVeil() {
    return (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 backdrop-blur-sm z-50">
            <div className="loader"></div>
        </div>
    )
}

export function Carousel({ images }: { images: string[] }) {
    const [currentIndex, setCurrentIndex] = useState(0);

    const prevSlide = () => {
        const newIndex = currentIndex === 0 ? images.length - 1 : currentIndex - 1;
        setCurrentIndex(newIndex);
    };

    const nextSlide = () => {
        const newIndex = currentIndex === images.length - 1 ? 0 : currentIndex + 1;
        setCurrentIndex(newIndex);
    };

    return (
        <div className="flex max-w-xl m-auto items-center my-5">
            <SvgChevronLeft clickHandler={prevSlide} />
            <div className="relative w-full max-w-lg mx-auto">
                <div className="overflow-hidden relative h-64 rounded-lg">
                    {images.map((image, index) => (
                        <img
                            key={index}
                            src={image}
                            alt={`Slide ${index}`}
                            className={`absolute inset-0 w-full h-full object-cover transition-transform duration-500 ease-in-out transform ${index === currentIndex ? 'translate-x-0' : 'translate-x-full'}`}
                            style={{ transform: `translateX(${(index - currentIndex) * 100}%)` }}
                        />
                    ))}
                </div>
                <div className="absolute bottom-0 left-1/2 transform -translate-x-1/2 flex space-x-2 p-2">
                    {images.map((_, index) => (
                        <div
                            key={index}
                            className={`w-2 h-2 rounded-full ${index === currentIndex ? 'bg-white' : 'bg-gray-400'}`}
                        ></div>
                    ))}
                </div>
            </div>
            <SvgChevronRight clickHandler={nextSlide} />

        </div>
    );
};

export function Footer(){
    return (
        <footer className='absolute left-0 bottom-0 py-1 text-center text-sm w-full bg-primary'>
            swanoo © 2024
        </footer>
    )
}