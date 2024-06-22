import axios from 'axios'
import React, { createContext, useContext, useState } from 'react'

export const SessionInfoContext = createContext()

export const SessionInfoProvider = ({ children }) => {
  const [sessionInfo, setSessionInfo] = useState({})

  const updateSessionInfo = (info) => {
    axios.get(`/api/warnet/sewa_pc/get_session/${info.id}`)
    .then((response)=>{
        setSessionInfo({ ...sessionInfo, ...info, ...response.data })
    })
  }
  
  return (
    <SessionInfoContext.Provider value={{ sessionInfo, updateSessionInfo }}>
      {children}
    </SessionInfoContext.Provider>
  )
}

export const useSessionContext = () => useContext(SessionInfoContext);