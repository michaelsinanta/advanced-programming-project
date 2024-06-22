import { formatRupiah } from "./formatRupiah";

const Bill = ({ data }) => {
  return (
    <>
      <tr className="border-b border-slate-200">
        <td className="py-4 pl-4 pr-3 text-sm sm:pl-6 md:pl-0">
          <div className="font-medium text-gray-900 dark:text-white">
            {data.name}
          </div>
        </td>
        <td className="hidden px-3 py-4 text-sm text-right text-gray-900 dark:text-white sm:table-cell">
          {formatRupiah(data.price.toString(), "Rp")}
        </td>
        <td className="hidden px-3 py-4 text-sm text-right text-gray-900 dark:text-white sm:table-cell">
          {data.quantity}
        </td>
        <td className="py-4 pl-3 pr-4 text-sm text-right text-gray-900 dark:text-white sm:pr-6 md:pr-0">
          {formatRupiah(data.subTotal.toString(), "Rp")}
        </td>
      </tr>
    </>
  );
};

export default Bill;
