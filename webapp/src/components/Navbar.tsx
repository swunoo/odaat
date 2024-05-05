import logo from '../assets/images/logo.png'

type NavProps = {active: string}

export default function Navbar({active}: NavProps){

    const baseStyle = 'capitalize';
    const activeStyle = ' underline';
    const linkStyle = (link: string) => 
        baseStyle + (link === active ? activeStyle : '') 

    return (
        <nav className="
            flex justify-between items-center
        ">
            <img className='h-8' src={logo} alt="Logo" />
            <div className="flex gap-16">
                {
                    ['tasks', 'projects'].map((l, idx) => (
                        <a key={idx} href={'/' + l} className={linkStyle(l)}>{l}</a>
                    ))
                }
            </div>
        </nav>
    )
}