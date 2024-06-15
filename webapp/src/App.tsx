import { BrowserRouter, Route, Routes } from "react-router-dom"
import Projects from "./components/Projects"
import Tasks from "./components/Tasks"
import { Homepapge } from "./Homepage"

function App() {

  const appStyle = 'bg-primary min-h-screen'

  Date.prototype.toJSON = function () {
    var timezoneOffsetInHours = -(this.getTimezoneOffset() / 60);
    var correctedDate = new Date(this.getFullYear(), this.getMonth(), 
        this.getDate(), this.getHours(), this.getMinutes(), this.getSeconds(), 
        this.getMilliseconds());
    correctedDate.setHours(this.getHours() + timezoneOffsetInHours);
    return correctedDate.toISOString().replace('Z', '');
  }

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