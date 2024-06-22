import React, { useEffect, useState } from "react";
import axios from "axios";
import PricingChooser from "@/components/warnet/PricingChooser";
import {Listbox, Transition} from "@headlessui/react";
import PilihPC from "@/components/warnet/PilihPC";

export default function FormPilih({
                                      selectedPC,
                                      setSelectedPC,
                                      selectedPricing,
                                      setSelectedPricing,
                                      qty,
                                      setQty,
                                  }) {
    const pcChosen = selectedPC.id !== -1;
    const [jenis, setJenis] = useState("Tarif per jam")

    useEffect(() => {
        setSelectedPricing({pricingId: -1, namaPricing: "Pilih Paket", harga: -1, durasi: -1, makananId: -1})
        setQty(0)
        setJenis("")
    }, [selectedPC])

    return (
        <div className="mb-2 mt-4">
            <div>
                <div className="flex items-center justify-center p-12">
                    <div className="w-full max-w-xs mx-auto">
                        <PilihPC selectedPC={selectedPC} setSelectedPC={setSelectedPC} pcChosen={pcChosen}/>
                    </div>
                </div>
                {pcChosen && (
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
            </div>
        </div>
    );
}
