import { Table, Tooltip, Loading } from "@nextui-org/react";
import React, {useEffect, useState} from "react";
import axios from "axios";
import {EyeIcon} from "@/components/Session/EyeIcon";
import {IconButton} from "@/components/Session/IconButton";
import {ChevronRightIcon, ChevronLeftIcon} from "@heroicons/react/20/solid";
import {FilterBar} from "@/components/Session/FilterBar";
import { useRouter } from "next/router";
import {formatDatetime, getTimeZoneAbbreviation, getNonTimeZonePart} from "@/components/Session/formatDatetime";

export default function AllSessionTable() {
    const router = useRouter();
    const [isLoading, setIsLoading] = useState(true);
    const [sessionItems, setSessionItems] = useState([]);
    const [page, setPage] = useState(1);
    const isFirstPage = (page-1) === 0;
    const [isLastPage, setIsLastPage] = useState(true);
    const [pcId, setPcId] = useState(null);
    const [date, setDate] = useState(null);

    useEffect(() => {
        console.log("page", page, "pcId", pcId, "date", date);
        setIsLoading(true);
        axios
            .get("/api/warnet/info_sesi/all_session", {
                params: {
                    page: page,
                    limit: 8,
                    pcId,
                    date
                }
            })
            .then((sessions) => {
                setSessionItems(sessions.data);
                setIsLoading(false);
            });
    }, [page, pcId, date]);

    useEffect(() => {
        axios
            .get("/api/warnet/info_sesi/all_session", {
                params: {
                    page: page*8+1,
                    limit: 1,
                    pcId,
                    date
                }
            })
            .then((sessions) => setIsLastPage(sessions.data.length === 0));
    }, [page, pcId, date]);

    const handleNextPage = () => {
        setPage(page+1);
    }

    const handlePrevPage = () => {
        setPage(page-1);
    }

    const timeZoneAbbreviation = getTimeZoneAbbreviation()

    const columns = [
        {key: "id", label: "SESSION ID"},
        {key: "pc", label: "PC"},
        {key: "datetimeStart", label: `SESSION START TIME (${timeZoneAbbreviation})`},
        {key: "datetimeEnd", label: `SESSION END TIME (${timeZoneAbbreviation})`},
        {key: "actions", label: "" },
    ];

    const renderCell = (item, columnKey) => {
        const cellValue = item[columnKey];
        switch (columnKey) {
            case "datetimeStart":
                return (
                    <p>{getNonTimeZonePart(item.datetimeStart)}</p>
                );
            case "datetimeEnd":
                return (
                    <p>{getNonTimeZonePart(item.datetimeEnd)}</p>
                );
            case "pc":
                return (
                    <p>PC {item.noPC} Ruangan {item.noRuangan} </p>
                );
            case "actions":
                return (
                        <Tooltip content="Details">
                            <IconButton onClick={
                                () => router.push(`/AdminWarnet/Session/SessionDetail?id=${item.id}`)
                            }>
                                <EyeIcon size={20} fill="#979797" />
                            </IconButton>
                        </Tooltip>
                );
            default:
                return cellValue;
        }
    };

    const renderTable = () => {
        if (isLoading) {
            return (
                <div className="h-[27.3rem] w-full grid items-center">
                    <Loading/>
                </div>
            );
        } else {
            return (
                <Table
                    bordered
                    shadow={false}
                    color="secondary"
                    aria-label="All Session Table"
                    css={{
                        height: "auto",
                        minWidth: "100%",
                        bgColor:"$white"
                    }}
                    selectionMode="none"
                >
                    <Table.Header columns={columns}>
                        {(column) => (
                            <Table.Column key={column.key}>{column.label}</Table.Column>
                        )}
                    </Table.Header>
                    <Table.Body items={sessionItems}>
                        {(item) => (
                            <Table.Row key={item.key}>
                                {(columnKey) => <Table.Cell>{renderCell(item, columnKey)}</Table.Cell>}
                            </Table.Row>
                        )}
                    </Table.Body>
                </Table>
            );
        }
    }

    return (
        <div>
            <FilterBar setPcId={setPcId} setDate={setDate} setPage={setPage}/>
            <div className="p-5" style={{position:"relative", zIndex: 0}}>
                {renderTable()}
                <div className="pt-3 flex justify-center w-full">
                    <button className={`bg-gray-100 p-2 rounded-l-xl ${isFirstPage ? 'cursor-not-allowed' : ''}`} disabled={isFirstPage} onClick={handlePrevPage}>
                        <ChevronLeftIcon
                            className="h-5 w-5 text-gray-400"
                            aria-hidden="true"
                        />
                    </button>
                    <div className="bg-gray-100 grid items-center">
                        <button className="rounded-xl bg-purple-700 w-10 h-10 font-bold shadow-[0_2px_10px_#BC8EE9]">
                            {page}
                        </button>
                    </div>
                    <button className={`bg-gray-100 p-2 rounded-r-xl ${isLastPage ? 'cursor-not-allowed' : ''}`} disabled={isLastPage} onClick={handleNextPage}>
                        <ChevronRightIcon
                            className="h-5 w-5 text-gray-400"
                            aria-hidden="true"
                        />
                    </button>
                </div>
            </div>
        </div>
    );
}