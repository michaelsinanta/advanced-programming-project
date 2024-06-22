import React from "react";
import { useState } from "react";
import { BiDetail } from "react-icons/bi";
import Modal from "@/components/ModalBill/ModalBill.js"

const BillItemCard = ({ bill }) => {
  const[showBillModal, setShowBillModal] = useState(false);
  return (
    <div className="flex flex-col shadow-md shadow-indigo-300 hover:shadow-lg rounded-md p-4 flex-grow w-full outline outline-2 outline-offset-2">
      <h2 className="text-xl font-bold text-indigo-600">{bill.name}</h2>
      <p className="">
        {bill.price.toLocaleString("id-ID", {
          style: "currency",
          currency: "IDR",
        })}
      </p>
      <h5 className="mb-3">Quantity : {bill.quantity}</h5>
      <button onClick={() => setShowBillModal(true)} className="flex justify-end ">
        <div className="flex gap-1 p-2 rounded-md bg-indigo-50  hover:shadow-indigo-700 hover:shadow-md transition-all duration-100 ease-in-out">
        <h1 className="text-l font-bold text-indigo-600">Details</h1>
        <BiDetail size={25} color="blue"/>
        </div>
      </button>
      <Modal
          onClose={() => setShowBillModal(false)}
          show={showBillModal}
          bill={bill}
      />
    </div>
  );
};

export default BillItemCard;
