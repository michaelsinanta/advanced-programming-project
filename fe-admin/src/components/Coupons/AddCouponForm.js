const AddCouponForm = ({
  handleAddCoupon,
  setShowModalAdd,
  newCoupon,
  handleCouponChange,
}) => {
  return (
    <div className="fixed inset-0 z-10 overflow-y-auto">
      <div className="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div className="fixed inset-0 transition-opacity" aria-hidden="true">
          <div className="absolute inset-0 bg-gray-500 opacity-75"></div>
        </div>
        <span
          className="hidden sm:inline-block sm:align-middle sm:h-screen"
          aria-hidden="true"
        >
          &#8203;
        </span>
        <div className="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
          <div className="bg-white text-black p-4 rounded text-center">
            <h2 className="text-center font-bold text-3xl mb-2">
              Add New Coupon
            </h2>
            <hr className="border-t-2 border-gray-500" />
            <div className="mb-2 mt-4">
              <label className="font-bold mr-2" htmlFor="name">
                Name:
              </label>
              <input
                type="text"
                id="name"
                name="name"
                value={newCoupon.name}
                onChange={handleCouponChange}
                className="bg-white border border-black rounded px-2"
                placeholder="Coupon Name"
                autoComplete="off"
              />
            </div>
            <div className="mb-2">
              <label className="font-bold mr-2" htmlFor="discount">
                Discount:
              </label>
              <input
                type="number"
                id="discount"
                name="discount"
                value={newCoupon.discount}
                onChange={handleCouponChange}
                className="bg-white border border-black rounded px-2"
                placeholder="Admin Fee"
                autoComplete="off"
              />
            </div>
            <div className="flex justify-center mt-8">
              <button
                className="bg-blue-700 text-white p-2 font-bold mr-4 rounded-lg"
                onClick={handleAddCoupon}
              >
                Add Coupon
              </button>
              <button
                className="bg-red-700 text-white p-2 font-bold rounded-lg"
                onClick={() => setShowModalAdd(false)}
              >
                Cancel
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
export default AddCouponForm;
