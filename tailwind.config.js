/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {      
      colors: {
        gray1: '#101010',
        gray2: '#151515',
        gray3: '#333',
        gray4: '#555',
        gray5: '#dedede'
      },
      fontFamily: {
      'jomhuria': ['Jomhuria']
    }},
  },
  plugins: [],
}

