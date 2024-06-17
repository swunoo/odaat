import { useContext } from 'react';
import logo from '../assets/images/logo.png';
import backlogLogo from '../assets/images/backlog-logo.png'
import { LoadingContext, SyncContext } from '../App';
import { BACKLOG_API } from '../conf';

type NavProps = {active: string}

export const baseStyle = 'uppercase text-sm w-20 py-1 text-center';
export const activeStyle = ' bg-secondary text-white font-medium';

const MAX_POLL = 5;
let poll_count = 0;

export default function Navbar({active}: NavProps){

    const syncContext = useContext(SyncContext);
    const loadingContext = useContext(LoadingContext);

    const syncWithBacklog = () => {

        loadingContext?.setLoading(true);

        // API: SYNC/UNSYNC WITH BACKLOG
        const action = syncContext?.sync ? '/off' : '/on';
        fetch(BACKLOG_API + action, {
            method: 'GET'
        })
        .then(res => { 
            if(res.status < 400) return res.json()
            else throw Error (res.statusText)
        })
        .then(data => {
            if(data['url']){
                window.open(data['url'], '_blank');
                setTimeout(() => {
                    if(++poll_count >= MAX_POLL){
                        poll_count = 0;
                        throw Error ('Poll Limit Exceeded')
                    }
                    syncWithBacklog();
                }, 3000);

            } else {
                poll_count = 0;
                loadingContext?.setLoading(false);
                syncContext?.setSync(!syncContext?.sync);
            }

        })
        .catch(err =>{
            loadingContext?.setLoading(false);
            console.log("CANNOT SYNC");
            console.log(err)
        })
    }

    const linkStyle = (link: string, idx: number) => 
        baseStyle 
        + (idx === 0 ? ' rounded-l-lg' : ' rounded-r-lg  hover:bg-secondary hover:text-white') 
        + (link === active ? activeStyle : ' shadow bg-white font-light') 

    return (
        <nav className="
            flex justify-between items-center
        ">
            <img className='h-8' src={logo} alt="Logo" />
            <div className="flex gap-10">
                <div className="flex">
                    {
                        ['tasks', 'projects'].map((l, idx) => (
                            <a key={idx} href={'/' + l} className={linkStyle(l, idx)}>{l}</a>
                        ))
                    }
                </div>
                <button onClick={syncWithBacklog}>
                    <img className={'w-7 rounded' + (!syncContext?.sync ? ' grayscale' : '')} src={backlogLogo} alt="Backlog" />
                </button>
            </div>
        </nav>
    )
}