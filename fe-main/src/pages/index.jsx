import FormPilih from "@/components/warnet/FormPilih";
import axios from "axios";
import React, {useEffect, useState} from "react";
import { useRouter } from 'next/router'
import useWarnetSession from "@/components/warnet/hooks/useSession";
import { useSessionContext } from '../components/warnet/context/SessionContext';
import { toast } from "react-toastify";

export default function PilihPCPricing() {
  const router = useRouter()
  const {setSessionId} = useWarnetSession()
  const {updateSessionInfo} = useSessionContext()
  const [qty, setQty] = useState(0)
  const [selectedPC, setSelectedPC] = useState({id: -1, noPC: -1, noRuangan: -1})
  const [selectedPricing, setSelectedPricing] = useState({pricingId: -1, namaPricing: "Pilih Paket", harga: -1, durasi: -1, makananId: -1})
  const { getSessionId, checkSessionStillActive } = useWarnetSession();

    useEffect(() => {
        checkSessionStillActive()
            .then((isActive) => {
                if (isActive) {
                    toast.error("Anda masih memiliki sesi aktif.");
                    router.push("/warnet");
                } else if (getSessionId()) {
                    toast.error("Anda belum membayar tagihan warnet.");
                    router.push("/invoice");
                }
            })
            .catch((error) => {
                console.error(error);
            });
    }, []);

  const handleSubmitPilih = () => {
    if (selectedPC.id === -1 || selectedPricing.pricingId === -1) {
      toast.error("Harap memilih PC dan tarif/paket");
      return;
    } else if (qty <= 0) {
      toast.error("Durasi sewa harus angka bulat positif");
      return;
    }
    axios.post('/api/warnet/sewa_pc/create_session',
        {
          "pcId": selectedPC.id,
          "pricingId": selectedPricing.pricingId,
          "quantity": qty
        })
        .then((response) => {
          setSessionId(response.data.id)
          updateSessionInfo(response.data)
          toast.success("Selamat berinternet!")
          router.push('/warnet')
        })
        .catch((error) => {
          toast.error(error.response.data.message)
        })

  }

  return (
      <div className="bg-white text-black p-4 rounded text-center">
        <h2 className="text-center font-bold text-3xl mb-2">Selamat Datang!</h2>
        <hr className="border-t-2 border-gray-500" />
        <div>
          <FormPilih
              selectedPC={selectedPC}
              setSelectedPC={setSelectedPC}
              selectedPricing={selectedPricing}
              setSelectedPricing={setSelectedPricing}
              qty={qty}
              setQty={setQty}
          >
          </FormPilih>
          <div className="flex justify-center mt-8">
            <button
                className="bg-blue-700 text-white p-2 font-bold mr-4 rounded-lg"
                onClick={handleSubmitPilih}
            >
              Mulai Sesi Baru
            </button>
          </div>
        </div>
      </div>
  )
}