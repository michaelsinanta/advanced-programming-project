import { useEffect, useState } from "react";
import { Listbox, Transition } from "@headlessui/react";
import axios from "axios";
import { formatRupiah } from "../util/formatRupiah";

export default function PilihPaket({ pcId, selectedPaket, setSelectedPaket }) {
  const [paketItems, setPaketItems] = useState([]);
  const [makananName, setMakananName] = useState("");
  const isPaket = true;

  const [paketUnavailable, setPaketUnavailable] = useState(false);
  useEffect(() => {
    axios
      .get(`/api/warnet/sewa_pc/get_pricing_by_pc/${pcId}/${isPaket}`)
      .then((paket) => {
        setPaketItems(paket.data);
        setPaketUnavailable(paket.data.length === 0);
      });
  }, []);

  useEffect(() => {
    console.log("Makanan ID " + selectedPaket.makananId);
    if (selectedPaket.makananId !== -1 && selectedPaket.makananId !== null) {
      axios
        .get(`/api/cafe/menu/id/${selectedPaket.makananId}`)
        .then((response) =>
          setMakananName(
            response.data.name.charAt(0).toUpperCase() +
              response.data.name.slice(1)
          )
        );
    }
  }, [selectedPaket]);

  const paketSummary = () => {
    if (selectedPaket.durasi !== -1) {
      return (
        <div className="mt-4 rounded-md bg-blue-100 p-3 text-left grid grid-cols-2 gap-x-4">
          <p className="font-bold">Harga:</p>
          <p>{formatRupiah(selectedPaket.harga)}</p>
          <p className="font-bold">Durasi:</p>
          <p>{selectedPaket.durasi} jam</p>
          {selectedPaket.makananId !== null && (
            <p className="font-bold">Makanan/Minuman:</p>
          )}
          {selectedPaket.makananId !== null && <p>{makananName}</p>}
        </div>
      );
    }
  };

  return (
    <div className="flex items-center justify-center p-12">
      <div className="w-full max-w-xs mx-auto">
        <Listbox
          as="div"
          className="space-y-1"
          value={selectedPaket}
          onChange={setSelectedPaket}
          disabled={paketUnavailable}
        >
          {({ open }) => (
            <>
              <Listbox.Label className="block text-sm leading-5 font-medium text-gray-700">
                Pilih paket:
              </Listbox.Label>
              <div className="relative">
                <span className="inline-block w-full rounded-md shadow-sm">
                  <Listbox.Button
                    className={`${
                      paketUnavailable ? "cursor-not-allowed" : ""
                    } relative z-0 disabled:text-gray-400 cursor-default w-full rounded-md border border-gray-300 bg-white pl-3 pr-10 py-2 text-left focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition ease-in-out duration-150 sm:text-sm sm:leading-5`}
                  >
                    <span className="block truncate">
                      {selectedPaket.namaPricing}
                    </span>
                    <span className="absolute inset-y-0 right-0 flex items-center pr-2 pointer-events-none">
                      <svg
                        className="h-5 w-5 text-gray-400"
                        viewBox="0 0 20 20"
                        fill="none"
                        stroke="currentColor"
                      >
                        <path
                          d="M7 7l3-3 3 3m0 6l-3 3-3-3"
                          strokeWidth="1.5"
                          strokeLinecap="round"
                          strokeLinejoin="round"
                        />
                      </svg>
                    </span>
                  </Listbox.Button>
                </span>

                <Transition
                  show={open}
                  leave="transition ease-in duration-100"
                  leaveFrom="opacity-100"
                  leaveTo="opacity-0"
                  className="absolute mt-1 w-full rounded-md bg-white shadow-lg"
                >
                  <Listbox.Options
                    static
                    className="max-h-60 rounded-md py-1 text-base leading-6 shadow-xs overflow-auto focus:outline-none sm:text-sm sm:leading-5"
                  >
                    {paketItems.map((paket, index) => (
                      <Listbox.Option
                        key={paket.pricingId || index}
                        value={paket}
                      >
                        {({ selected, active }) => (
                          <div
                            className={`${
                              active
                                ? "text-white bg-blue-600"
                                : "text-gray-900"
                            } cursor-default select-none relative py-2 pl-8 pr-4`}
                          >
                            <span
                              className={`${
                                selected ? "font-semibold" : "font-normal"
                              } block truncate`}
                            >
                              {paket.namaPricing}
                            </span>
                            {selected && (
                              <span
                                className={`${
                                  active ? "text-white" : "text-blue-600"
                                } absolute inset-y-0 left-0 flex items-center pl-1.5`}
                              >
                                <svg
                                  className="h-5 w-5"
                                  xmlns="http://www.w3.org/2000/svg"
                                  viewBox="0 0 20 20"
                                  fill="currentColor"
                                >
                                  <path
                                    fillRule="evenodd"
                                    d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                                    clipRule="evenodd"
                                  />
                                </svg>
                              </span>
                            )}
                          </div>
                        )}
                      </Listbox.Option>
                    ))}
                  </Listbox.Options>
                </Transition>
                {paketSummary()}
              </div>
            </>
          )}
        </Listbox>
      </div>
    </div>
  );
}
