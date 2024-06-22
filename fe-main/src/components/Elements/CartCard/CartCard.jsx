import React from "react";
import { useEffect, useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";

const CartCard = ({ orderParam, quantity, incrementOrder, decrementOrder, deleteOrder }) => {
  const [menuData, setMenuData] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchMenuData = async () => {
      try {
        const response = await axios.get(`/api/cafe/menu/id/${orderParam.menuItem}`);
        setMenuData(response.data);
        setIsLoading(false);
      } catch (error) {
        toast.error(`Error occured! ${error.response.data.message}. Please refresh the page`);
        deleteOrder(orderParam.menuItem);
      }
    };
    fetchMenuData();
  }, [orderParam.menuItem]);

  if (isLoading) {
    return <div></div>;
  }

  if( menuData.stock === 0 ){
    deleteOrder(orderParam.menuItem);
  }

  return (
    <div className="flex flex-col shadow-md shadow-indigo-300 hover:shadow-lg rounded-md p-4 flex-grow w-full outline outline-2 outline-offset-2">
      <h2 className="text-xl font-bold text-indigo-600">{menuData.name}</h2>
      <p className="">
        {menuData.price.toLocaleString("id-ID", {
          style: "currency",
          currency: "IDR",
        })}
      </p>
      <h5 className="mb-3">Stock : {menuData.stock}</h5>
        <div className="flex justify-end gap-5">
        <p className="text-indigo-600 font-semibold">Quantity :</p>
          <div className="flex gap-5 justify-center items-center flex-row text-xl font-semibold">
          <button
            className="border border-red-500 rounded-full text-2xl font-normal w-7 h-7 flex items-center justify-center"
            onClick={() => decrementOrder(orderParam.menuItem)}
          >
            -
          </button>
          {quantity}
          <button
            className="border border-blue-500 rounded-full text-2xl font-normal w-7 h-7 flex items-center justify-center"
            onClick={() => incrementOrder(orderParam.menuItem, menuData.stock)}
          >
            +
          </button>
          </div>
        </div>
    </div>
  );
};

export default CartCard;
