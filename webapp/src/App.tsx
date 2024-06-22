import { BrowserRouter, Route, Routes } from "react-router-dom"
import Projects from "./components/Projects"
import Tasks from "./components/Tasks"
import { Homepapge } from "./Homepage"
import { createContext, useEffect, useState } from "react"
import { LoadingVeil } from "./components/common"
import { About } from "./components/About"
import { PUBLIC_API } from "./conf"

export interface SyncContextType { sync: boolean, setSync: (value: boolean) => void; }
export interface AuthContextType { authenticated: boolean, setAuthenticated: (value: boolean) => void; }
export interface LoadingContextType { loading: boolean, setLoading: (value: boolean) => void; }

export const SyncContext = createContext<SyncContextType | undefined>(undefined);
export const AuthContext = createContext<AuthContextType | undefined>(undefined);
export const LoadingContext = createContext<LoadingContextType | undefined>(undefined);

function App() {

  const appStyle = 'bg-primary min-h-screen'

  const [ sync, setSync ] = useState<boolean>(false);
  const [ authenticated, setAuthenticated ] = useState<boolean>(false);
  const [ loading, setLoading ] = useState<boolean>(false);

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


  return (
    <LoadingContext.Provider value={ { loading, setLoading } }>
      <AuthContext.Provider value={ { authenticated, setAuthenticated } }>
      <SyncContext.Provider value={ { sync, setSync } }>
        {loading && <LoadingVeil />}
        <BrowserRouter>
          { authenticated ?
            <div className={appStyle}>
                <Routes>
                  <Route path="/home" element={<Homepapge />} />
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

export default App