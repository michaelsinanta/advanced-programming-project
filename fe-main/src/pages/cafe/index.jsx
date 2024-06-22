import axios from "axios";
import { useEffect, useState } from "react";
import styles from "@/styles/Home.module.css";
import Link from "next/link";
import MenuItemCard from "@/components/Elements/MenuItemCard/MenuItemCard";
import useWarnetSession from "@/components/warnet/hooks/useSession";
import { useRouter } from "next/router";
import Image from "next/image";

export default function Menu() {
  const [menuItem, setMenuItems] = useState([]);
  const { getSessionId } = useWarnetSession();
  const router = useRouter();

  useEffect(() => {
    const session = getSessionId();

    if (session == undefined || session == null) {
      router.replace("/warnet");
    }
  }, [getSessionId, router]);

  useEffect(() => {
    axios.get("/api/cafe/menu/all?query=available").then(function (response) {
      setMenuItems(response.data);
    });
  }, []);

  return (
    <main className={styles.main}>
      <div className="flex flex-col">
        <div className="w-full flex md:justify-end gap-5 justify-center mb-5 md:mb-0">
          <Link href={"/cafe/cart"}>
            <p>Cart</p>
          </Link>
          <Link href={"/cafe/my-order"}>
            <p>My Order</p>
          </Link>
        </div>
        <h1 className="text-4xl text-center font-bold">Our Menu</h1>
        {menuItem.length === 0 ? (
          <div className="flex flex-col w-full items-center my-10 justify-center">
            <Image width="400" height="500" src="/cafe/empty-menu.png" />
            <h1 className="text-2xl font-semibold">Oops! The menu is empty!</h1>
            <p>Please bear with us for just a moment while we replenish it for your warnet pleasure!</p>
          </div>
        ) : (
          <div className="grid md:grid-cols-2 lg:grid-cols-3 sm: grid-cols-1 gap-8 mt-8">
            {menuItem.map((menu) => (
              <MenuItemCard key={menu.id} menu={menu} />
            ))}
          </div>
        )}
      </div>
    </main>
  );
}
