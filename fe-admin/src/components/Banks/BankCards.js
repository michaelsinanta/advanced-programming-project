import React, {useEffect, useState} from "react";
import Image from "next/image";
import EditBankForm from "./EditBankForm";
import { numberToRupiah } from "@/utils/numberToRupiah";

const BankCards = ({ bank, showModal, setShowModal, removeBank, editBank }) => {
  const [isHovered, setIsHovered] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  
  useEffect(() => {
    if (!showModal) {
      setIsEditing(false)
    }
  }, [showModal]);

  const handleMouseOver = () => {
    setIsHovered(true);
  };

  const handleMouseOut = () => {
    setIsHovered(false);
  };

  const imagePath = isHovered
    ? "/static/icons/trashHover.gif"
    : "/static/icons/trash.svg";

  return (
    <div className="bg-gradient-to-r from-pink-500 via-red-500 to-yellow-500 text-offwhite rounded p-4 flex flex-col flex-grow">
      <div className="bg-blue-800 mt-2 p-4 rounded-lg">
        <h5 className="text-xl font-bold">{bank.name}</h5>
        <p className="text-md mt-2">Admin Fee:</p>
        <p className="text-lg font-bold mt-2">{numberToRupiah(bank.adminFee)}</p>
        <div className="relative h-16">
          <div className="absolute bottom-0 left-0">
            <button className="btn btn-danger mt-4 rounded" onClick={() => removeBank(bank)}>
              <Image
                src={imagePath}
                alt="trash icon"
                width={30}
                height={30}
                unoptimized
                className="mx-auto rounded-full"
                onMouseOver={handleMouseOver}
                onMouseOut={handleMouseOut}
              />
            </button>
          </div>
          <div className="absolute bottom-0 right-0">
            <button className="m-1" onClick={() => {setIsEditing(true); setShowModal(true)}}>
              Edit
            </button>
            {showModal && isEditing && (
              <EditBankForm
                setShowModal={setShowModal}
                bank={bank}
                editBank={editBank}
              />
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default BankCards;
