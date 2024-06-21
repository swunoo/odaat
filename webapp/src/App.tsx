import { BrowserRouter, Route, Routes } from "react-router-dom"
import Projects from "./components/Projects"
import Tasks from "./components/Tasks"
import { Homepapge } from "./Homepage"
import { createContext, useState } from "react"
import { LoadingVeil } from "./components/common"
import { About } from "./About"

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


  return (
    <LoadingContext.Provider value={ { loading, setLoading } }>
      <AuthContext.Provider value={ { authenticated, setAuthenticated } }>
      <SyncContext.Provider value={ { sync, setSync } }>
        {loading && <LoadingVeil />}
        <BrowserRouter>
          <div className={appStyle}>
            { authenticated ?
              <Routes>
                <Route path="/home" element={<Homepapge />} />
                <Route path="/tasks" element={<Tasks />} />
                <Route path="/projects" element={<Projects />} />
                <Route path="/" element={<Tasks />} />
              </Routes>
              :
              <About />
            }
          </div>
        </BrowserRouter>
      </SyncContext.Provider>
    </AuthContext.Provider>
    </LoadingContext.Provider>
  )
}

export default App