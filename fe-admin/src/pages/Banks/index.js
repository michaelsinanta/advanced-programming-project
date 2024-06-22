import Navbar from "@/components/Navbar/Navbar";
import Banks from "@/components/Banks/DisplayBanks.js";
import React from "react";

const index = () => {
  return (
    <>
      <div className="font-sans">
        <Navbar />
        <Banks />
      </div>
    </>
  );
};

export default index;
