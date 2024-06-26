import { BrowserRouter, Route, Routes } from "react-router-dom"
import Projects from "./components/Projects"
import Tasks from "./components/Tasks"
import { createContext, useEffect, useState } from "react"
import { LoadingVeil } from "./components/common"
import { About } from "./components/About"
import { PUBLIC_API } from "./conf"

/* Application Contexts */

// Whether the data is synced (with Backlog)
export interface SyncContextType { sync: boolean, setSync: (value: boolean) => void; }
export const SyncContext = createContext<SyncContextType | undefined>(undefined);

// Whether the user is authenticated
export interface AuthContextType { authenticated: boolean, setAuthenticated: (value: boolean) => void; }
export const AuthContext = createContext<AuthContextType | undefined>(undefined);

// Whether the application is in a "Loading" state
export interface LoadingContextType { loading: boolean, setLoading: (value: boolean) => void; }
export const LoadingContext = createContext<LoadingContextType | undefined>(undefined);

/* Application Entry Point */

export default function App() {

  const appStyle = 'bg-primary min-h-screen max-h-screen'

  const [ sync, setSync ] = useState<boolean>(false);
  const [ authenticated, setAuthenticated ] = useState<boolean>(false);
  const [ loading, setLoading ] = useState<boolean>(false);

  // Check if the user is authenticated
  useEffect(() => {
    fetch(PUBLIC_API, {
        method: 'GET',
        credentials: 'include'
    })
        .then(res => res.text())
        .then(data => {
            if (data === '') setAuthenticated(false)
            else setAuthenticated(true)
        })
        .catch(err => console.log(err))
}, [])

  // Show application pages and routes when authenticated.
  // Show the About page when unauthenticated.
  return (
    <LoadingContext.Provider value={ { loading, setLoading } }>
      <AuthContext.Provider value={ { authenticated, setAuthenticated } }>
      <SyncContext.Provider value={ { sync, setSync } }>
        {loading && <LoadingVeil />}
        <BrowserRouter>
          { authenticated ?
            <div className={appStyle}>
                <Routes>
                  <Route path="/tasks" element={<Tasks />} />
                  <Route path="/projects" element={<Projects />} />
                  <Route path="/" element={<Tasks />} />
                </Routes>
            </div>
            :
            <About />
          }
        </BrowserRouter>
      </SyncContext.Provider>
    </AuthContext.Provider>
    </LoadingContext.Provider>
  )
}