import { Navbar } from "./components/Navbar"

export function About() {
    return (
        <div className="md:max-h-screen max-w-5xl m-auto py-10 px-20">
            <Navbar active={""} />
            <div className="mt-60 text-center">
                Please Sign In.
            </div>
        </div>
    )
}