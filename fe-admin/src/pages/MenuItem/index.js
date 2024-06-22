import CreateMenuItem from '@/components/DisplayMenuCards/DisplayMenuCards'
import Navbar from '@/components/Navbar/Navbar'
import React from 'react'

const index = () => {
  return (
    <>
    <div className="font-sans">
      <Navbar/>
      <CreateMenuItem/>
    </div>
    </>
  )
}

export default index