import { useState } from "react";
import Link from "next/link";

function NavLink({ to, children }) {
  return (
    <a href={to} className={`mx-4`}>
      {children}
    </a>
  );
}

function MobileNav({ open, setOpen }) {
  return (
    <div
      className={`absolute top-0 left-0 h-screen w-screen bg-white text-black transform ${
        open ? "-translate-x-0" : "-translate-x-full"
      } transition-transform duration-300 ease-in-out filter drop-shadow-md `}
    >
      <div className="flex items-center justify-center filter drop-shadow-md h-20">
        {" "}
        {/*logo container*/}
        <Link href="/">
          <span className="text-xl font-semibold">LOGO</span>
        </Link>
      </div>
      <div className="flex flex-col ml-4">
        <Link href="/">
          <span
            className="text-xl font-medium my-4"
            onClick={() =>
              setTimeout(() => {
                setOpen(!open);
              }, 100)
            }
          >
            Home
          </span>
        </Link>
        <Link href="/menu">
          <span
            className="text-xl font-normal my-4"
            onClick={() =>
              setTimeout(() => {
                setOpen(!open);
              }, 100)
            }
          >
            Menu Item
          </span>
        </Link>
      </div>
    </div>
  );
}

export default function Navbar() {
  const [open, setOpen] = useState(false);
  return (
    <nav className="flex filter drop-shadow-md px-4 py-4 h-20 items-center">
      <MobileNav open={open} setOpen={setOpen} />
      <div className="px-3 w-3/12 flex items-center">
        <Link href="/">
          <span className="text-2xl font-semibold">
             <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40"
                  fill="currentColor" className="bi bi-laptop" viewBox="0 0 16 16">
              <path d="M13.5 3a.5.5 0 0 1 .5.5V11H2V3.5a.5.5 0 0 1 .5-.5h11zm-11-1A1.5 1.5 0 0 0 1 3.5V12h14V3.5A1.5 1.5 0 0 0 13.5 2h-11zM0 12.5h16a1.5 1.5 0 0 1-1.5 1.5h-13A1.5 1.5 0 0 1 0 12.5z"/>
            </svg>
          </span>
        </Link>
      </div>
      <div className="w-9/12 flex justify-end items-center">
        <div
          className="z-50 flex relative w-8 h-8 flex-col justify-between items-center md:hidden"
          onClick={() => {
            setOpen(!open);
          }}
        >
          {/* hamburger button */}
          <span
            className={`h-1 w-full bg-white rounded-lg transform transition duration-300 ease-in-out ${
              open ? "rotate-45 translate-y-3.5 bg-black" : ""
            }`}
          />
          <span
            className={`h-1 w-full bg-white rounded-lg transition-all duration-300 ease-in-out ${
              open ? "w-0 bg-black" : "w-full"
            }`}
          />
          <span
            className={`h-1 w-full bg-white rounded-lg transform transition duration-300 ease-in-out ${
              open ? "-rotate-45 -translate-y-3.5 bg-black" : ""
            }`}
          />
        </div>

        <div className="hidden md:flex">
          <NavLink to="/">Home</NavLink>
          <NavLink to="/warnet">Sesi Warnet Aktif</NavLink>
          <NavLink to="/cafe">Cafe</NavLink>
          <NavLink to="/invoice">Invoice</NavLink>
        </div>
      </div>
    </nav>
  );
}
