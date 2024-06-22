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
export function getValue(id: string): string | null {
    const ele = document.getElementById(id);
    if (ele instanceof HTMLInputElement || ele instanceof HTMLTextAreaElement || ele instanceof HTMLSelectElement) {
        return ele.value;
    }
    return null;
}

export function combineDateAndTimeInput(dateId: string, timeId: string, fallback: Date): Date {
    let [date, time] = [getValue(dateId), getValue(timeId)]

    console.log(fallback);
    
    
    if(!date){
        const [month, day, year] = new Date(fallback).toLocaleDateString().split('/');
        date = year + '-' + month.padStart(2, '0') + '-' + day.padStart(2, '0');
    }
    if(!time){
        let [hrMinSec, amPm] = new Date(fallback).toLocaleTimeString().split(' ');
        let [hr, min, sec] = hrMinSec.split(':')
        hr = String(amPm === 'PM' ? (Number)(hr) + 12 : hr).padStart(2, '0');
        time = [hr, min, sec].join(':')
    }
    
    return new Date(`${date}T${time}`);
}

// Cast it to a number, or return null
export function numberOrNull(input: string){
    if(input && input.length > 0) return (Number)(input)
    return null
}

// Convert date to a formatted string
export function formatTime(date: Date, mode?: string): string{
    
    if(!date) return '';
    const ds = new Date(date).toString().split(' ');
    const [year, month, day] = [ds[3], ds[1], ds[2]]
    return mode && mode === 'full' ? year + '-' + month + '-' + day : month + '/' + day;
}

// Return HH:mm from a Date
export function getHHmm(d: Date): string {
    const [hr, min] = [d.getHours(), d.getMinutes()];
    return (hr < 10 ? '0' + hr : hr) + ':' + (min < 10 ? '0' + min : min);
}

// Generate a range [e.g. 10:00 - 11:00] from startTime [e.g. 2024-06-15 10:00] and durationHr [e.g. 1]
export function getTimeRange(date: Date, durationHr: number): string{
    if(!date) return '';
    const dateDate = new Date(date); // input date might be a string parsed from JSON.

    return getHHmm(dateDate) + ' - ' + getHHmm(new Date(dateDate.getTime() + (durationHr * 3600 * 1000)))
}

// Generate a string formatted like the value of a date input
export function getDateFormatted(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-based
    const day = String(date.getDate()).padStart(2, '0');
  
    return `${year}-${month}-${day}`;
  }

