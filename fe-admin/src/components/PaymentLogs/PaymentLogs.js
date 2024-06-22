import React, { useState, useEffect } from "react";
import PaymentLogsRow from "./PaymentLogsRow";
import FilterModal from "./FilterModal";
import { createPopper } from "@popperjs/core";
import TowerLoader from "../TowerLoader/TowerLoader";

const baseUrl = "/api/bayar/log/paymentLog/";

const PaymentLogs = () => {
  const [isLoading, setIsLoading] = useState(true);
  const [paymentLogs, setPaymentLogs] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [dropdownPopoverShow, setDropdownPopoverShow] = useState(false);
  const [filterBy, setFilterBy] = useState("");
  const [formData, setFormData] = useState({
    year: "",
    month: "",
    week: "",
  });

  const btnDropdownRef = React.createRef();
  const popoverDropdownRef = React.createRef();

  const openDropdownPopover = () => {
    createPopper(btnDropdownRef.current, popoverDropdownRef.current, {
      placement: "bottom-start",
    });
    setDropdownPopoverShow(true);
  };

  const closeDropdownPopover = () => {
    setDropdownPopoverShow(false);
  };

  useEffect(() => {
    const fetchData = async () => {
      const response = await fetch(baseUrl, {
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
        },
      });
      if (!response.ok) {
        const message = `An error has occured: ${response.status}`;
        throw new Error(message);
      }

      const data = await response.json();
      setPaymentLogs(data.content);
      setIsLoading(false);
    };

    fetchData();
  }, []);

  const submitForm = (e) => {
    e.preventDefault();
    setShowModal(false);

    let getPaymentLogURL = baseUrl;

    if (filterBy === "yearly") {
      getPaymentLogURL += formData.year;
    }

    if (filterBy === "monthly") {
      getPaymentLogURL += "monthly/" + formData.year + "/" + formData.month;
    }

    if (filterBy === "weekly") {
      getPaymentLogURL += "weekly/" + formData.year + "/" + formData.week;
    }

    fetch(getPaymentLogURL, {
      method: "GET",
      headers: {
        accept: "application/json",
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setPaymentLogs(data.content);
        setFormData({});
      });
  };

  const handleInput = (e) => {
    const fieldName = e.target.name;
    const fieldValue = e.target.value;

    setFormData((prevState) => ({
      ...prevState,
      [fieldName]: fieldValue,
    }));
  };

  if (isLoading) {
    return (
      <>
        <div className="items-center m-8">
          <h1 className="text-4xl text-center my-8 font-bold">Payment Log</h1>
        </div>
        <div className="flex justify-center items-center h-full mt-32">
          <TowerLoader />
        </div>
      </>
    );
  }

  return (
    <>
      {showModal ? (
        <FilterModal
          setShowModal={setShowModal}
          filterBy={filterBy}
          submitForm={submitForm}
          handleInput={handleInput}
          formData={formData}
        />
      ) : null}

      <div className="items-center m-8">
        <h1 className="text-4xl text-center my-8 font-bold">Payment Log</h1>
      </div>
      <div className="flex flex-col gap-y-2">
        <div className="flex justify-end px-64">
          <button
            className="flex flex-row bg-blue-700 hover:bg-blue-800 text-white font-bold py-2 px-4 border border-blue-700 rounded transition-all duration-150"
            type="button"
            ref={btnDropdownRef}
            onClick={() => {
              dropdownPopoverShow
                ? closeDropdownPopover()
                : openDropdownPopover();
            }}
          >
            Filter
            <span>
              <svg
                className="flex justify-end w-4 ml-4 py-1"
                aria-hidden="true"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth="2"
                  d="M19 9l-7 7-7-7"
                ></path>
              </svg>
            </span>
          </button>
          <div
            ref={popoverDropdownRef}
            className={
              (dropdownPopoverShow ? "block " : "hidden ") +
              "dark:bg-gray-700 dark:text-gray-400 text-base z-50 float-left py-2 list-none text-left rounded shadow-lg mt-1 min-w-48"
            }
          >
            <a
              href="#pablo"
              className="text-sm py-2 px-4 font-normal block w-full whitespace-no-wrap dark:bg-gray-700 dark:text-gray-400 text-white"
              onClick={() => {
                setShowModal(true);
                closeDropdownPopover();
                setFilterBy("yearly");
              }}
            >
              Yearly
            </a>
            <a
              href="#pablo"
              className="text-sm py-2 px-4 font-normal block w-full whitespace-no-wrap dark:bg-gray-700 dark:text-gray-400 text-white"
              onClick={() => {
                setShowModal(true);
                closeDropdownPopover();
                setFilterBy("monthly");
              }}
            >
              Monthly
            </a>
            <a
              href="#pablo"
              className="text-sm py-2 px-4 font-normal block w-full whitespace-no-wrap dark:bg-gray-700 dark:text-gray-400 text-white"
              onClick={() => {
                setShowModal(true);
                closeDropdownPopover();
                setFilterBy("weekly");
              }}
            >
              Weekly
            </a>
          </div>
        </div>
        <div className="relative overflow-x-auto shadow-md sm:rounded-lg px-8">
          <table className="w-full text-sm text-left text-gray-500 dark:text-gray-400">
            <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
              <tr>
                <th scope="col" className="px-6 py-3">
                  <div className="flex items-center">No PC</div>
                </th>
                <th scope="col" className="px-6 py-3">
                  <div className="flex items-center">Date</div>
                </th>
                <th scope="col" className="px-6 py-3">
                  <div className="flex items-center">Payment Method</div>
                </th>
                <th scope="col" className="px-6 py-3">
                  <div className="flex items-center">Total Price</div>
                </th>
                <th scope="col" className="px-6 py-3">
                  <span className="sr-only">Detail</span>
                </th>
              </tr>
            </thead>
            <tbody>
              {paymentLogs.map((paymentLog) => (
                <PaymentLogsRow data={paymentLog} key={paymentLog.id} />
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </>
  );
};

export default PaymentLogs;
