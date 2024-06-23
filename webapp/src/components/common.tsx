import { useState } from 'react';
import plusIcon from '../assets/images/plus.svg'

/* Common Types */
export type VoidFunc = () => void;

/* Common Components */

// Generic button
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

// Special button for "New Project"
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

// Special button for "New Task"
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

// Special button without a border, and shown with a "+" sign.
export function PlusButton(
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

// Common style for SvgChevronLeft and SvgChevronRight
const svgBtnStyle = "h-fit bg-secondary fill-primary hover:bg-primary hover:fill-secondary rounded cursor-pointer"

// Left-facing, clickable chevron
export function SvgChevronLeft(
    { clickHandler }: { clickHandler: VoidFunc }
) {
    return (
        <div onClick={clickHandler} className={svgBtnStyle}>
            <svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24"><path d="M560-240 320-480l240-240 56 56-184 184 184 184-56 56Z" /></svg>
        </div>
    )
}

// Right-facing, clickable chevron
export function SvgChevronRight(
    { clickHandler }: { clickHandler: VoidFunc }
) {
    return (
        <div onClick={clickHandler} className={svgBtnStyle}>
            <svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24"><path d="M504-480 320-664l56-56 240 240-240 240-56-56 184-184Z" /></svg>
        </div>
    )
}

// Loading screen
export function LoadingVeil() {
    return (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 backdrop-blur-sm z-50">
            <div className="loader"></div>
        </div>
    )
}

// Image carousel
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
        <div className="flex gap-10 m-auto max-w-xl 2xl:max-w-5xl items-center my-5">
            <SvgChevronLeft clickHandler={prevSlide} />
            <div className="relative w-full mx-auto">
                <div className="overflow-hidden relative h-60 2xl:h-600 rounded-lg">
                    {images.map((image, index) => (
                        <img
                            key={index}
                            src={image}
                            alt={`Slide ${index}`}
                            className={`absolute inset-0 w-full h-fit m-auto shadow-lg rounded object-cover transition-transform duration-500 ease-in-out transform ${index === currentIndex ? 'translate-x-0' : 'translate-x-full'}`}
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
}

// Footer with developer information
export function Footer() {
    return (
        <footer className='absolute left-0 bottom-0 py-1 text-center text-sm w-full bg-primary'>
            swanoo © 2024
        </footer>
    )
}