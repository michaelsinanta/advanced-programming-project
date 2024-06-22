import { numberToRupiah } from "@/utils/numberToRupiah";
import axios from "axios";
import React, { useState } from "react";
import { toast } from "react-toastify";

const UpdateOrderCards = ({
  name,
  price,
  quantity,
  noPc,
  noRuangan,
  orderID,
  sessionID,
  status,
}) => {
  const [loading, setLoading] = useState(false);
  const [curr, setCurr] = useState(status);

  let bgColor;
  if (curr === "Menunggu Konfirmasi") {
    bgColor = "bg-yellow-100";
  } else if (curr === "Sedang Disiapkan") {
    bgColor = "bg-yellow-200";
  } else if (curr === "Sedang Diantar") {
    bgColor = "bg-yellow-300";
  } else if (curr === "Selesai") {
    bgColor = "bg-green-100";
  } else if (curr === "Dibatalkan") {
    bgColor = "bg-red-200";
  } else {
    bgColor = "bg-white";
  }

  const renderButton = (selectedOption) => {
    switch (selectedOption) {
      case "Menunggu Konfirmasi":
        return "Konfirmasi";
      case "Sedang Disiapkan":
        return "Antar";
      case "Sedang Diantar":
        return "Selesai";
    }
  };

  const updateState = (selectedOption) => {
    switch (selectedOption) {
      case "Menunggu Konfirmasi":
        return "prepare";
      case "Sedang Disiapkan":
        return "deliver";
      case "Sedang Diantar":
        return "done";
    }
  };

  const nextState = (curr) => {
    switch (curr) {
      case "Menunggu Konfirmasi":
        return "Sedang Disiapkan";
      case "Sedang Disiapkan":
        return "Sedang Diantar";
      case "Sedang Diantar":
        return "Selesai";
    }
  };

  return (
    <div className={`text-black rounded-lg shadow-md p-4 ${bgColor}`}>
      <h5 className="text-lg font-semibold mb-2">{name}</h5>
      <p className="text-gray-600">Price: {numberToRupiah(price)}</p>
      <p className="text-gray-600">Quantity: {quantity}</p>
      <p className="text-gray-600">Order ID: {orderID}</p>
      <p className="text-gray-600">Session ID: {sessionID}</p>
      <p className="text-gray-600">Nomor PC: {noPc}</p>
      <p className="text-gray-600">Nomor Ruangan: {noRuangan}</p>
      <div className="mt-3">{curr}</div>

      {curr != "Selesai" && curr != "Dibatalkan" && (
        <button
          className="mt-5 bg-blue-500 px-2 py-1 rounded-sm"
          disabled={loading}
          onClick={async () => {
            setLoading(true);
            await axios
              .put(
                `/api/cafe/order/update/${orderID}?status=${updateState(curr)}`
              )
              .then(() => {
                setCurr(nextState(curr));
                toast.success(`Order status updated into ${nextState(curr)}`);
              });
            setLoading(false);
          }}
        >
          {renderButton(curr)}
        </button>
      )}
    </div>
  );
};

export default UpdateOrderCards;
