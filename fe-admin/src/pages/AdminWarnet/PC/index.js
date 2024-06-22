import CreatePC from '@/components/PC/DisplayPCCards'
import Navbar from '@/components/Navbar/Navbar'
import React from 'react'

const index = () => {
  return (
    <>
    <div className="font-sans bg-black text-white">
      <Navbar/>
      <CreatePC/>
    </div>
    </>
  )
}

export default index