import axios from "axios";
import React, { useEffect, useState } from "react";
import { useRouter } from "next/router";
import useWarnetSession from "@/components/warnet/hooks/useSession";
import { useSessionContext } from "@/components/warnet/context/SessionContext";
import PricingChooser from "@/components/warnet/PricingChooser";
import { toast } from "react-toastify";

export default function PerpanjangSewa() {
  const router = useRouter();
  const { updateSessionInfo } = useSessionContext();
  const { getSessionId, checkSessionStillActive } = useWarnetSession();
  const [sessionId, setSessionId] = useState("loading...");
  const [selectedPC, setSelectedPC] = useState({
    id: -1,
    noPC: -1,
    noRuangan: -1,
  });

  const [jenis, setJenis] = useState("Tarif per jam");
  const [qty, setQty] = useState(0);
  const [selectedPricing, setSelectedPricing] = useState({
    pricingId: -1,
    namaPricing: "Pilih Paket",
    harga: -1,
    durasi: -1,
    makananId: -1,
  });

  useEffect(() => {
    checkSessionStillActive()
      .then((isActive) => {
        if (isActive) {
          setSessionId(getSessionId());
        } else {
          toast.error("Maaf, sesi Anda sudah habis.");
          router.push("/invoice");
        }
      })
      .catch((error) => {
        console.error(error);
      });
  }, []);
  

  useEffect(() => {
    if (sessionId !== "loading...") {
      axios
        .get(`/api/warnet/sewa_pc/get_session/${sessionId}`)
        .then((response) => {
          setSelectedPC(response.data.pc);
        })
        .catch((error) => {
          console.log(error);
        });
    }
  }, [sessionId]);

  const handleSubmitPerpanjang = () => {
    if (selectedPC.id === -1 || selectedPricing.pricingId === -1) {
      toast.error("Harap memilih PC dan tarif/paket");
      return;
    } else if (qty <= 0) {
      toast.error("Durasi sewa harus angka bulat positif");
      return;
    }
    console.log(
      "id " +
        sessionId +
        ", pricing " +
        selectedPricing.pricingId +
        ", qty " +
        qty
    );
    axios
      .post("/api/warnet/perpanjang/tambah_pricing", {
        id: sessionId,
        pricingId: selectedPricing.pricingId,
        quantity: qty,
      })
      .then((response) => {
        setSessionId(response.data.id);
        updateSessionInfo(response.data);
        toast.success("Sukses memperpanjang sesi!");
        router.push("/warnet");
      })
      .catch((error) => {
        toast.error(error.response.data.message)
      })
  };

  return (
    <div className="bg-white text-black p-4 rounded text-center">
      <h2 className="text-center font-bold text-3xl mb-2">Perpanjang Sewa</h2>
      <hr className="border-t-2 border-gray-500" />
      <div>
        <div className="py-10">
          <p>Perpanjang sewa untuk</p>
          {selectedPC.id !== -1 && (
            <p className="font-semibold">
              PC {selectedPC.noPC} Ruangan {selectedPC.noRuangan}
            </p>
          )}
        </div>
        {selectedPC.id !== -1 && (
          <PricingChooser
            pcId={selectedPC.id}
            selectedPricing={selectedPricing}
            setSelectedPricing={setSelectedPricing}
            qty={qty}
            setQty={setQty}
            jenis={jenis}
            setJenis={setJenis}
          ></PricingChooser>
        )}

        <div className="flex justify-center mt-8">
          <button
            className="bg-blue-700 text-white p-2 font-bold mr-4 rounded-lg"
            onClick={handleSubmitPerpanjang}
          >
            Submit
          </button>
        </div>
      </div>
    </div>
  );
}
