import React from "react";

const OrderCard = ({
  orderParam,
  quantity,
  incrementOrder,
  decrementOrder,
}) => {
  return (
    <div className="flex flex-col shadow-md shadow-indigo-300 hover:shadow-lg rounded-md p-4 flex-grow w-full outline outline-2 outline-offset-2">
      <h2 className="text-xl font-bold text-indigo-600">{orderParam.name}</h2>
      <p>quantity: {quantity}</p>
      <p>status: menunggu</p>
      <div className="w-full flex justify-end">
        <button className="shadow-md shadow-indigo-300 hover:shadow-lg rounded-md px-3 outline outline-2 outline-offset-2">
          Batalkan
        </button>
      </div>
    </div>
  );
};

export default OrderCard;
