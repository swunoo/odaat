/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {      
      colors: {
        dark: '#101010',
        light: '#EEEEEE',
        gray: '#888',
        accent: '#FB8500',
        accent2: '#8ECAE6',
        primary: '#D6E6ED',
        secondary: '#023047'
      },
      fontFamily: {
      'jomhuria': ['Jomhuria']
     },
     spacing: {
      '100': '28rem',
    }
  },
  },
  plugins: [],
}

