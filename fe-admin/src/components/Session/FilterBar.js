import { Button } from "@nextui-org/react";
import {Fragment, useState} from "react";
import {PCDropdown} from "@/components/Session/PCDropdown";
import {FilterByDropdown} from "@/components/Session/FilterByDropdown";
import {SubmitFilterButton} from "@/components/Session/SubmitFilterButton";

export const FilterBar = ( filter ) => {
    const filterOptions = ["None", "PC", "Date"]
    const [selectedFilter, setSelectedFilter] = useState(filterOptions[0]);

    const filterByPC = selectedFilter === filterOptions[1];
    const filterByDate = selectedFilter === filterOptions[2];

    const [pcFilter, setPcFilter] = useState({id: -1, noPC: -1, noRuangan: -1});
    const [dateFilter, setDateFilter] = useState("");

    const handleDateChange = e => {
        setDateFilter(e.target.value);
    }

    const secondFilterDropdown = () => {
        if (filterByDate) {
            return <input type="date" value={dateFilter} onChange={handleDateChange} className="rounded-xl p-2 bg-purple-100 text-purple-700 text-sm dark:[color-scheme:light]"></input>;
        } else if (filterByPC) {
            return <PCDropdown pcFilter={pcFilter} setPcFilter={setPcFilter}/>
        }
    }

    const renderSubmitFilterButton = () => {
        if (filterByPC) {
            return <SubmitFilterButton pcId={pcFilter.id} setPcId={filter.setPcId} setDate={filter.setDate} setPage={filter.setPage}/>
        } else if (filterByDate) {
            return <SubmitFilterButton date={dateFilter} setDate={filter.setDate} setPcId={filter.setPcId} setPage={filter.setPage}/>
        }
        return <SubmitFilterButton setPcId={filter.setPcId} setDate={filter.setDate} setPage={filter.setPage}/>
    }

    return (
        <div className="flex justify-center items-center py-4 space-x-2" style={{position:"relative", zIndex: 5}}>
            <div className="flex items-center flex-row space-x-2 bg-purple-800 px-4 py-2 rounded-xl drop-shadow-[0_4px_14px_rgba(188,142,233,0.5)] border-none font-semibold">
                <p>Filter by</p>
                <FilterByDropdown filterOptions={filterOptions} selectedFilter={selectedFilter} setSelectedFilter={setSelectedFilter}/>
                {secondFilterDropdown()}
            </div>
            <div>
                {renderSubmitFilterButton()}
            </div>
        </div>
    );
}