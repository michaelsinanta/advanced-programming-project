import React, {useState, useEffect} from "react";
import axios from "axios";
import { numberToRupiah } from "@/utils/numberToRupiah";

const EditBankForm = ({ sessionId, noPC, setShowModal, giveDiscount }) => {
  const [discountType, setDiscountType] = useState([]);
  const [discount, setDiscount] = useState(0);
  const [invoiceData, setInvoiceData] = useState(0);
  const [discountGiven, setDiscountGiven] = useState(0);
  const [totalPrice, setTotalPrice] = useState(0);

  useEffect(() => {
    axios
      .get(`/api/bayar/invoices/${sessionId}`)
      .then((response) => {
        setInvoiceData(response.data?.content??0);
        setDiscountGiven(response.data?.content?.discount??0);
        setTotalPrice(response.data?.content?.totalAmount??0);
      });
  }, []);

  const handleGiveDiscount = () => {

    let discountFinal = 0;

    if (discountType == "") {
      alert("Select a discount type");
      return;
    } else {
      if (discountType == "NOMINAL") {
        if (discount < 1000) {
          alert("Discount cannot be under Rp1.000");
          return;
        }
        else if (discount > (totalPrice - discountGiven)) {
          alert("Discount cannot be larger than the final total");
          return;
        }
        else {
          discountFinal = discount
        }
      } else {
        if (discount > 100) {
          alert("Discount cannot be above 100%")
          return
        } 
        else if (discount < 0) {
          alert("Discount cannot be negative");
          return;
        }
        else {
          discountFinal = discount
        }
      }
    }
    discountFinal = discount
    giveDiscount(sessionId, discountType, discountFinal)
  };

  return (
    <div className="fixed inset-0 z-10 overflow-y-auto">
      <div className="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div className="fixed inset-0 transition-opacity" aria-hidden="true">
          <div className="absolute inset-0 bg-gray-500 opacity-75"></div>
        </div>
        <span className="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>
        <div className="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
          <div className="bg-white text-black p-4 rounded text-center">
            <div className="bg-white rounded-lg shadow-lg px-6 py-8 max-w-md mx-auto mt-5">
              <h1 className="font-bold text-2xl text-center text-blue-600 mb-2">Kang Warnet</h1>
              <hr className="border-t-1 border-gray-500 mb-2" />
              <div className="flex justify-between mb-6">
                  <h1 className="text-lg font-bold">Invoice</h1>
                  <div className="text-gray-700">
                      <div>Date: {invoiceData?.createdAt??0}</div>
                      <div>Invoice #: {invoiceData?.id??0}</div>
                  </div>
              </div>
              <div class="mb-8">
                  <h2 className="text-lg font-bold mb-4">Details:</h2>
                  <div className="text-gray-700 mb-2">Session: {sessionId}</div>
                  <div className="text-gray-700 mb-2">PC Number: {noPC}</div>
              </div>
              <table className="w-full">
                  <thead>
                      <tr>
                          <th className="text-left font-bold text-gray-700">Description</th>
                          <th className="text-right font-bold text-gray-700">Amount</th>
                      </tr>
                  </thead>
                  <tbody>
                      <tr>
                          <td className="text-left text-gray-700">Status:</td>
                          <td className="text-right text-gray-700">{invoiceData?.paymentStatus??0}</td>
                      </tr>
                      <tr>
                          <td className="text-left text-gray-700">Total Amount:</td>
                          <td className="text-right text-gray-700">{numberToRupiah(invoiceData?.totalAmount??0)}</td>
                      </tr>
                      <tr>
                          <td className="text-left text-gray-700">Discount Given:</td>
                          <td className="text-right text-gray-700">{numberToRupiah(invoiceData?.discount??0)}</td>
                      </tr>
                  </tbody>
                  <tfoot>
                      <tr>
                          <td className="text-left font-bold text-gray-700">Final Total:</td>
                          <td className="text-right font-bold text-gray-700">{numberToRupiah(totalPrice - discountGiven)}</td>
                      </tr>
                  </tfoot>
              </table>
            </div>
            <div className="bg-white rounded-lg shadow-lg px-6 py-8 max-w-md mx-auto mt-5 mb-5">
              <h2 className="font-bold text-2xl text-center text-blue-600 mb-2 mt-5">
                Give Discount
              </h2>
              <hr className="border-t-1 border-gray-500" />
              <div className="mb-2 mt-4">
                <label className="font-bold mr-2" htmlFor="discountType">
                  Discount Type
                </label>
                <select className="bg-white border border-black rounded px-2" defaultValue="DEFAULT" onChange={e => {setDiscountType(e.target.value)}}>
                  <option disabled value="DEFAULT">Select</option>
                  <option
                    type="text"
                    id="discountType"
                    name="discountType"
                    value="NOMINAL"
                    className="bg-white border border-black rounded px-2"
                    placeholder="Nominal"
                    autoComplete="off"
                  >Nominal
                  </option>
                  <option
                    type="text"
                    id="discountType"
                    name="discountType"
                    value="PERCENTAGE"
                    className="bg-white border border-black rounded px-2"
                    placeholder="Percentage"
                    autoComplete="off"
                  >Percentage
                  </option>
                </select>
              </div>
              <div className="mb-2">
                <label className="font-bold mr-2" htmlFor="discount">
                  Discount
                </label>
                <input
                  type="number"
                  id="discount"
                  name="discount"
                  value={discount}
                  onChange={e => setDiscount(e.target.value)}
                  className="bg-white border border-black rounded px-2"
                  placeholder="Discount"
                  autoComplete="off"
                />
              </div>
              <div className="flex justify-center mt-8">
                <button
                  className="bg-blue-700 text-white p-2 font-bold mr-4 rounded-lg"
                  onClick={handleGiveDiscount}
                >
                  Submit
                </button>
                <button
                  className="bg-red-700 text-white p-2 font-bold rounded-lg"
                  onClick={() => setShowModal(false)}
                >
                  Cancel
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EditBankForm;
