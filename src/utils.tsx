export const numberInputKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    
    if (
        event.key !== null 
        && !(event.key === '.' && !event.currentTarget.value.includes('.'))
        && !['Backspace','Delete','ArrowLeft','ArrowRight'].includes(event.code)
        && isNaN(Number(event.key))
    ) {
        event.preventDefault();
    }
};
