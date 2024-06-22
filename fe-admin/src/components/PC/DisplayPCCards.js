import React, { useState, useEffect } from "react";
import AddPCForm from "../PC/AddPCForm";
import PCCard from "../PC/PCCard";
import axios from "axios";
import { useRouter } from "next/router";

const CreatePC = () => {
  const [PCItems, setPCItems] = useState([]);
  const [showDiscountModal, setShowDiscountModal] = useState(false);

  useEffect(() => {
    axios
      .get("/api/warnet/kelola_warnet/all_pc")
      .then((PCItems) => setPCItems(PCItems.data));
  }, []);

  const [showModal, setShowModal] = useState(false);
  const router = useRouter();

  const PostPC = (data) => {
    console.log("jalan ga", data);
    axios
      .post("/api/warnet/kelola_warnet/create_pc", data)
      .then(setShowModal(false))
      .then(router.reload());
    console.log("jalan ga ni", data);
  };

  const handleAddPC = (data) => {
    console.log(data);
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

    PostPC(data);
  };

  const handleGiveDiscount = (sessionId, discountType, discount) => {
    console.log(sessionId)
    console.log(discountType)
    axios.post(`/api/bayar/sessions/${sessionId}/discount`, {
      "discountType": discountType,
      "discount": discount,
    })
    .then(response => {
      console.log(response);
      setShowDiscountModal(false);
    })
    .catch(error => {
      console.log(error);
    });
  };

  return (
    <div className="items-center m-8">
      <h1 className="text-4xl text-center my-8 font-bold">PC LIST</h1>
      <div className="mt-4 text-center mb-8">
        <button
          className="bg-blue-600 p-4 rounded font-semibold"
          onClick={() => setShowModal(true)}
        >
          Add New PC
        </button>
      </div>
      {showModal && (
        <AddPCForm handleAddPC={handleAddPC} setShowModal={setShowModal} />
      )}
      <div className="grid lg:grid-cols-3 md:grid-cols-2 grid-cols-1 gap-x-4">
        {PCItems.map((PC, index) => (
          <div className="col-md-4 mb-4 mx-4 flex" key={PC.id || index}>
            <PCCard
              {...PC}
              onDelete={async () => {
                await axios.delete(
                  `/api/warnet/kelola_warnet/delete_pc/${PC.id}`
                );
                router.reload();
              }}
              showModal={showDiscountModal}
              setShowModal={setShowDiscountModal}
              giveDiscount={handleGiveDiscount}
            />
          </div>
        ))}
      </div>
    </div>
  );
};

export default CreatePC;
