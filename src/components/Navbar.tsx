import logo from '../assets/images/logo.png'

type NavProps = {active: string}

export default function Navbar({active}: NavProps){

    const baseStyle = '';
    const activeStyle = ' underline';
    const linkStyle = (link: string) => 
        baseStyle + (link === active ? activeStyle : '') 

    return (
        <div className="
            flex justify-between items-center
        ">
            <img src={logo} alt="Logo" />
            <div className="flex gap-5">
                {
                    ['tasks', 'projects'].map(l => (
                        <a href={'/' + l} className={linkStyle(l)}>Tasks</a>
                    ))
                }
            </div>
        </div>
    )
}