import PilihPaket from "@/components/warnet/PilihPaket";
import PilihTarif from "@/components/warnet/PilihTarif";

export default function PricingChooser({ pcId, jenis, setJenis, selectedPricing, setSelectedPricing, qty, setQty }) {

    const onOptionChange = e => {
        setSelectedPricing({pricingId: -1, namaPricing: "Pilih Paket", harga: -1, durasi: -1, makananId: -1})
        setJenis(e.target.value);
    }

    const paketSelected = (jenis === "Paket");
    const tarifSelected = (jenis === "Tarif per jam");

    if (paketSelected) setQty(1);

    return (
        <div>
            <div className="flex justify-center">
                <div className="grid grid-cols-2 gap-4">
                    <div>
                        <input
                            type="radio"
                            name="jenis"
                            value="Tarif per jam"
                            id="tarif"
                            checked={jenis === "Tarif per jam"}
                            onChange={onOptionChange}
                        />
                        <label htmlFor="tarif" className="px-5">Tarif per jam</label>
                    </div>
                    <div>
                        <input
                            type="radio"
                            name="jenis"
                            value="Paket"
                            id="paket"
                            checked={jenis === "Paket"}
                            onChange={onOptionChange}
                        />
                        <label htmlFor="medium" className="px-5">Paket</label>
                    </div>
                </div>
            </div>
            <div>
                {paketSelected &&
                    <PilihPaket
                        pcId={ pcId }
                        selectedPaket={selectedPricing}
                        setSelectedPaket={setSelectedPricing}
                    >

                    </PilihPaket>
                }
                {tarifSelected &&
                    <PilihTarif
                        pcId={pcId}
                        selectedTarif={selectedPricing}
                        setSelectedTarif={setSelectedPricing}
                        qtyTarif={qty}
                        setQtyTarif={setQty}
                    >
                    </PilihTarif>
                }
            </div>
        </div>

    )
}