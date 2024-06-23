import { useContext } from 'react';
import logo from '../assets/images/logo.png';
import backlogLogo from '../assets/images/backlog-logo.png'
import { LoadingContext, SyncContext } from '../App';
import { BACKLOG_API, LOGOUT_API } from '../conf';
import { Button } from './common';
import { useCookies } from 'react-cookie';

type NavProps = { active: string }

export const baseStyle = 'uppercase text-sm w-20 py-1 text-center';
export const activeStyle = ' bg-secondary text-white font-medium';

// Parameters to poll the endpoint of backlog-sync
const MAX_POLL = 5, POLL_INTERVAL = 3000;
let poll_count = 0;

/* Navigation Bar */
export function Navbar({ active }: NavProps) {

    const syncContext = useContext(SyncContext);
    const loadingContext = useContext(LoadingContext);

    const [cookies] = useCookies(['XSRF-TOKEN']);

    /* 
        Logout by
        - Calling the LOGOUT_API on the server
        - Following the returned link to logout with oauth2 provider
    */
    const logout = () => {
        fetch(LOGOUT_API, {
            method: 'POST', credentials: 'include',
            headers: { 'X-XSRF-TOKEN': cookies['XSRF-TOKEN'] }
        })
            .then(res => res.json())
            .then(response => {
                window.location.href = response.logoutUrl + `&returnTo=${window.location.origin}`;
            });
    }

    /*
        Sync/Unsync with backlog
        Currently, server endpoint is polled to update the state
        In the future, webhooks can be added
    */
    const syncWithBacklog = () => {

        loadingContext?.setLoading(true);

        // API: SYNC/UNSYNC WITH BACKLOG
        const action = syncContext?.sync ? '/off' : '/on';
        fetch(BACKLOG_API + action, { // fetch backend API
            method: 'GET', credentials: 'include'
        })
            .then(res => {
                // Continue if the status is 200 or 302, throw otherwise.
                if (res.status < 400) return res.json() 
                else throw Error(res.statusText)
            })
            .then(data => {
                // If the response contains a 'url' parameter,
                // it is the OAuth2 login url, so it is followed.
                if (data['url']) {
                    window.open(data['url'], '_blank');
                    // Endpoint is then polled based on specified parameters
                    setTimeout(() => {

                        // If poll limit is exceeded, polling is stopped, and a debug message is shown.
                        if (++poll_count >= MAX_POLL) {
                            poll_count = 0;
                            loadingContext?.setLoading(false);
                            console.log("CANNOT SYNC");
                        } else {

                        // Otherwise, we keep polling.
                            syncWithBacklog();
                        }
                    }, POLL_INTERVAL);

                // If the response doesn't contain 'url', it means backlog-sync has been successful.
                // Poll count is then reset, and loading stopped.
                } else {
                    poll_count = 0;
                    loadingContext?.setLoading(false);
                    syncContext?.setSync(!syncContext?.sync);
                }

            })
            .catch(err => {
                loadingContext?.setLoading(false);
                console.log("CANNOT SYNC");
                console.log(err)
            })
    }

    // Style of each link, based on if it is active.
    const linkStyle = (link: string, idx: number) =>
        baseStyle
        + (idx === 0 ? ' rounded-l-lg' : ' rounded-r-lg  hover:bg-secondary hover:text-white')
        + (link === active ? activeStyle : ' shadow bg-white font-light')

    return (
        <nav className="
            flex justify-between items-center
        ">
            {/* 1. Logo */}
            <img className='h-8' src={logo} alt="Logo" />
            <div className="flex gap-10">

                {/* 2. Links */}
                <div className="flex">
                    {
                        ['tasks', 'projects'].map((l, idx) => (
                            <a key={idx} href={'/' + l} className={linkStyle(l, idx)}>{l}</a>
                        ))
                    }
                </div>

                {/* 3. Backlog-sync button */}
                <button onClick={syncWithBacklog}>
                    <img className={'w-7 rounded' + (!syncContext?.sync ? ' grayscale' : '')} src={backlogLogo} alt="Backlog" />
                </button>
            </div>

            {/* 4. Logout button */}
            <Button label='Log Out' clickHandler={logout} />
        </nav>
    )
}