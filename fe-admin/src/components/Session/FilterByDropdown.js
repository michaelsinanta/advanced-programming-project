import {Fragment} from "react";
import {Listbox, Transition} from "@headlessui/react";
import {CheckIcon, ChevronUpDownIcon, ChevronRightIcon, ChevronLeftIcon} from "@heroicons/react/20/solid";

export const FilterByDropdown = ({filterOptions, selectedFilter, setSelectedFilter}) => {
    return (
        <Listbox value={selectedFilter} onChange={setSelectedFilter} className="pb-1">
            <div className="relative mt-1">
                <Listbox.Button className="relative w-28 cursor-default rounded-xl p-2.5 bg-[#EADCF8] font-medium text-purple-700 text-left focus:outline-none focus-visible:border-indigo-500 focus-visible:ring-2 focus-visible:ring-white focus-visible:ring-opacity-75 focus-visible:ring-offset-2 focus-visible:ring-offset-purple-300 sm:text-sm">
                    <span className="block truncate">{selectedFilter}</span>
                    <span className="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2">
                      <ChevronUpDownIcon
                          className="h-5 w-5 text-gray-400"
                          aria-hidden="true"
                      />
                    </span>
                </Listbox.Button>
                <Transition as={Fragment} leave="transition ease-in duration-100" leaveFrom="opacity-100" leaveTo="opacity-0">
                    <Listbox.Options className="p-2 z-50 absolute mt-1 max-h-60 w-full overflow-auto rounded-md bg-white text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
                        {filterOptions.map((filter) => (
                            <Listbox.Option
                                key={filter}
                                className={({ active }) => `z-50 rounded-lg relative cursor-default select-none py-2 pr-10 pl-4 ${
                                    active ? 'bg-purple-100 text-purple-900' : 'text-gray-900'
                                }`
                                }
                                value={filter}
                            >
                                {({ selected }) => (<>
                                        {selected ? (
                                            <span className="absolute inset-y-0 right-0 flex items-center pr-3 text-purple-600">
                                            <CheckIcon className="h-5 w-5" aria-hidden="true" />
                                        </span>
                                        ) : null}
                                        <span className={`z-50 block truncate ${selected ? 'font-medium' : 'font-normal'}`}>
                                        {filter}
                                    </span>
                                    </>
                                )}
                            </Listbox.Option>
                        ))}
                    </Listbox.Options>
                </Transition>
            </div>
        </Listbox>
    );
}