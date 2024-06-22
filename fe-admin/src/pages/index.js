import { Inter } from 'next/font/google'
import Navbar from '@/components/Navbar/Navbar'
import HomeBanner from '@/components/HomeBanner/HomeBanner'

const inter = Inter({ subsets: ['latin'] })

export default function Home() {
  return (
    <>
      <div className="font-sans">
        <Navbar/>
        <HomeBanner />
      </div>
    </>
  )
}
