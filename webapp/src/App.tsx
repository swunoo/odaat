import { BrowserRouter, Route, Routes } from "react-router-dom"
import Tasks from "./components/Tasks"
import Projects from "./components/Projects"
import { Homepapge } from "./Homepage"

function App() {

  const appStyle = 'bg-primary min-h-screen'

  return (
    <BrowserRouter>
      <div className={appStyle}>
          <Routes>
            <Route path="/home" element={<Homepapge />} />
            <Route path="/tasks" element={<Tasks />} />
            <Route path="/projects" element={<Projects />} />
            <Route path="/" element={<Tasks />} />
          </Routes>
      </div>
    </BrowserRouter>
  )
}

export default App