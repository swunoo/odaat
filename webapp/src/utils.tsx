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

