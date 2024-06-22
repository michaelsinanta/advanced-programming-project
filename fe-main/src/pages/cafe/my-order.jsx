import axios from "axios";
import { useEffect, useState } from "react";
import styles from "@/styles/Home.module.css";
import Link from "next/link";
import useWarnetSession from "@/components/warnet/hooks/useSession";
import { useRouter } from "next/router";
import { toast } from "react-toastify";
import Image from "next/image";

export default function Cart() {
  const [myOrder, setMyOrder] = useState([]);
  const { getSessionId } = useWarnetSession();
  const router = useRouter();
  const [orderUpdated, setOrderUpdated] = useState(false);

  useEffect(() => {
    const session = getSessionId();

    if (session == undefined || session == null) {
      router.replace("/warnet");
    }

    axios.get(`/api/cafe/order/${session}`).then(function (response) {
      setMyOrder(response.data);
      setOrderUpdated(false);
    });
  }, [router, orderUpdated]);

  const handleCancelOrder = async (orderDetailsId) => {
    try {
      axios.put(`/api/cafe/order/update/${orderDetailsId}?status=cancel`);
      setOrderUpdated(true);
      toast.success("Order successfully cancelled!");
    } catch (error) {
      toast.error("An error occurred. Please try again.");
    }
  };

  return (
    <main className={styles.main}>
      <div className="flex flex-col">
        <div className="w-full flex md:justify-end gap-5 justify-center mb-5 md:mb-0">
          <Link Link href={"/cafe"}>
            <p>Our Menu</p>
          </Link>
          <Link href={"/cafe/cart"}>
            <p>Cart</p>
          </Link>
        </div>
        <h1 className="text-4xl text-center font-bold">My Orders</h1>
        <div>
          {myOrder.length === 0 ? (
            <div className="flex flex-col w-full items-center my-10 justify-center">
              <Image width="400" height="500" src="/cafe/empty-order.png" />
              <h1 className="text-2xl font-semibold">
                Oops! Your order is empty!
              </h1>
              <p>Looks like you haven&apos;t order anything yet</p>
              <button className="mt-10 p-2 bg-gray-300 hover:bg-gray-400 rounded-md font-bold text-lg w-full">
                <Link href={"/cafe"}>
                  <p>Go Shopping</p>
                </Link>
              </button>
            </div>
          ) : (
            <div className="grid lg:grid-cols-2 md:grid-cols-1 grid-cols-1 gap-8 mt-8">
              {myOrder.map((o, i) => (
                <div
                  key={o.id}
                  className="rounded-lg shadow-md p-4 shadow-indigo-300 hover:shadow-lg flex-grow w-full outline outline-2 outline-offset-2 flex flex-col"
                >
                  <div className="font-semibold text-purple-700 mb-4">
                    Order {i + 1}
                  </div>
                  <div className="grid lg:grid-cols-2 md:grid-cols-1 grid-cols-1 gap-5">
                    {(Array.isArray(o.orderDetailsList)
                      ? o.orderDetailsList
                      : [o.orderDetailsList]
                    ).map((item, j) => (
                      <div
                        key={j}
                        className="mb-2 bg-violet-200 p-2 rounded-md"
                      >
                        <div className=" text-gray-900">
                          Name:{" "}
                          {item.menuItem?.name
                            ? item.menuItem.name
                            : "Menu telah dihapus"}
                        </div>
                        <div>
                          Quantity:{" "}
                          <span className="text-gray-500">
                            {item.menuItem?.price ? item.quantity : "-"}
                          </span>
                        </div>
                        <div>
                          Price:{" "}
                          <span className="text-gray-500">
                            {item.menuItem?.price ? item.menuItem.price : "-"}
                          </span>
                        </div>
                        <div>
                          Total Price:{" "}
                          <span className="text-gray-500">
                            {item.menuItem?.price ? item.totalPrice : "-"}
                          </span>
                        </div>
                        <div>
                          Status:{" "}
                          <span
                            className={`${
                              item.status === "Dibatalkan"
                                ? "text-red-500 font-bold"
                                : "text-blue-800 font-semibold "
                            }`}
                          >
                            {item.status}
                          </span>
                        </div>
                        <div className="w-full flex justify-end mt-3">
                          <button
                            className={`p-2 rounded-md bg-indigo-50 ${
                              item.totalPrice === 0 ||
                              item.status !== "Menunggu Konfirmasi"
                                ? "text-gray-400"
                                : "hover:shadow-indigo-700 hover:shadow-md transition-all duration-100 ease-in-out"
                            }`}
                            onClick={() => {
                              if (
                                item.totalPrice === 0 ||
                                item.status !== "Menunggu Konfirmasi"
                              ) {
                                toast.error(
                                  `Pesanan tidak dapat dibatalkan karena sudah ${item.status.toLowerCase()}!`
                                );
                                return;
                              }
                              handleCancelOrder(item.id);
                            }}
                          >
                            Batalkan
                          </button>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </main>
  );
}
