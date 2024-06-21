import { useEffect, useState } from 'react'
import logo from './assets/images/logo.png'
import { PROJECT_API } from './conf';
import { useCookies } from 'react-cookie';

export function Homepapge() {

    const [authenticated, setAuthenticated] = useState(false);

    const priv = 'http://localhost:9000/api/user';

    useEffect(() => {
        fetch('http://localhost:9000/tmp/public', {
            method: 'GET',
            credentials: 'include'
        })
            .then(res => res.text())
            .then(data => {
                console.log(data);
                if (data === 'NO') setAuthenticated(false)
                else { setAuthenticated(true) }

            })
            .catch(err => console.log(err))
    }, [])

    const login = () => {
        window.location.href = priv;
    }

    const logout = () => {
        fetch('http://localhost:9000/api/logout', {
            method: 'GET', credentials: 'include'
        })
            .then(res => res.json())
            .then(response => {
                window.location.href = response.logoutUrl + `&returnTo=${window.location.origin}`;
            });
    }

    return (
        <div className="
            h-screen w-screen bg-accent2 py-10
        ">
            <div className="flex justify-center">
                <img className='w-20' src={logo} alt="Logo" />
            </div>

            <div className="relative  my-10 ">
                <div className=" absolute top-4 border-t border-light w-screen"></div>
                <div className="flex justify-center h-fit">
                    <h1>{authenticated}</h1>
                    {authenticated
                        ? <button onClick={logout}>Logout</button>
                        : <button onClick={login}>Login</button>
                    }
                    <h3 className="
                        text-lg text-secondary
                        relative w-fit
                        bg-accent2 px-5
                    ">
                        A simple, free, and powerful tool for personal task management
                    </h3>
                </div>
            </div>
        </div>
    )
}