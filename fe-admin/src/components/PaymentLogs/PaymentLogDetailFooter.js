import { formatRupiah } from "./formatRupiah";

const PaymentLogDetailFooter = ({ data }) => {
  return (
    <>
      <tr>
        <th
          scope="row"
          colSpan="3"
          className="hidden pt-6 pl-6 pr-3 text-sm font-light text-right text-gray-900 dark:text-white sm:table-cell md:pl-0"
        >
          Subtotal
        </th>
        <th
          scope="row"
          className="pt-6 pl-4 pr-3 text-sm font-light text-left text-gray-900 dark:text-white sm:hidden"
        >
          Subtotal
        </th>
        <td className="pt-6 pl-3 pr-4 text-sm text-right text-gray-900 dark:text-white sm:pr-6 md:pr-0">
          {formatRupiah(data.invoice.totalAmount.toString(), "Rp")}
        </td>
      </tr>
      <tr>
        <th
          scope="row"
          colSpan="3"
          className="hidden pt-6 pl-6 pr-3 text-sm font-light text-right text-gray-900 dark:text-white sm:table-cell md:pl-0"
        >
          Discount
        </th>
        <th
          scope="row"
          className="pt-6 pl-4 pr-3 text-sm font-light text-left text-gray-900 dark:text-white sm:hidden"
        >
          Discount
        </th>
        <td className="pt-6 pl-3 pr-4 text-sm text-right text-gray-900 dark:text-white sm:pr-6 md:pr-0">
          {formatRupiah(data.invoice.discount.toString(), "Rp")}
        </td>
      </tr>

      {data.bank && (
        <tr>
          <th
            scope="row"
            colSpan="3"
            className="hidden pt-4 pl-6 pr-3 text-sm font-light text-right text-gray-900 dark:text-white sm:table-cell md:pl-0"
          >
            Admin Fee
          </th>
          <th
            scope="row"
            className="pt-4 pl-4 pr-3 text-sm font-light text-left text-gray-900 dark:text-white sm:hidden"
          >
            Admin Fee
          </th>
          <td className="pt-4 pl-3 pr-4 text-sm text-right text-gray-900 dark:text-white sm:pr-6 md:pr-0">
            {formatRupiah(data.bank.adminFee.toString(), "Rp")}
          </td>
        </tr>
      )}

      <tr>
        <th
          scope="row"
          colSpan="3"
          className="hidden pt-4 pl-6 pr-3 text-sm font-normal text-right text-gray-900 dark:text-white sm:table-cell md:pl-0"
        >
          Total
        </th>
        <th
          scope="row"
          className="pt-4 pl-4 pr-3 text-sm font-normal text-left text-gray-900 dark:text-white sm:hidden"
        >
          Total
        </th>
        <td className="pt-4 pl-3 pr-4 text-sm font-normal text-right text-gray-900 dark:text-white sm:pr-6 md:pr-0">
          {formatRupiah(data.paymentLog.totalAmount.toString(), "Rp")}
        </td>
      </tr>
    </>
  );
};

export default PaymentLogDetailFooter;
