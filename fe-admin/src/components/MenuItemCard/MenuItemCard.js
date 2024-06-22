import React, { useState } from "react";
import Image from "next/image";
import { numberToRupiah } from "@/utils/numberToRupiah";

const MenuItemCard = ({ name, stock, price, onDelete, onUpdate }) => {
  const [isHovered, setIsHovered] = useState(false);

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
        <h5 className="text-xl font-bold">{name}</h5>
        <p className="text-md mt-2">Available Stock: {stock}</p>
        <p className="text-lg font-bold mt-4">{numberToRupiah(price)}</p>
        <div className="flex justify-between mt-4">
          <button className="btn btn-danger rounded" onClick={onDelete}>
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
          <button className="p-2 rounded-md bg-indigo-50 hover:bg-indigo-200 hover:shadow-md transition-all duration-100 ease-in-out" onClick={onUpdate}>
            Update
          </button>
        </div>
      </div>
    </div>
  );
};

export default MenuItemCard;
