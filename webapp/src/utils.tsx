// number of miliseconds in a day
export const MILLIS_A_DAY = 86400000; 

export const numberInputKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    
    if (
        event.key !== null 
        && !(event.key === '.' && !event.currentTarget.value.includes('.'))
        && !['Backspace','Delete','ArrowLeft','ArrowRight'].includes(event.code)
        && isNaN(Number(event.key))
    ) {
        event.preventDefault();
    }

    (event.target as HTMLInputElement).value += event.key
};

export const menuBtnStyle = (option?: string) => {

    let hover = 'hover:bg-accent2';
    if(option === 'delete') hover = 'hover:bg-red-500 hover:text-white'
    else if(option === 'cancel') hover = 'hover:bg-gray hover:text-white'

    return 'cursor-pointer border-b border-white px-5 py-1 ' + hover;
} 

// Given an id, return the value of an input|textarea|select element with that id.
export function getValue(id: string){
    const ele = document.getElementById(id) as (HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement)
    return ele.value
}

export function dateToString(date: Date){
    return date.toISOString().split('T')[0]
}

