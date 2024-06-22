import { useState, useEffect } from "react";
import Bill from "./Bill";
import PaymentLogDetailHeader from "./PaymentLogDetailHeader";
import PaymentLogDetailFooter from "./PaymentLogDetailFooter";
import TowerLoader from "../TowerLoader/TowerLoader";
import Link from "next/link";

const baseUrl = "/api/bayar/log/paymentLog/detail";

const PaymentLogDetail = ({ sessionId }) => {
  const [isLoading, setIsLoading] = useState(true);
  const [data, setData] = useState({});

  useEffect(() => {
    const fetchData = async () => {
      const response = await fetch(`${baseUrl}/${sessionId}`, {
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
        },
      });

      if (!response.ok) {
        const message = `An error has occured: ${response.status}`;
        throw new Error(message);
      }

      const dataResponse = await response.json();
      setData(dataResponse.content);
      setIsLoading(false);
    };

    fetchData();
  }, []);

  if (isLoading) {
    return (
      <div className="items-center">
        <h1 className="text-2xl text-center font-bold">Payment Log Detail</h1>
        <div className="flex justify-center items-center h-full mt-32">
          <TowerLoader />
        </div>
      </div>
    );
  }

  return (
    <div className="items-center">
      <div className="max-w-5xl mx-auto bg-gray-400 border-gray-200 rounded-lg shadow dark:bg-gray-800 dark:border-gray-700">
        <div className="flex justify-end">
          <Link href="/PaymentLog">
            <button
              type="button"
              className="m-2 bg-gray-600 rounded-md p-2 text-gray-900 hover:text-gray-500 hover:bg-gray-600 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-indigo-500"
            >
              <svg
                className="h-6 w-6"
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
                aria-hidden="true"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth="2"
                  d="M6 18L18 6M6 6l12 12"
                />
              </svg>
            </button>
          </Link>
        </div>

        <div className="border-gray-200 rounded-b-md">
          <div className="p-9">
            <div className="text-gray-900 dark:text-white">
              <p className="text-xl font-extrabold tracking-tight uppercase font-body">
                Payment Log Detail Id : {data.paymentLog.id}
              </p>
              <p className="font-normal text-gray-700 dark:text-gray-400">
                Date: {data.paymentLog.createdAt}
              </p>
            </div>
          </div>
          <PaymentLogDetailHeader data={data} />

          <div className="p-4">
            <div className="flex flex-col mx-0">
              <table className="min-w-full divsessionIde-y divsessionIde-slate-500">
                <thead>
                  <tr>
                    <th
                      scope="col"
                      className="py-3.5 pl-4 pr-3 text-left text-sm font-normal text-gray-900 dark:text-white sm:pl-6 md:pl-0"
                    >
                      Name
                    </th>
                    <th
                      scope="col"
                      className="hsessionIdden py-3.5 px-3 text-right text-sm font-normal text-gray-900 dark:text-white sm:table-cell"
                    >
                      Price
                    </th>
                    <th
                      scope="col"
                      className="hsessionIdden py-3.5 px-3 text-right text-sm font-normal text-gray-900 dark:text-white sm:table-cell"
                    >
                      Quantity
                    </th>
                    <th
                      scope="col"
                      className="py-3.5 pl-3 pr-4 text-right text-sm font-normal text-gray-900 dark:text-white sm:pr-6 md:pr-0"
                    >
                      Amount
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {data.invoice.bills.map((bill) => (
                    <Bill data={bill} key={bill.id} />
                  ))}
                </tbody>
                <tfoot>
                  <PaymentLogDetailFooter data={data} />
                </tfoot>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PaymentLogDetail;
