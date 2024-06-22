import React, { useState, useEffect } from "react";
import BankCard from "./BankCards";
import BankForm from "./BankForm";
import axios from "axios";

const DisplayBanks = () => {
  const [banks, setBanks] = useState([]);
  const [showAddModal, setShowAddModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [newBank, setNewBank] = useState({
    name: "",
    adminFee: 0,
  });

  useEffect(() => {
    axios
      .get(`/api/bayar/banks`)
      .then(function (response) {
        setBanks(response.data.content);
      });
  }, []);

  const handleBankChange = (event) => {
    const { name, value } = event.target;
    setNewBank((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleRemoveBank = (bank) => {
    fetch(`/api/bayar/banks/delete/${bank.id}`, {
      method: "DELETE",
    })
      .then(() => {
        const newBanks = banks.filter(
          (bankDetail) => bankDetail !== bank
        );
        setBanks(newBanks);
      })
  };

  const handleAddBank = () => {
    if (!newBank.name || !newBank.adminFee) {
      alert("Please provide all required input");
      return;
    } else if (newBank.adminFee < 0) {
      alert("Admin Fee cannot be negative.");
      return;
    }
  
    fetch(`/api/bayar/addBank`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(newBank),
    })
      .then((response) => response.json())
      .then((data) => {
        setBanks((prevState) => [...prevState, data]);
        setNewBank({
          name: "",
          adminFee: 0,
        });
        setShowAddModal(false);
      })
      .catch((error) => console.error(error));
  };

  const handleEditBank = (bankId, bankName, adminFee) => {
    axios.put(`/api/bayar/banks/update/${bankId}`, {
      "id": bankId,
      "name": bankName,
      "adminFee": adminFee
    })
    .then(response => {
      console.log(response);
      const editedBank = response.data
      setBanks((prevState) => prevState.map(bank => bank.id == editedBank.id ? editedBank : bank))
      setShowEditModal(false);
    })
    .catch(error => {
      console.log(error);
    });
  }

  return (
    <div className="items-center m-8">
      <h1 className="text-4xl text-center my-8 font-bold">Banks</h1>
      <div className="mt-4 text-center mb-8">
        <button
          className="bg-blue-600 p-4 rounded font-semibold"
          onClick={() => setShowAddModal(true)}
        >
          Add New Bank
        </button>
      </div>
      {showAddModal && (
        <BankForm
          handleAddBank={handleAddBank}
          setShowModal={setShowAddModal}
          newBank={newBank}
          handleBankChange={handleBankChange}
        />
      )}
      <div className="grid lg:grid-cols-3 md:grid-cols-2 grid-cols-1 gap-x-4">
        {banks?.map((bank) => (
          <div className="col-md-4 mb-4 mx-4 flex" key={bank.id}>
            <BankCard
              bank={bank}
              showModal={showEditModal}
              setShowModal={setShowEditModal}
              removeBank={handleRemoveBank}
              editBank={handleEditBank}
            />
          </div>
        ))}
      </div>
    </div>
  );
};

export default DisplayBanks;
