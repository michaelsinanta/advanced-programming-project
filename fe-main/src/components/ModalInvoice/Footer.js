import { formatRupiah } from "../util/formatRupiah";

const Footer = ({ radioValue, invoiceItems, optionAdminFee }) => {
  return (
    <>
      {radioValue === "BANK" && (
        <div className="w-full mx-auto rounded-lg bg-white border border-gray-00 text-gray-800 font-light mb-6">
          <div className="w-full p-3 border-b border-gray-200">
            <div className="mb-2">
              <table>
                <tbody>
                  <tr>
                    <td className="text-l font-bold text-indigo-600 ml-3">
                      Subtotal
                    </td>
                  </tr>
                  <tr>
                    <td>{formatRupiah(invoiceItems.totalAmount)}</td>
                  </tr>
                  {optionAdminFee && (
                    <>
                      <tr>
                        <td className="text-l font-bold text-indigo-600 ml-3">
                          Admin Fee
                        </td>
                      </tr>
                      <tr>
                        <td>{formatRupiah(optionAdminFee)}</td>
                      </tr>
                      <tr>
                        <td className="text-l font-bold text-indigo-600 ml-3">
                          Total
                        </td>
                      </tr>
                      <tr>
                        <td>
                          {formatRupiah(
                            invoiceItems.totalAmount + optionAdminFee
                          )}
                        </td>
                      </tr>
                    </>
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}
      {radioValue === "CASH" && (
        <div className="w-full mx-auto rounded-lg bg-white border border-gray-200 text-gray-800 font-light mb-6">
          <div className="w-full p-3 border-b border-gray-200">
            <div className="mb-2">
              <table>
                <tbody>
                  <tr>
                    <td className="text-l font-bold text-indigo-600 ml-3">
                      Subtotal
                    </td>
                  </tr>
                  <tr>
                    <td>{formatRupiah(invoiceItems.totalAmount)}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}
      {invoiceItems.discount != 0 && (
        <div className="w-full p-3">
          <div
            className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded relative"
            role="alert"
          >
            <strong className="font-bold">Discount Applied!</strong>
            <span className="block sm:inline">
              {" "}
              You got {formatRupiah(invoiceItems.discount)} discount!
            </span>
          </div>
        </div>
      )}
    </>
  );
};

export default Footer;
