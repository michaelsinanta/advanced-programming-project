import React, { useState, useEffect } from "react";
import CouponCard from "./CouponCard";
import axios, { AxiosError } from "axios";
import AddCouponForm from "./AddCouponForm";
import TowerLoader from "../TowerLoader/TowerLoader";
import { toast } from "react-toastify";

const DisplayCoupons = () => {
  const [coupons, setCoupons] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [showModalAdd, setShowModalAdd] = useState(false);
  const [newCoupon, setNewCoupon] = useState({
    name: "",
    discount: 0,
  });

  useEffect(() => {
    axios.get(`/api/bayar/coupons/getAll`).then(function (response) {
      setCoupons(response.data.content);
      setIsLoading(false);
    });
  }, []);

  const handleCouponChange = (event) => {
    const { name, value } = event.target;
    setNewCoupon((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleAddCoupon = () => {
    if (!newCoupon.name || !newCoupon.discount) {
      toast.warning("Please provide all required input");
      return;
    }
    if (newCoupon.discount < 0) {
      toast.error("Discount cannot be negative.");
      return;
    }

    axios
      .post(`/api/bayar/coupons/createCoupon`, newCoupon)
      .then((response) => {
        setCoupons(coupons.concat(response.data.content));
        setShowModalAdd(false);
      })
      .catch((error) => {
        if (error instanceof AxiosError) {
          if (
            error.response?.status === 400 ||
            error.response?.status === 409
          ) {
            console.log(error.response.data);
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
      })
      .finally(() => {
        setNewCoupon({
          name: "",
          discount: 0,
        });
        setShowModalAdd(false);
      });
  };

  const handleDeleteCoupon = (coupon) => {
    axios.delete(`/api/bayar/coupons/delete/${coupon.id}`).then(() => {
      const newCoupons = coupons.filter(
        (couponDetail) => couponDetail !== coupon
      );
      setCoupons(newCoupons);
    });
  };

  const handleEditCoupon = (id) => {
    const url = `/api/bayar/coupons/${id}`;
    const coupon = coupons.find((c) => c.id === id);

    if (newCoupon.discount < 0) {
      toast.error("Admin Fee cannot be negative.");
      return;
    }
    if (newCoupon.name.length === 0) {
      newCoupon.name = coupon.name;
    }
    if (newCoupon.discount === 0) {
      newCoupon.discount = coupon.discount;
    }

    axios
      .put(url, newCoupon)
      .then((response) => {
        const content = response.data.content;
        setCoupons(coupons.map((c) => (c.id !== id ? c : content)));
      })
      .catch((error) => {
        if (error instanceof AxiosError) {
          if (
            error.response?.status === 400 ||
            error.response?.status === 409
          ) {
            console.log(error.response.data);
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
      })
      .finally(() => {
        setNewCoupon({
          name: "",
          discount: 0,
        });
      });
  };

  if (isLoading) {
    return (
      <>
        <div className="items-center m-8">
          <h1 className="text-4xl text-center my-8 font-bold">Coupons</h1>
        </div>
        <div className="flex justify-center items-center h-full mt-32">
          <TowerLoader />
        </div>
      </>
    );
  }

  return (
    <div className="items-center m-8">
      <h1 className="text-4xl text-center my-8 font-bold">Coupons</h1>
      <div className="mt-4 text-center mb-8">
        <button
          className="bg-blue-600 p-4 rounded font-semibold"
          onClick={() => setShowModalAdd(true)}
        >
          Add New Coupon
        </button>
      </div>

      {showModalAdd && (
        <AddCouponForm
          handleAddCoupon={handleAddCoupon}
          setShowModalAdd={setShowModalAdd}
          newCoupon={newCoupon}
          handleCouponChange={handleCouponChange}
        />
      )}

      <div className="grid lg:grid-cols-3 md:grid-cols-2 grid-cols-1 gap-x-4">
        {coupons?.map((coupon) => (
          <div className="col-md-4 mb-4 mx-4 flex" key={coupon.id}>
            <CouponCard
              coupon={coupon}
              removeCoupon={handleDeleteCoupon}
              newCoupon={newCoupon}
              handleEditCoupon={() => handleEditCoupon(coupon.id)}
              handleCouponChange={handleCouponChange}
            />
          </div>
        ))}
      </div>
    </div>
  );
};

export default DisplayCoupons;
