import React, {useState} from "react";

const EditBankForm = ({ setShowModal, bank, editBank }) => {
  const [newName, setNewName] = useState([]);
  const [newAdminFee, setNewAdminFee] = useState([]);

  const handleEditBank = () => {

    const bankId = bank.id;
    let bankName = "";
    let adminFee = 0;

    if (newAdminFee < 0) {
      alert("Admin Fee cannot be negative.");
      return;
    }
    else {
      if (newName.length == 0) {
        bankName = bank.name;
        adminFee = newAdminFee;
      } 
      else if (newAdminFee.length == 0) {
        bankName = newName;
        adminFee = bank.adminFee;
      }
      else {
        bankName = newName;
        adminFee = newAdminFee;
      }
    }
    editBank(bankId, bankName, adminFee)
  };

  return (
    <div className="fixed inset-0 z-10 overflow-y-auto">
      <div className="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div className="fixed inset-0 transition-opacity" aria-hidden="true">
          <div className="absolute inset-0 bg-gray-500 opacity-75"></div>
        </div>
        <span className="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>
        <div className="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
          <div className="bg-white text-black p-4 rounded text-center">
            <h2 className="text-center font-bold text-3xl mb-2">
              Edit Bank Information
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
                value={newName}
                onChange={e => {setNewName(e.target.value)}}
                className="bg-white border border-black rounded px-2"
                placeholder={bank.name}
                autoComplete="off"
              />
            </div>
            <div className="mb-2">
              <label className="font-bold mr-2" htmlFor="adminFee">
                Admin Fee:
              </label>
              <input
                type="number"
                id="adminFee"
                name="adminFee"
                value={newAdminFee}
                onChange={e => setNewAdminFee(e.target.value)}
                className="bg-white border border-black rounded px-2"
                placeholder={bank.adminFee}
                autoComplete="off"
              />
            </div>
            <div className="flex justify-center mt-8">
              <button
                className="bg-blue-700 text-white p-2 font-bold mr-4 rounded-lg"
                onClick={handleEditBank}
              >
                Save Changes
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

export default EditBankForm;
