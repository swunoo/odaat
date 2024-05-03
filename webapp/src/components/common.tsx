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