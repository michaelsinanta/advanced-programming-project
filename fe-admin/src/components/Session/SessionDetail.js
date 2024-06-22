import { Table, Loading } from "@nextui-org/react";
import React, { useEffect, useState } from "react";
import axios from "axios";
import { YesIcon } from "@/components/Session/YesIcon";
import { NoIcon } from "@/components/Session/NoIcon";
import {formatDatetime} from "@/components/Session/formatDatetime";
import {formatRupiah} from "@/components/PaymentLogs/formatRupiah";

export default function SessionDetail({ id }) {
  const [isLoading, setIsLoading] = useState(true);
  const [sessionPricingList, setSessionPricingList] = useState([]);
  const [session, setSession] = useState([]);

  useEffect(() => {
    setIsLoading(true);
    if (id) {
      axios
        .get(`/api/warnet/info_sesi/session_detail/${id}`)
        .then(async (details) => {
          setSession(details.data.session);
          const list = details.data.sessionPricingList;
          
          await Promise.all(list.map(async (item) => {
            if (item.pricing.makananId != null) {
              return axios.get(`/api/cafe/menu/id/${item.pricing.makananId}`)
                .then((response) => {
                  item.pricing.makananId = response.data.name;
                });
            }
          }))
          
          setSessionPricingList(list);
          setIsLoading(false);
        })
        .catch((error) => console.log(error))
        
        
    }
  }, [id]);

  const columnsPricingList = [
    { key: "pricing.id", label: "PRICING ID" },
    { key: "pricing.name", label: "PRICING NAME" },
    { key: "pricing.price", label: "PRICE" },
    { key: "pricing.duration", label: "DURATION" },
    { key: "pricing.isPaket", label: "PAKET" },
    { key: "pricing.makananId", label: "FOOD/BEVERAGE" },
    { key: "waktuPembelian", label: "PURCHASE TIME" },
    { key: "quantity", label: "QUANTITY" },
  ];

  const renderTable = () => {
    if (isLoading) {
      return (
        <div className="h-[27.3rem] w-full grid items-center">
          <Loading />
        </div>
      );
    } else {
      return (
        <Table
          bordered
          shadow={false}
          color="secondary"
          aria-label="Detail Session Table"
          css={{
            height: "auto",
            minWidth: "100%",
            bgColor: "$white",
          }}
          selectionMode="none"
        >
          <Table.Header columns={columnsPricingList}>
            {(column) => (
              <Table.Column key={column.key}>{column.label}</Table.Column>
            )}
          </Table.Header>
          <Table.Body items={sessionPricingList}>
            {(item) => (
              <Table.Row key={item.waktuPembelian}>
                {(columnKey) => (
                  <Table.Cell>{renderCell(item, columnKey)}</Table.Cell>
                )}
              </Table.Row>
            )}
          </Table.Body>
        </Table>
      );
    }
  };

  const renderCell = (item, columnKey) => {
    switch (columnKey) {
      case "pricing.id":
        return <p>{item.pricing.id}</p>;
      case "pricing.name":
        return <p>{item.pricing.name}</p>;
      case "pricing.price":
        return <p>{formatRupiah(item.pricing.price.toString(), "Rp")}</p>;
      case "pricing.duration":
        return <p>{item.pricing.duration} jam</p>;
      case "pricing.makananId":
        if (item.pricing.makananId === null) {
          return <p className="text-gray-400">N/A</p>;
        } else {
          return <p>{item.pricing.makananId}</p>;
        }
      case "waktuPembelian":
        return <p>{formatDatetime(item.waktuPembelian)}</p>;
      case "pricing.isPaket":
        return item.pricing.isPaket ? <YesIcon /> : <NoIcon />;
      default:
        return item[columnKey];
    }
  };

  const renderHeader = () => {
    if (!isLoading) {
      return (
        <div className="p-5 flex flex-col items-center" style={{ position: "relative", zIndex: 0 }}>
            <div className="container w-fit rounded-2xl p-5 bg-[#EADCF8] text-purple-700 drop-shadow-[0_4px_14px_rgba(188,142,233,0.5)] border-none">
              <p>
                <strong>Session ID:</strong> {session.id}
              </p>
              <p>
                <strong>PC:</strong> PC {session.pc.noPC} Ruangan{" "}
                {session.pc.noRuangan}
              </p>
              <p>
                <strong>Waktu Mulai:</strong>{" "}
                {formatDatetime(session.datetimeStart)}
              </p>
              <p>
                <strong>Waktu Selesai:</strong>{" "}
                {formatDatetime(session.datetimeEnd)}
              </p>
            </div>
        </div>
      );
    }
  };

  return (
    <div>
      {renderHeader()}
      <div className="p-5" style={{ position: "relative", zIndex: 0 }}>
        {renderTable()}
      </div>
    </div>
  );
}
