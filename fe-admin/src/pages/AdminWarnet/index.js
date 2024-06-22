import Navbar from '@/components/Navbar/Navbar'
import {Typewriter} from "@/components/HomeBanner/Typewriter";
import {Button, Link} from "@nextui-org/react";
import {useRouter} from "next/router";

export default function App() {
    const router = useRouter();

    return (
        <div className="h-screen bg-black text-white">
            <Navbar/>
            <div className="flex flex-col items-center justify-center py-16">
                <Typewriter text="HELLO, ADMIN WARNET" />
            </div>
            <div className="py-10 flex flex-col items-center justify-center space-y-10">
                <Button color="warning" size="xl" onPress={() => router.push("/AdminWarnet/PC")} ghost>
                    Kelola PC
                </Button>
                <Button color="warning" size="xl" onPress={() => router.push("/AdminWarnet/Session")} ghost>
                    Session Log
                </Button>
            </div>
        </div>
    );
}