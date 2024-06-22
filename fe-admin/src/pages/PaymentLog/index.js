import Navbar from "@/components/Navbar/Navbar";
import PaymentLogs from "@/components/PaymentLogs/PaymentLogs";
import React from "react";

const index = () => {
  return (
    <>
      <div className="font-sans">
        <Navbar />
        <PaymentLogs />
      </div>
    </>
  );
};

export default index;
