import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App'
import './index.css'
import { CookiesProvider } from 'react-cookie'

// Parse Date to be compatible with custom formats
Date.prototype.toJSON = function () {
  var timezoneOffsetInHours = -(this.getTimezoneOffset() / 60);
  var correctedDate = new Date(this.getFullYear(), this.getMonth(), 
      this.getDate(), this.getHours(), this.getMinutes(), this.getSeconds(), 
      this.getMilliseconds());
  correctedDate.setHours(this.getHours() + timezoneOffsetInHours);
  return correctedDate.toISOString().replace('Z', '');
}

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
      <CookiesProvider>
        <App />
      </CookiesProvider>
  </React.StrictMode>,
)
