import React, { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { useRouter } from "next/router";
import styles from "../../styles/Modal.module.css";
import stylesInvoices from "../../styles/Invoices.module.css";
import { SlClose } from "react-icons/sl";
import { BiCartAlt } from "react-icons/bi";
import axios, { AxiosError } from "axios";
import useWarnetSession from "@/components/warnet/hooks/useSession";
import { toast } from "react-toastify";
import Footer from "./Footer";
import CouponCode from "./CouponCode";

const Modal = ({ show, onClose, invoiceItems, title }) => {
  const [banks, setBanks] = useState([]);
  const [bankId, setBankId] = useState();
  const [radioValue, setRadioValue] = useState("");
  const [optionAdminFee, setOptionAdminFee] = useState();
  const [couponCode, setCouponCode] = useState("");

  const { register, handleSubmit } = useForm();
  const { removeSession } = useWarnetSession();
  const router = useRouter();

  const handleCloseClick = (e) => {
    e.preventDefault();
    onClose();
  };

  useEffect(() => {
    axios.get(`/api/bayar/banks`).then(function (response) {
      setBanks(response.data.content);
    });
  }, []);

  const onCheckout = (data) => {
    const invoice = {
      sessionId: invoiceItems.sessionId,
      totalAmount: invoiceItems.totalAmount,
      paymentMethod: data.paymentMethod,
      bankId: bankId,
    };

    axios
      .post(`/api/bayar/invoices/${invoiceItems.id}/payments`, invoice)
      .then(() => {
        removeSession();
        toast.success("Paid!", {
          pauseOnHover: false,
          pauseOnFocusLoss: false,
        });
        router.push("/");
      })
      .catch((error) => {
        if (error instanceof AxiosError) {
          if (error.response?.status === 400) {
            toast.error(error.response.data.message, {
              pauseOnHover: false,
              pauseOnFocusLoss: false,
            });
          } else {
            toast.error("Oops, ada kesalahan!");
          }
        } else {
          toast.error(error.message);
        }
      });
  };

  const handleChange = (event) => {
    setRadioValue(event.target.value);
    setOptionAdminFee("");
  };

  const displayAdminFee = (adminFee) => {
    setOptionAdminFee(adminFee);
  };

  const handleCouponCode = () => {
    axios
      .post(`/api/bayar/sessions/${invoiceItems.sessionId}/coupons/use`, {
        name: couponCode,
      })
      .then(() => {
        toast.success("Coupon Applied!");
        window.location.reload();
      })
      .catch((error) => {
        if (error instanceof AxiosError) {
          if (
            error.response?.status === 400 ||
            error.response?.status === 404
          ) {
            toast.error(error.response.data.message, {
              pauseOnHover: false,
              pauseOnFocusLoss: false,
            });
          } else {
            toast.error("Oops, ada kesalahan!");
          }
        } else {
          toast.error(error.message);
        }
      });
  };

  return show ? (
    <div className={styles.overlay}>
      <div className={styles.modal}>
        <div className={styles.header}>
          <a href="#" onClick={handleCloseClick}>
            <SlClose size={25} color="blue" />
          </a>
        </div>
        <div className={styles.title}>
          <h2 className="text-2xl font-bold ml-3 pb-5">Select Payment</h2>
        </div>
        {title && <div className={styles.title}>{title}</div>}
        <form onSubmit={handleSubmit(onCheckout)} className={styles.modalBody}>
          <div className="w-full mx-auto rounded-lg bg-white border border-gray-200 text-gray-800 font-light mb-6">
            <div className="w-full p-3 border-b border-gray-200">
              <div className="mb-5">
                <label
                  htmlFor="BANK"
                  className="flex items-center cursor-pointer"
                >
                  <input
                    type="radio"
                    className="form-radio h-5 w-5 text-indigo-500"
                    {...register("paymentMethod")}
                    value="BANK"
                    id="BANK"
                    checked={radioValue === "BANK"}
                    onChange={handleChange}
                  />
                  <h2 className="text-xl font-bold text-indigo-600 ml-3">
                    Bank Transfer
                  </h2>
                </label>
              </div>
              {radioValue === "BANK" && (
                <div className="mb-3 -mx-2 flex items-end">
                  <div className="px-2 w-full">
                    <label className="text-gray-600 font-semibold text-sm mb-2 ml-1">
                      Banks
                    </label>
                    <select
                      className="form-select w-full px-3 py-2 mb-1 border border-gray-200 rounded-md focus:outline-none focus:border-indigo-500 transition-colors cursor-pointer"
                      defaultValue={"DEFAULT"}
                      onChange={(e) => setBankId(e.target.value)}
                    >
                      <option value="DEFAULT" disabled>
                        Pilih Bank
                      </option>
                      {banks?.map((bank, key) => (
                        <option
                          key={key}
                          id={bank.id}
                          value={bank.id}
                          onClick={() => displayAdminFee(bank.adminFee)}
                        >
                          {bank.name}
                        </option>
                      ))}
                    </select>
                  </div>
                </div>
              )}
            </div>
            <div className="w-full p-3">
              <label
                htmlFor="CASH"
                className="flex items-center cursor-pointer"
              >
                <input
                  type="radio"
                  className="form-radio h-5 w-5 text-indigo-500"
                  {...register("paymentMethod")}
                  value="CASH"
                  id="CASH"
                  checked={radioValue === "CASH"}
                  onChange={handleChange}
                />
                <h2 className="text-xl font-bold text-indigo-600 ml-3">Cash</h2>
              </label>
            </div>
          </div>

          <CouponCode
            radioValue={radioValue}
            couponCode={couponCode}
            setCouponCode={setCouponCode}
            handleCouponCode={handleCouponCode}
          />

          <Footer
            radioValue={radioValue}
            invoiceItems={invoiceItems}
            optionAdminFee={optionAdminFee}
          />

          <button type="submit" className={stylesInvoices.buttonPay}>
            <h1 className={stylesInvoices.titleCheckout}>Pay Now!</h1>
            <BiCartAlt size={25} color="blue" />
          </button>
        </form>
      </div>
    </div>
  ) : null;
};
export default Modal;
