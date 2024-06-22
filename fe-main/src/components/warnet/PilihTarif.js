import { useEffect } from "react";
import axios from "axios";
import { formatRupiah } from "../util/formatRupiah";

export default function PilihTarif({
  pcId,
  selectedTarif,
  setSelectedTarif,
  qtyTarif,
  setQtyTarif,
}) {
  const handleTarif = (event) => {
    let qtyInput = event.target.value;
    if (qtyInput % 1 !== 0) {
      setQtyTarif(Math.floor(qtyInput));
    } else if (qtyInput < 0) {
      setQtyTarif(0);
    } else {
      setQtyTarif(qtyInput);
    }
  };

  const isPaket = false;
  const initial = selectedTarif.pricingId === -1;
  const qtyChosen = qtyTarif !== 0 && !initial;

  useEffect(() => {
    axios
      .get(`/api/warnet/sewa_pc/get_pricing_by_pc/${pcId}/${isPaket}`)
      .then((tarif) =>
        tarif.data.length !== 0
          ? setSelectedTarif(tarif.data[0])
          : setSelectedTarif({
              pricingId: -1,
              namaPricing: "Pilih Paket",
              harga: -1,
              durasi: -1,
              makananId: -1,
            })
      );
  }, []);

  return (
    <div>
      <div className="flex items-center justify-center p-5">
        <div className="font-medium text-gray-700">
          {!initial && (
            <div className="mt-4 rounded-md bg-yellow-100 p-3 text-left">
              <p>
                Tarif:{" "}
                <span className="text-gray-950">
                  {formatRupiah(selectedTarif.harga)}
                </span>{" "}
                per jam
              </p>
            </div>
          )}
        </div>
      </div>
      <div className="flex flex-col justify-center items-center">
        <label htmlFor="qty">Total durasi sewa:</label>
        <div className="relative flex justify-center items-stretch">
          <input
            type="number"
            min="1"
            step="1"
            id="qtyInput"
            name="qtyInput"
            className="cursor-default invalid:border-pink-500 focus:invalid:border-pink-500 invalid:text-pink-600 relative w-1/2 rounded-l-md border border-gray-300 bg-white px-4 py-2 text-left focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition ease-in-out duration-150 sm:text-sm sm:leading-5"
            onChange={handleTarif}
            value={qtyTarif}
            required
          />
          <span
            className="flex items-center rounded-r border border-l-0 border-solid border-neutral-300 px-3 py-[0.25rem] text-center"
            id="basic-addon2"
          >
            jam
          </span>
        </div>
        {qtyChosen && (
          <div className="mt-4 rounded-md bg-blue-100 p-3 text-left">
            <p>Harga : {formatRupiah(selectedTarif.harga)}</p>
            <p>Durasi : {qtyTarif < 0 ? 0 : Math.floor(qtyTarif)} jam</p>
            <p className="font-semibold">
              Total:{" "}
              {qtyTarif < 0
                ? Rp0
                : formatRupiah(selectedTarif.harga * Math.floor(qtyTarif))}
            </p>
          </div>
        )}
      </div>
    </div>
  );
}
