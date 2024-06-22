import { React, useState, useEffect } from "react";
import Navbar from "@/components/Navbar/Navbar";
import UpdateOrderCards from "@/components/UpdateOrderCards/UpdateOrderCards";
import TowerLoader from "@/components/TowerLoader/TowerLoader";
import TypeWriter from "@/components/TypeWritter/TypeWritter";

const Index = () => {
  const [menuItems, setMenuItems] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchMenuItems = async () => {
      try {
        setIsLoading(true);
        const response = await fetch(`/api/cafe/order/all/${currentPage}`);
        const allMenu = await fetch(`/api/cafe/order/all/count`);
        const data = await response.json();
        const dataAll = await allMenu.json();
        setMenuItems(data);
        setTotalPages(Math.ceil(dataAll / 16));
        setIsLoading(false);
      } catch (error) {
        console.error(error);
        setIsLoading(false);
      }
    };

    fetchMenuItems();
  }, [currentPage]);

  const handlePageChange = (page) => {
    setCurrentPage(page);
    setMenuItems([]);
  };

  return (
    <div className="min-h-screen">
      <Navbar />
      <div className="flex justify-center items-center h-full">
        <TypeWriter />
      </div>
      <h1 className="text-4xl text-center my-8 font-bold">UPDATE ORDER</h1>
      <div className="container mx-auto mt-4">
        {isLoading ? (
          <div className="flex justify-center items-center h-full pt-16">
            <TowerLoader />
          </div>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
            {menuItems.map((menuItem) =>
              menuItem.orderDetailsList.map((orderDetail) => (
                <UpdateOrderCards
                  key={orderDetail.id}
                  name={orderDetail?.menuItem?.name ?? "Menu telah dihapus"}
                  price={orderDetail?.menuItem?.price ?? "-"}
                  quantity={orderDetail.quantity}
                  noPc={orderDetail?.noPC ?? "-"}
                  noRuangan={orderDetail?.noRuangan ?? "-"}
                  orderID={orderDetail.id}
                  sessionID={menuItem.session}
                  status={orderDetail.status}
                />
              ))
            )}
          </div>
        )}
      </div>
      <div className="flex justify-center mt-8 mb-32">
        {Array.from({ length: totalPages }, (_, index) => (
          <button
            key={index + 1}
            className={`mx-2 px-4 py-2 rounded-md ${
              currentPage === index + 1
                ? "bg-blue-500 text-white"
                : "bg-gray-200 text-gray-700"
            }`}
            onClick={() => handlePageChange(index + 1)}
          >
            {index + 1}
          </button>
        ))}
      </div>
    </div>
  );
};

export default Index;
