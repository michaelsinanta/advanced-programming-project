import { useRouter } from 'next/router';
import React, {useEffect, useState} from "react";
import GiveDiscountForm from './GiveDiscountForm'
import { DateTime } from 'luxon';

const PCCard = ({ id, noPC, noRuangan, beingUsed, activeSession, onDelete, showModal, setShowModal, giveDiscount }) => {
  const[isActive, setIsActive] = useState(false);
  const router = useRouter();
  const handleEditClick = () => {
    router.push(`/AdminWarnet/PC/Edit?id=${id}`);
  };

  useEffect(() => {
    if (!showModal) {
      setIsActive(false)
    }
  }, [showModal]);

  const dateTimeStart = DateTime.fromISO(activeSession?.datetimeStart, { zone: 'utc' });
  const dateTimeEnd = DateTime.fromISO(activeSession?.datetimeEnd, { zone: 'utc' });
  const userTimeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;

  return (
    <div className="bg-gradient-to-r from-pink-500 via-red-500 to-yellow-500 text-offwhite rounded p-4 flex flex-col flex-grow">
      <div className="bg-blue-800 mt-2 p-4 rounded-lg h-full">
        <div className="text-md mt-2">
          <span className= "font-bold">ID: </span>
          {id}
          {beingUsed ? (
            <>
              <div className="float-right">
                <button className= "btn text-white-500" onClick={() => {setShowModal(true); setIsActive(true)}}>View Invoice</button>
              </div>
              {showModal && isActive && (
              <GiveDiscountForm
                sessionId={activeSession?.sessionId}
                noPC={noPC}
                setShowModal={setShowModal}
                giveDiscount={giveDiscount}
              />
            )}
            </>
          ) : null}

        </div>
        <div className="text-md mt-2">
          <span className= "font-bold">No PC: </span>
          {noPC}
        </div>
        <div className="text-md mt-2">
          <span className= "font-bold">No Ruangan: </span>
          {noRuangan}
        </div>
        <p className={`text-md mt-2 font-bold ${beingUsed ? "text-red-500" : "text-green-500"}`}>{beingUsed ? "Sedang digunakan" : "Tersedia"}</p>
        <div style={{ display: beingUsed ? 'block' : 'none' }}>
          <div className="text-md mt-2">
            <span className= "font-bold">Session ID: </span>
            {activeSession?.sessionId}
          </div>
          <div className="text-md mt-2">
            <span className="font-bold">Start: </span>
            {dateTimeStart?.setZone(userTimeZone).toLocaleString(DateTime.DATETIME_FULL_WITH_SECONDS)}
          </div>
          <div className="text-md mt-2">
            <span className="font-bold">End: </span>
            {dateTimeEnd?.setZone(userTimeZone).toLocaleString(DateTime.DATETIME_FULL_WITH_SECONDS)}
          </div>
        </div>
        <br></br>

        {!beingUsed ? (
          <>
            <button className="btn text-white-500 float-left" onClick={handleEditClick}>
              Edit
            </button>
            <button className="btn text-red-500 float-right" onClick={onDelete}>
              Hapus
            </button>
          </>
        ) : null}
      </div>
    </div>
  );
};

export default PCCard;
