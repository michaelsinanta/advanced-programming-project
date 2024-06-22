import React, { useState } from "react";
import Image from "next/image";
import EditCouponForm from "./EditCouponForm";
import { numberToRupiah } from "@/utils/numberToRupiah";

const CouponCard = ({
  coupon,
  removeCoupon,
  newCoupon,
  handleEditCoupon,
  handleCouponChange,
}) => {
  const [isHovered, setIsHovered] = useState(false);
  const [showModalEdit, setShowModalEdit] = useState(false);

  const handleEdit = () => {
    setShowModalEdit(false);
    handleEditCoupon();
  };

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
        <h5 className="text-xl font-bold">{coupon.name}</h5>
        <p className="text-md mt-2">Discount :</p>
        <p className="text-lg font-bold mt-2">{numberToRupiah(coupon.discount)}</p>
        <div className="relative h-16">
          <div className="absolute bottom-0 left-0">
            <button
              className="btn btn-danger mt-4 rounded"
              onClick={() => removeCoupon(coupon)}
            >
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
            <button className="m-1" onClick={() => setShowModalEdit(true)}>
              Edit
            </button>
            {showModalEdit && (
              <EditCouponForm
                setShowModalEdit={setShowModalEdit}
                coupon={coupon}
                newCoupon={newCoupon}
                handleEditCoupon={handleEdit}
                handleCouponChange={handleCouponChange}
              />
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default CouponCard;
