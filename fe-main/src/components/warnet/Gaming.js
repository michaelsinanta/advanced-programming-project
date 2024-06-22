import Link from "next/link";
import React, {Fragment, useState} from "react";
import axios from "axios";
import { Dialog, Transition } from '@headlessui/react'
import { useRouter } from 'next/router'
import { toast } from "react-toastify";

export default function Gaming({ sessionId, timeRemaining, setHasActiveSession, stopAlert }) {
    const router = useRouter()
    const [isOpen, setIsOpen] = useState(false)
    function closeModal() {
        setIsOpen(false);
    }

    function openModal() {
        setIsOpen(true);
    }
    const handleEndSession = () => {
      stopAlert()
        axios.post('/api/warnet/sewa_pc/session_done/' + sessionId)
            .then(() => {
                setHasActiveSession(false);
                toast.success("Sesi berhasil diakhiri")
                router.push('/invoice')
            })

    }

  return (
    <div style={{ backgroundColor: "#f2f2f2", minHeight: "100vh" }}>
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          padding: "20px",
        }}
      >
        <div>
          <strong>Session ID:</strong> {sessionId}
        </div>
        <div>
          <strong>Waktu tersisa:</strong> {timeRemaining}
        </div>
      </div>
      <div
        style={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          minHeight: "70vh",
        }}
      >
        <img
          src="/warnet/kohakuGaming.gif"
          style={{ maxWidth: "100%", maxHeight: "100%" }}
        />
      </div>
      <div
        style={{ display: "flex", justifyContent: "center", padding: "20px" }}
      >
        <Link
          href="/warnet/perpanjang"
          className="bg-green-600 px-4 py-2 rounded text-white font-medium mr-3"
        >
          Perpanjang sesi
        </Link>
        <button
          className="bg-red-500 px-4 py-2 rounded text-white font-medium mr-3"
          onClick={openModal}
        >
          Akhiri sesi
        </button>
      </div>

        <Transition appear show={isOpen} as={Fragment}>
            <Dialog
                as="div"
                className="fixed inset-0 z-10 overflow-y-auto"
                onClose={closeModal}
            >
                <div className="min-h-screen px-4 text-center">
                    <Transition.Child
                        as={Fragment}
                        enter="ease-out duration-300"
                        enterFrom="opacity-0"
                        enterTo="opacity-100"
                        leave="ease-in duration-200"
                        leaveFrom="opacity-100"
                        leaveTo="opacity-0"
                    >
                        <Dialog.Overlay className="fixed inset-0" />
                    </Transition.Child>

                    {/* This element is to trick the browser into centering the modal contents. */}
                    <span
                        className="inline-block h-screen align-middle"
                        aria-hidden="true"
                    >

            </span>
                    <Transition.Child
                        as={Fragment}
                        enter="ease-out duration-300"
                        enterFrom="opacity-0 scale-95"
                        enterTo="opacity-100 scale-100"
                        leave="ease-in duration-200"
                        leaveFrom="opacity-100 scale-100"
                        leaveTo="opacity-0 scale-95"
                    >
                        <div className="inline-block w-full max-w-md p-6 my-8 overflow-hidden align-middle transition-all transform bg-white shadow-xl rounded-2xl">
                            <Dialog.Title
                                as="h3"
                                className="text-lg font-medium leading-6 text-gray-900"
                            >
                                Yakin ingin mengakhiri sesi?
                            </Dialog.Title>
                            <div className="mt-2">
                                <p className="text-sm text-gray-500 border-t pt-2">
                                    Sesi akan diakhiri secara permanen
                                </p>
                            </div>

                            <div className="mt-4">
                                <button
                                    type="button"
                                    className="font-medium inline-flex justify-center px-4 py-2 mr-2 text-sm text-blue-900 bg-blue-200 border border-transparent rounded-md hover:bg-red-200 duration-300"
                                    onClick={closeModal}
                                >
                                    Cancel
                                </button>
                                <button
                                    type="button"
                                    className="font-medium inline-flex justify-center px-4 py-2 text-sm text-white bg-red-500 border border-transparent rounded-md hover:bg-red-200 duration-300"
                                    onClick={handleEndSession}
                                >
                                    Ya, akhiri sesi saya
                                </button>
                            </div>
                        </div>
                    </Transition.Child>
                </div>
            </Dialog>
        </Transition>
    </div>
  );
}
