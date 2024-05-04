import plusIcon from '../assets/images/plus.svg'

/** Components */

export function AddButton(
    {label, clickHandler}:
    {label: string, clickHandler: ()=>void}
){
    return (
        <button
            onClick={clickHandler}
            className="
                bg-white px-5 py-1 rounded text-sm shadow-lg hover:bg-secondary hover:text-white
        ">{label}</button>
    )
}

export function NewButton(
    {label, clickHandler}:
    {label: string, clickHandler: ()=>void}
){
    return (
        <button
        onClick={clickHandler}
        className="flex gap-2 items-center w-fit hover:underline text-xs uppercase">
            <img src={plusIcon} alt="New Project" />
            <span>{label}</span>
        </button>
    )
}

const svgBtnStyle = "bg-secondary fill-primary hover:bg-primary hover:fill-secondary rounded cursor-pointer"

export function SvgChevronLeft(
    {clickHandler}: {clickHandler: ()=>void}
){
    return (
        <div onClick={clickHandler} className={svgBtnStyle}>
            <svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24"><path d="M560-240 320-480l240-240 56 56-184 184 184 184-56 56Z"/></svg>
        </div>
    )
}

export function SvgChevronRight(
    {clickHandler}: {clickHandler: ()=>void}
){
    return (
        <div onClick={clickHandler} className={svgBtnStyle}>
            <svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24"><path d="M504-480 320-664l56-56 240 240-240 240-56-56 184-184Z"/></svg>
        </div>
    )
}