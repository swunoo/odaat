import { BrowserRouter, Route, Routes } from "react-router-dom"
import Tasks from "./components/Tasks"
import Projects from "./components/Projects"

function App() {

  return (
    <BrowserRouter>
      <div className="font-serif">
          <Routes>
            <Route path="/tasks" element={<Tasks />} />
            <Route path="/projects" element={<Projects />} />
            <Route path="/" element={<Tasks />} />
          </Routes>
      </div>
    </BrowserRouter>
  )
}

export default App