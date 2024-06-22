import CreatePC from '@/components/PC/DisplayPCCards'
import Navbar from '@/components/Navbar/Navbar'
import React from 'react'
import { useRouter } from 'next/router';
import EditPCCard from '@/components/PC/EditPCCard';

const Index = () => {
  const router = useRouter();
  const { id } = router.query;
  return (
    <>
    <div className="font-sans h-screen bg-black text-white">
      <Navbar/>
      <EditPCCard
        id={id}
      />
    </div>
    </>
  )
}

export default Index