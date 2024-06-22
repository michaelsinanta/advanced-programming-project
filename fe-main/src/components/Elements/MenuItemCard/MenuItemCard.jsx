import { useOrderContext } from "@/components/contexts/OrderContext";
import React from "react";
import { AiOutlineShoppingCart } from "react-icons/ai";

const MenuItemCard = ({ menu }) => {
  const { addOrder } = useOrderContext();
  return (
    <div className="flex flex-col shadow-md shadow-indigo-300 hover:shadow-lg rounded-md p-4 flex-grow w-full outline outline-2 outline-offset-2">
      <h2 className="text-xl font-bold text-indigo-600">{menu.name}</h2>
      <p className="">
        {menu.price.toLocaleString("id-ID", {
          style: "currency",
          currency: "IDR",
        })}
      </p>
      <h5 className="mb-3">Stock : {menu.stock}</h5>
      <button
        className="flex justify-end "
        onClick={() => {
          addOrder(menu);
        }}
      >
        <div className="flex gap-1 p-2 rounded-md bg-indigo-50  hover:shadow-indigo-700 hover:shadow-md transition-all duration-100 ease-in-out">
          <h1>Add to Cart</h1>
          <AiOutlineShoppingCart size={25} color="blue" />
        </div>
      </button>
    </div>
  );
};

export default MenuItemCard;
