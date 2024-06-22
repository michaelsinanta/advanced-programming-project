import React from "react";

const MenuItemForm = ({
  handleAddMenuItem,
  setShowModal,
  newMenuItem,
  handleNewMenuItemChange,
  isLoading,
  defaultName = "",
  defaultStock = 0,
  defaultPrice = 0,
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
              Add New Menu Item
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
                value={newMenuItem.name}
                onChange={handleNewMenuItemChange}
                className="bg-white border border-black rounded px-2"
                placeholder="Item Name"
                autoComplete="off"
              />
            </div>
            <div className="mb-2">
              <label className="font-bold mr-2" htmlFor="stock">
                Stock:
              </label>
              <input
                type="number"
                id="stock"
                name="stock"
                value={newMenuItem.stock}
                onChange={handleNewMenuItemChange}
                className="bg-white border border-black rounded px-2"
                placeholder="Item stock"
                autoComplete="off"
              />
            </div>
            <div className="mb-2">
              <label className="font-bold mr-2" htmlFor="price">
                Price:
              </label>
              <input
                type="number"
                id="price"
                name="price"
                value={newMenuItem.price}
                onChange={handleNewMenuItemChange}
                className="bg-white border border-black rounded px-2"
                placeholder="Item price"
                autoComplete="off"
              />
            </div>
            <div className="flex justify-center mt-8">
              <button
                className="bg-blue-700 text-white p-2 font-bold mr-4 rounded-lg"
                onClick={handleAddMenuItem}
                disabled={isLoading}
              >
                 {isLoading ? (
                  <div className="flex w-full justify-center items-center">
                    <svg
                      class="animate-spin -ml-1 mr-3 h-5 w-5 text-white"
                      xmlns="http://www.w3.org/2000/svg"
                      fill="none"
                      viewBox="0 0 24 24"
                    >
                      <circle
                        class="opacity-25"
                        cx="12"
                        cy="12"
                        r="10"
                        stroke="currentColor"
                        stroke-width="4"
                      ></circle>
                      <path
                        class="opacity-75"
                        fill="currentColor"
                        d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                      ></path>
                    </svg>
                    Processing
                  </div>
                ) : (
                  "Add Menu"
                )}
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
  );
};

export default MenuItemForm;
