const CouponCode = ({
  radioValue,
  couponCode,
  setCouponCode,
  handleCouponCode,
}) => {
  if (radioValue !== "") {
    return (
      <div className="w-full mx-auto rounded-lg bg-white border border-gray-200 text-gray-800 font-light mb-6">
        <div className="w-full p-3 border-b border-gray-200">
          <div className="mb-5">
            <label
              htmlFor="couponCode"
              className="text-gray-600 font-semibold text-sm mb-2 ml-1"
            >
              Coupon Code
            </label>
            <input
              type="text"
              placeholder="Enter Coupon Code"
              className="form-input w-full px-3 py-2 mb-1 border border-gray-200 rounded-md focus:outline-none focus:border-indigo-500 transition-colors"
              value={couponCode}
              onChange={(e) => setCouponCode(e.target.value)}
            />
          </div>
        </div>

        <div className="w-full p-3">
          <button
            onClick={handleCouponCode}
            type="button"
            className="bg-indigo-500 hover:bg-indigo-600 text-white font-bold py-2 px-4 w-full rounded focus:outline-none focus:shadow-outline"
          >
            Apply Coupon
          </button>
        </div>
      </div>
    );
  }
};

export default CouponCode;
