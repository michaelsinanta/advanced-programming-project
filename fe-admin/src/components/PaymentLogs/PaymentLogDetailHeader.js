const PaymentLogDetailHeader = ({ data }) => {
  return (
    <div className="p-7">
      <div className="flex w-full">
        <div className="grid grid-cols-4 gap-10">
          <div className="text-sm font-light text-gray-900 dark:text-white">
            <p className="text-sm font-normal text-gray-900 dark:text-white">
              Billed To Session Id:
            </p>
            <p>{data.invoice.sessionId}</p>
          </div>

          <div className="text-sm font-light text-gray-900 dark:text-white">
            <p className="text-sm font-normal text-gray-900 dark:text-white">
              Invoice Number
            </p>
            <p>{data.invoice.id}</p>
            <p className="mt-2 text-sm font-normal text-gray-900 dark:text-white">
              Date of Issue
            </p>
            <p>{data.invoice.createdAt}</p>
          </div>

          <div className="text-sm font-light text-gray-900 dark:text-white">
            <p className="text-sm font-normal text-gray-900 dark:text-white">
              Payment Status
            </p>
            <p>{data.invoice.paymentStatus}</p>

            <p className="mt-2 text-sm font-normal text-gray-900 dark:text-white">
              Payment Method
            </p>
            <p>{data.invoice.paymentMethod}</p>
          </div>

          {data.bank && (
            <div className="text-sm font-light text-gray-900 dark:text-white">
              <p className="text-sm font-normal text-gray-900 dark:text-white">
                Bank Id
              </p>
              <p>{data.bank.id}</p>
              <p className="mt-2 text-sm font-normal text-gray-900 dark:text-white">
                Name
              </p>
              <p>{data.bank.name}</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default PaymentLogDetailHeader;
