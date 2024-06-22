import Navbar from '@/components/Navbar/Navbar'
import SessionDetail from "@/components/Session/SessionDetail";
import {FilterBar} from "@/components/Session/FilterBar";
import {useRouter} from "next/router";

export default function App() {
    const router = useRouter();
    const { id } = router.query;
    return (
        <div className="h-screen bg-black text-white">
            <Navbar/>
            <h2 className="text-center font-bold text-3xl mb-2 pb-2 pt-5 ">Detail Session</h2>
            <SessionDetail id={id}/>
        </div>
    );
}