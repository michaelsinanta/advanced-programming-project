"use client";

import { createContext, useContext, useEffect, useState, useRef } from "react";
import { toast } from "react-toastify";

const LOCAL_STORAGE_KEY = "cart";

const OrderContext = createContext();

export function OrderContextProvider({ children }) {
  const initialRender = useRef(true);

  const [order, setOrder] = useState([]);

  useEffect(() => {
    if (JSON.parse(localStorage.getItem(LOCAL_STORAGE_KEY))) {
      const storedCartItems = JSON.parse(
        localStorage.getItem(LOCAL_STORAGE_KEY)
      );
      setOrder([...storedCartItems]);
    }
  }, []);

  useEffect(() => {
    if (initialRender.current) {
      initialRender.current = false;
      return;
    }
    window.localStorage.setItem(LOCAL_STORAGE_KEY, JSON.stringify(order));
  }, [order]);

  const addOrder = (newOrder) => {
    const { id, name, stock, price } = newOrder;
    const existingOrder = order.find((o) => o.menuItem === id);
  
    if (existingOrder) {
      if (existingOrder.quantity < stock) {
        existingOrder.quantity++;
        setOrder([...order]);
        toast.success(`Successfully added ${name} on cart`)
      } else {
        toast.error('Sorry, the stock for this item is not sufficient.');
      }
    } else {
      if (stock > 0) {
        order.push({
          name: name,
          quantity: 1,
          menuItem: id,
        });
        setOrder([...order]);
        toast.success(`Successfully added ${name} on cart`)
      } else {
        toast.error('Sorry, this item is out of stock.');
      }
    }
  };

  const incrementOrder = (idMenuItem, stock) => {
    const index = order.findIndex((o) => o.menuItem === idMenuItem);
  
    if (index !== -1) {
      if (order[index].quantity < stock) {
        order[index].quantity++;
        setOrder([...order]);
      } else {
        toast.error(`Maximum stock reached for ${order[index].name}`);
      }
    }
  };

  const decrementOrder = (idMenuItem) => {
    const index = order.findIndex((o) => o.menuItem === idMenuItem);
  
    if (index !== -1) {
      if (order[index].quantity > 1) {
        order[index].quantity--;
        setOrder([...order]);
      } else {
        const updatedOrder = [...order];
        updatedOrder.splice(index, 1);
        setOrder(updatedOrder);
      }
    }
  };

  const deleteOrder = (idMenuItem) => {
    const updatedOrder = order.filter((o) => o.menuItem !== idMenuItem);
    setOrder(updatedOrder);
  };

  const value = {
    order,
    addOrder,
    incrementOrder,
    decrementOrder,
    setOrder,
    deleteOrder,
  };

  return (
    <OrderContext.Provider value={value}>{children}</OrderContext.Provider>
  );
}

export const useOrderContext = () => useContext(OrderContext);
