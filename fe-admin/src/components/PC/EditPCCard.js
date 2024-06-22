import React, { useState, useEffect } from "react";
import EditPCForm from "../PC/EditPCForm"
import axios from "axios";
import { useRouter } from 'next/router';

const EditPCCard = ({id}) => {
  const [PCItems, setPCItems] = useState({id:"loading...",noPC:"loading...",noRuangan:"loading..."});

  useEffect(() => {
    if (id) {
      axios.get(`/api/warnet/kelola_warnet/pc_detail/${id}`).then((response) => setPCItems(response.data));
    }
  }, [id]);

  const [showModal, setShowModal] = useState(false);
  const router = useRouter();
  
  const handleEditPC = (data) => {
    if (!data.noPC || !data.noRuangan) {
      alert("Please provide all required input");
      return;
    } else if (data.noPC <= 0) {
      alert("PC number must be positive integer");
      return;
    } else if (data.noRuangan <= 0) {
      alert("PC room must be positive integer");
      return;
    }
    EditPC(data);
  };

  const EditPC = (data) => {
    axios.put(`/api/warnet/kelola_warnet/pc_detail/${id}/update`, data)
        .then(() => {
          setShowModal(false);
          router.push(`/AdminWarnet/PC`);
        })
  }

  return (
    <div className="flex flex-col items-center m-8">
      <h1 className="text-4xl text-center my-8 font-bold">EDIT PC</h1>
      <div className="mt-4 text-center mb-8">
        {id && (
          <button
            className="bg-blue-600 p-4 rounded font-semibold"
            onClick={() => setShowModal(true)}
          >
            Edit PC
          </button>
        )}
      </div>
      {showModal && (
        <EditPCForm
          handleEditPC={handleEditPC}
          setShowModal={setShowModal}
        />
      )}
      <div className="container w-fit rounded-2xl p-5 bg-[#EADCF8] text-purple-700 drop-shadow-[0_4px_14px_rgba(188,142,233,0.5)] border-none">
        <p><strong>ID PC:</strong> {PCItems.id}</p>
        <p><strong>Nomor PC:</strong> {PCItems.noPC}</p>
        <p><strong>Nomor Ruangan:</strong> {PCItems.noRuangan}</p>
      </div>
    </div>
  );
};

export default EditPCCard;