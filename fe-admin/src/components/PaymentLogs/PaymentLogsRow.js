import Link from "next/link";
import { formatRupiah } from "./formatRupiah";

const PaymentLogsRow = ({ data }) => {
  return (
    <tr className="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
      <th
        scope="row"
        className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white"
      >
        {data.noPC}
      </th>
      <td className="px-6 py-4">{data.createdAt}</td>
      <td className="px-6 py-4">{data.paymentMethod}</td>
      <td className="px-6 py-4">
        {formatRupiah(data.totalAmount.toString(), "Rp")}
      </td>
      <td className="px-6 py-4 text-right">
        <Link
          className="font-medium text-blue-600 dark:text-blue-500 hover:underline"
          href={`PaymentLog/${data.sessionId}`}
        >
          Detail
        </Link>
      </td>
    </tr>
  );
};

export default PaymentLogsRow;
