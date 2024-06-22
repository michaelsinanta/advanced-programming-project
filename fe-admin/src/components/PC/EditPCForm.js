import React from "react";
import { useForm } from "react-hook-form";

const EditPCForm = ({ handleEditPC, setShowModal }) => {
  const {register, handleSubmit} = useForm()
  return (
    <div className="fixed inset-0 z-10 overflow-y-auto">
      <div className="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div className="fixed inset-0 transition-opacity" aria-hidden="true">
          <div className="absolute inset-0 bg-gray-500 opacity-75"></div>
        </div>
        <span className="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>
        <div className="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
          <form className="bg-white text-black p-4 rounded text-center" onSubmit={handleSubmit(handleEditPC)}>
            <h2 className="text-center font-bold text-3xl mb-2">
              Edit PC
            </h2>
            <hr class="border-t-2 border-gray-500" />
            <div className="mb-2 mt-4">
              <label className="font-bold mr-2" htmlFor="name">
                No PC:
              </label>
              <input
                type="number"
                id="noPC"
                {...register("noPC")}
                className="bg-white border border-black rounded px-2"
                placeholder="No. PC"
                autoComplete="off"
              />
            </div>
            <div className="mb-2">
              <label className="font-bold mr-2" htmlFor="description">
                No Ruangan:
              </label>
              <input
                type="number"
                id="noRuangan"
                {...register("noRuangan")}
                className="bg-white border border-black rounded px-2"
                placeholder="No. Ruangan PC"
                autoComplete="off"
              />
            </div>
            <div className="flex justify-center mt-8">
              <button
                className="bg-blue-700 text-white p-2 font-bold mr-4 rounded-lg"
                type="submit"
              >
                Edit PC
              </button>
              <button
                className="bg-red-700 text-white p-2 font-bold rounded-lg"
                onClick={() => setShowModal(false)}
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default EditPCForm;
