import { useEffect, useState } from "react";
import axios from "axios";
import styles from "@/styles/Invoices.module.css";
import ModalInvoice from "@/components/ModalInvoice/ModalInvoice.js";
import BillItemCard from "@/components/BillCard/BillCard";
import { BiCartAlt } from "react-icons/bi";
import useWarnetSession from "@/components/warnet/hooks/useSession";
import NoActiveSession from "@/components/warnet/NoActiveSession";

export default function Invoice() {
  const [showInvoiceModal, setShowInvoiceModal] = useState(false);
  const [invoiceItems, setInvoiceItems] = useState([]);
  const [isSessionActive, setIsSessionActive] = useState(false);
  const { getSessionId } = useWarnetSession();

  useEffect(() => {
    if (getSessionId()) {
      setIsSessionActive(true);
    }
  }, []);

  useEffect(() => {
    if (isSessionActive) {
      axios
        .get(`/api/bayar/invoices/${getSessionId()}`)
        .then(function (response) {
          setInvoiceItems(response.data.content);
        });
    }
  }, [isSessionActive]);

  return !isSessionActive ? (
    <NoActiveSession />
  ) : (
    <div className={styles.container}>
      <div className={styles.top}>
        <h1 className={styles.pageTitle}>Invoice</h1>
        <div className="grid md:grid-cols-1 lg:grid-cols-1 sm: grid-cols-1 gap-8 mt-8">
          {invoiceItems?.bills?.map((bill) => (
            <BillItemCard key={bill.id} bill={bill} />
          ))}
        </div>
      </div>
      <div className={styles.bottom}>
        <div className={styles.wrapper}>
          <button
            onClick={() => setShowInvoiceModal(true)}
            className={styles.buttonCheckOut}
          >
            <h1 className={styles.titleCheckout}>Checkout</h1>
            <BiCartAlt size={25} color="blue" />
          </button>
          <ModalInvoice
            onClose={() => setShowInvoiceModal(false)}
            show={showInvoiceModal}
            invoiceItems={invoiceItems}
          />
        </div>
      </div>
    </div>
  );
}
