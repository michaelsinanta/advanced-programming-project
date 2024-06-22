import axios from "axios";
import { useEffect, useState } from "react";
import styles from "@/styles/Home.module.css";
import Link from "next/link";
import { useOrderContext } from "@/components/contexts/OrderContext";
import CartCard from "@/components/Elements/CartCard/CartCard";
import useWarnetSession from "@/components/warnet/hooks/useSession";
import Image from "next/image";
import { toast } from "react-toastify";

export default function Cart() {
  const { order, addOrder, incrementOrder, decrementOrder, setOrder, deleteOrder } =
    useOrderContext();
  const axios = require("axios");
  const { getSessionId } = useWarnetSession();
  const [isLoading, setIsLoading] = useState(false);
  const [session, setSession] = useState("");

  useEffect(() => {
    const session = getSessionId();

    if (session == undefined || session == null) {
      router.replace("/warnet");
    } else {
      setSession(session);
    }
  }, []);

  const handleSubmitOrder = (event) => {
    event.preventDefault();
    setIsLoading(true);

    const orderData = {
      session: session,
      orderDetailsData: [
        ...order.map((item) => ({
          menuItemId: item.menuItem,
          quantity: item.quantity,
        })),
      ],
    };

    axios
      .post("/api/cafe/order/create", orderData)
      .then((response) => {
        localStorage.removeItem("cart");
        setOrder([]);
        toast.success("Order successfully placed!");
      })
      .catch((error) => {
        toast.error(`The order can't be placed! ${error.response.data.message}. Please refresh the page`);
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  return (
    <main className={styles.main}>
      <div className="flex flex-col">
        <div className="w-full flex md:justify-end gap-5 justify-center mb-5 md:mb-0">
          <Link href={"/cafe"}>
            <p>Our Menu</p>
          </Link>
          <Link href={"/cafe/my-order"}>
            <p>My Order</p>
          </Link>
        </div>
        <h1 className="text-4xl text-center font-bold">Cart</h1>
        <div>
          {order.length === 0 ? (
            <div className="flex flex-col w-full items-center my-10 justify-center">
              <Image width="400" height="500" src="/cafe/empty-cart.png" />
              <h1 className="text-2xl font-semibold">
                Oops! Your cart is empty!
              </h1>
              <p>Looks like you haven&apos;t added anything to your cart yet</p>
              <button className="mt-10 p-2 bg-gray-300 hover:bg-gray-400 rounded-md font-bold text-lg w-full">
                <Link href={"/cafe"}>
                  <p>Go Shopping</p>
                </Link>
              </button>
            </div>
          ) : (
            <div>
              <div className="grid md:grid-cols-2 lg:grid-cols-3 sm:grid-cols-1 gap-8 mt-8">
                {order.map((o) => (
                  <CartCard
                    key={o.menuItem}
                    orderParam={o}
                    quantity={o.quantity}
                    decrementOrder={decrementOrder}
                    incrementOrder={incrementOrder}
                    deleteOrder={deleteOrder}
                  />
                ))}
              </div>
              <button
                className="mt-10 p-2 bg-indigo-400 hover:bg-indigo-300 rounded-md font-bold text-lg w-full text-white"
                onClick={handleSubmitOrder}
                disabled={order.length === 0 || isLoading}
              >
                {isLoading ? (
                  <div className="flex w-full justify-center items-center">
                    <svg
                      class="animate-spin -ml-1 mr-3 h-5 w-5 text-white"
                      xmlns="http://www.w3.org/2000/svg"
                      fill="none"
                      viewBox="0 0 24 24"
                    >
                      <circle
                        class="opacity-25"
                        cx="12"
                        cy="12"
                        r="10"
                        stroke="currentColor"
                        stroke-width="4"
                      ></circle>
                      <path
                        class="opacity-75"
                        fill="currentColor"
                        d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                      ></path>
                    </svg>
                    Processing
                  </div>
                ) : (
                  "Complete the Order"
                )}
              </button>
            </div>
          )}
        </div>
      </div>
    </main>
  );
}
