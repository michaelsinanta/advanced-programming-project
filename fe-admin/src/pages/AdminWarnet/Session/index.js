import Navbar from '@/components/Navbar/Navbar'
import AllSessionTable from "@/components/Session/AllSessionTable";
import {FilterBar} from "@/components/Session/FilterBar";

export default function App() {

    return (
        <div className="h-screen bg-black text-white">
            <Navbar/>
            <div>
                <h2 className="text-center font-bold text-3xl mb-2 pt-4">Session Log</h2>
                <AllSessionTable/>
            </div>
        </div>
    );
}