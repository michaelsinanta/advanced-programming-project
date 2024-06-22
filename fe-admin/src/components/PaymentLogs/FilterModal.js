import { modalTitle } from "./util";

const FilterModal = ({
  setShowModal,
  filterBy,
  submitForm,
  handleInput,
  formData,
}) => {
  return (
    <>
      <div className="justify-center items-center flex overflow-x-hidden overflow-y-auto fixed inset-0 z-50 outline-none focus:outline-none">
        <div className="relative w-auto my-6 mx-auto max-w-3xl">
          {/*content*/}
          <div className="border-0 rounded-lg shadow-lg relative flex flex-col w-full dark:bg-gray-800 dark:border-gray-700 outline-none focus:outline-none">
            {/*header*/}
            <div className="flex items-start justify-between p-5 border-b border-solid rounded-t">
              <h3 className="text-3xl font-semibold">{modalTitle(filterBy)}</h3>
              <button
                className="p-1 ml-auto border-0 text-red- opacity-4 float-right text-3xl leading-none font-semibold outline-none focus:outline-none"
                onClick={() => setShowModal(false)}
              >
                X
              </button>
            </div>

            {/*body*/}
            <form
              method="POST"
              onSubmit={submitForm}
              className="flex flex-col px-8 pt-6 pb-8 mb-4 rounded"
            >
              <label htmlFor="year" className="mb-2">
                Year
              </label>
              <input
                className="mb-4 border-b-2"
                onChange={handleInput}
                value={formData.year}
                id="year"
                name="year"
                type="number"
                autoComplete="year"
                required
              />
              {filterBy !== "yearly" && (
                <>
                  <label
                    htmlFor={filterBy === "monthly" ? "month" : "week"}
                    className="mb-2"
                  >
                    {filterBy === "monthly" ? "Month" : "Week"}
                  </label>
                  <input
                    className="mb-4 border-b-2"
                    onChange={handleInput}
                    value={
                      filterBy === "monthly" ? formData.month : formData.week
                    }
                    id={filterBy === "monthly" ? "month" : "week"}
                    name={filterBy === "monthly" ? "month" : "week"}
                    type="number"
                    autoComplete={filterBy === "monthly" ? "month" : "week"}
                    required
                  />
                </>
              )}

              {/*footer*/}
              <div className="flex items-center justify-end p-6 border-t border-solid border-slate-200 rounded-b">
                <button
                  className="text-red-500 background-transparent font-bold uppercase px-6 py-2 text-sm outline-none focus:outline-none mr-1 mb-1 ease-linear transition-all duration-150"
                  type="button"
                  onClick={() => setShowModal(false)}
                >
                  Close
                </button>
                <button
                  className="bg-blue-700 text-white active:bg-emerald-600 font-bold uppercase text-sm px-6 py-3 rounded shadow hover:shadow-lg outline-none focus:outline-none mr-1 mb-1 ease-linear transition-all duration-150"
                  type="submit"
                >
                  Filter
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
      <div className="opacity-25 fixed inset-0 z-40 bg-black"></div>
    </>
  );
};
export default FilterModal;
