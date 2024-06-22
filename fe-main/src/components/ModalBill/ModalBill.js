import React, { useEffect, useState } from "react";
import ReactDOM from "react-dom";
import styles from "../../styles/Modal.module.css";
import { SlClose } from "react-icons/sl";

const Modal = ({ show, onClose, bill }) => {
  const [isBrowser, setIsBrowser] = useState(false);

  useEffect(() => {
    setIsBrowser(true);
  }, []);

  const handleCloseClick = (e) => {
    e.preventDefault();
    onClose();
  };

  const modalContent = show ? (
    <div className={styles.overlay}>
      <div className={styles.modal}>
        <div className="flex justify-end text-2xl pb-4">
          <a href="#" onClick={handleCloseClick}>
            <SlClose size={25} color="blue" />
          </a>
        </div>
        <div className="w-full mx-auto rounded-lg bg-white border border-gray-200 text-gray-800 font-light mb-6">
          <div className="w-full p-3">
            <div>
              <table className="min-w-full">
                <thead className="bg-gray-200 border-b">
                  <tr>
                    <th
                      scope="col"
                      className="text-sm font-semibold text-gray-900 px-6 py-4 text-left"
                    >
                      Name
                    </th>
                    <th
                      scope="col"
                      className="text-sm font-semibold text-gray-900 px-6 py-4 text-left"
                    >
                      Quantity
                    </th>
                    <th
                      scope="col"
                      className="text-sm font-semibold text-gray-900 px-6 py-4 text-left"
                    >
                      Price/Item
                    </th>
                    <th
                      scope="col"
                      className="text-sm font-semibold text-gray-900 px-6 py-4 text-left"
                    >
                      Total
                    </th>
                  </tr>
                </thead>
                <tbody>
                  <tr className="bg-white border-b transition duration-300 ease-in-out hover:bg-gray-100">
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                      {bill.name}
                    </td>
                    <td className="text-sm text-gray-900 font-light px-6 py-4 whitespace-nowrap">
                      {bill.quantity}
                    </td>
                    <td className="text-sm text-gray-900 font-light px-6 py-4 whitespace-nowrap">
                      {bill.price}
                    </td>
                    <td className="text-sm text-gray-900 font-light px-6 py-4 whitespace-nowrap">
                      {bill.subTotal}
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  ) : null;

  if (isBrowser) {
    return ReactDOM.createPortal(
      modalContent,
      document.getElementById("modal-root")
    );
  } else {
    return null;
  }
};
export default Modal;
