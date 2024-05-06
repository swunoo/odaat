import logo from '../assets/images/logo.png';

type NavProps = {active: string}

export default function Navbar({active}: NavProps){

    const baseStyle = 'uppercase text-sm w-20 py-1 text-center';
    const activeStyle = ' bg-secondary text-white font-medium';
    const linkStyle = (link: string, idx: number) => 
        baseStyle 
        + (idx === 0 ? ' rounded-l-lg' : ' rounded-r-lg  hover:bg-secondary hover:text-white') 
        + (link === active ? activeStyle : ' shadow bg-white font-light') 

    return (
        <nav className="
            flex justify-between items-center
        ">
            <img className='h-8' src={logo} alt="Logo" />
            <div className="flex">
                {
                    ['tasks', 'projects'].map((l, idx) => (
                        <a key={idx} href={'/' + l} className={linkStyle(l, idx)}>{l}</a>
                    ))
                }
            </div>
        </nav>
    )
}