import React, {useEffect, useState} from "react";
import axios from "axios";
import {Listbox, Transition} from "@headlessui/react";

export default function PilihPC({selectedPC, setSelectedPC, pcChosen}) {
    const [PCItems, setPCItems] = useState([]);

    useEffect(() => {
        axios
            .get("/api/warnet/kelola_warnet/all_pc", {
                params: {
                    filtered: true
                }
            })
            .then((PCItems) => setPCItems(PCItems.data));
    }, []);

    return (
        <Listbox as="div" className="space-y-1" value={selectedPC} by="id" onChange={setSelectedPC}>
            {({ open }) => (
                <>
                    <Listbox.Label className="block text-sm leading-5 font-medium text-gray-700">
                        Pilih PC:
                    </Listbox.Label>
                    <div className="relative">
                        <span className="inline-block w-full rounded-md shadow-sm">
                            <Listbox.Button className="cursor-default relative w-full rounded-md border border-gray-300 bg-white pl-3 pr-10 py-2 text-left focus:outline-none focus:shadow-outline-blue focus:border-blue-300 transition ease-in-out duration-150 sm:text-sm sm:leading-5">
                                <span className="block truncate">
                                    {pcChosen && `PC ${selectedPC.noPC} Ruangan ${selectedPC.noRuangan}`}
                                    {!pcChosen && "Pilih PC"}
                                </span>
                                <span className="absolute inset-y-0 right-0 flex items-center pr-2 pointer-events-none">
                                    <svg className="h-5 w-5 text-gray-400" viewBox="0 0 20 20" fill="none" stroke="currentColor">
                                        <path d="M7 7l3-3 3 3m0 6l-3 3-3-3" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
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
                                className="relative z-50 bg-white max-h-60 rounded-md py-1 text-base leading-6 shadow-xs overflow-auto focus:outline-none sm:text-sm sm:leading-5"
                            >
                                {PCItems.map((PC, index) => (
                                    <Listbox.Option key={PC.id || index} value={PC}>
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
                                                    PC {PC.noPC} Ruangan {PC.noRuangan}
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
                    </div>
                </>
            )}
        </Listbox>
    );
}