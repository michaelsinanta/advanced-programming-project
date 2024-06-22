import Navbar from "@/components/Navbar/Navbar";
import Coupons from "@/components/Coupons/DisplayCoupons.js";
import React from "react";

const index = () => {
    return (
        <>
            <div className="font-sans">
                <Navbar />
                <Coupons />
            </div>
        </>
    );
};

export default index;