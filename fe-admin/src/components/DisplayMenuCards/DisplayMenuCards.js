import React, { useState, useEffect } from "react";
import MenuItemCard from "../MenuItemCard/MenuItemCard";
import MenuItemForm from "../MenuItemForm/MenuItemForm";
import BrewLoader from "../BrewLoader/BrewLoader";
import axios from "axios";
import { useRouter } from "next/router";
import { toast } from "react-toastify";

const CreateMenuItem = () => {
  const [menuItems, setMenuItems] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [newMenuItem, setNewMenuItem] = useState({
    name: "",
    stock: 0,
    price: 0,
  });
  const [selectedMenuItemId, setSelectedMenuItemId] = useState(null);
  const [selectedMenuItemName, setSelectedMenuItemName] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const router = useRouter();

  const refreshData = () => {
    router.reload();
  };

  useEffect(() => {
    fetch("/api/cafe/menu/all")
      .then((response) => response.json())
      .then((data) => setMenuItems(data))
      .catch((error) => toast.error(error));
  }, []);

  const handleNewMenuItemChange = (event) => {
    const { name, value } = event.target;
    setNewMenuItem((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleUpdateMenuItem = (id, name) => {
    setSelectedMenuItemId(id);
    setSelectedMenuItemName(name);
    setShowUpdateModal(true);
  };

  const validateMenuItem = () => {
    if (
      newMenuItem.name === "" ||
      isNaN(parseInt(newMenuItem.stock)) ||
      isNaN(parseInt(newMenuItem.price))
    ) {
      toast.error("Please provide all required input");
      return false;
    } else if (parseInt(newMenuItem.stock) < 0) {
      toast.error("Menu stock cannot be lower than 0.");
      return false;
    } else if (parseInt(newMenuItem.price) < 0) {
      toast.error("Menu price cannot be lower than 0.");
      return false;
    }
    return true;
  };

  const handleSubmitUpdate = () => {
    if (!validateMenuItem()) {
      return;
    }

    if (
      menuItems.some(
        (item) =>
          item.name.toLowerCase() === newMenuItem.name.toLowerCase() &&
          item.name.toLowerCase() !== selectedMenuItemName.toLowerCase()
      )
    ) {
      toast.error("A menu item with the same name already exists.");
      return;
    }

    fetch(`/api/cafe/menu/update/${selectedMenuItemId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(newMenuItem),
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.httpStatus == "BAD_REQUEST") {
          console.log(data);
          toast.error(data.message);
        } else {
          const updatedMenuItems = menuItems.map((menuItem) =>
            menuItem.id === selectedMenuItemId ? data : menuItem
          );
          setMenuItems(updatedMenuItems);
          setNewMenuItem({
            name: "",
            stock: 0,
            price: 0,
          });
          setSelectedMenuItemId(null);
          setSelectedMenuItemName("");
          setShowUpdateModal(false);
          toast.success("Succesfully update menu!");
        }
      })
      .catch((error) => toast.error(error));
  };

  const handleAddMenuItem = () => {
    if (!validateMenuItem()) {
      return;
    }

    if (
      menuItems.some(
        (item) => item.name.toLowerCase() === newMenuItem.name.toLowerCase()
      )
    ) {
      toast.error("A menu item with the same name already exists.");
      return;
    }

    setIsLoading(true);
    fetch("/api/cafe/menu/create", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(newMenuItem),
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.httpStatus == "BAD_REQUEST") {
          console.log(data);
          toast.error(data.message);
        } else {
          setMenuItems((prevState) => [...prevState, data]);
          setNewMenuItem({
            name: "",
            stock: 0,
            price: 0,
          });
          setShowModal(false);
          toast.success("Succesfully create menu!");
        }
      })
      .catch((error) => toast.error(error))
      .finally(() => {
        setIsLoading(false);
      });
  };

  const handleCancelUpdate = () => {
    setSelectedMenuItemId(null);
    setShowUpdateModal(false);
    setNewMenuItem({
      name: "",
      stock: 0,
      price: 0,
    });
  };

  return (
    <div className="items-center m-8">
      <div className="flex justify-center items-center h-full">
        <BrewLoader />
      </div>
      <h1 className="text-4xl text-center my-8 font-bold">MENU ITEMS</h1>
      <div className="mt-4 text-center mb-8">
        <button
          className="bg-blue-600 p-4 rounded font-semibold"
          onClick={() => setShowModal(true)}
        >
          Add New Menu Item
        </button>
      </div>
      {showModal && (
        <MenuItemForm
          handleAddMenuItem={handleAddMenuItem}
          setShowModal={setShowModal}
          newMenuItem={newMenuItem}
          handleNewMenuItemChange={handleNewMenuItemChange}
          isLoading={isLoading}
        />
      )}
      {showUpdateModal && (
        <div className="fixed inset-0 flex items-center justify-center">
          <div className="bg-white rounded-lg shadow-lg p-6">
            <h2 className="text-lg font-semibold mb-4 text-black">
              Update Menu Item
            </h2>
            <div className="mb-4">
              <label htmlFor="name" className="mb-2 text-black">
                Name:
              </label>
              <input
                type="text"
                id="name"
                name="name"
                value={newMenuItem.name}
                onChange={handleNewMenuItemChange}
                className="border border-gray-300 p-2 rounded"
                autoComplete="off"
              />
            </div>
            <div className="mb-4">
              <label htmlFor="stock" className="mb-2 text-black">
                Stock:
              </label>
              <input
                type="number"
                id="stock"
                name="stock"
                value={newMenuItem.stock}
                onChange={handleNewMenuItemChange}
                className="border border-gray-300 p-2 rounded"
                autoComplete="off"
              />
            </div>
            <div className="mb-4">
              <label htmlFor="price" className="mb-2 text-black">
                Price:
              </label>
              <input
                type="number"
                id="price"
                name="price"
                value={newMenuItem.price}
                onChange={handleNewMenuItemChange}
                className="border border-gray-300 p-2 rounded"
                autoComplete="off"
              />
            </div>
            <div className="flex justify-end">
              <button
                className="bg-blue-500 text-white py-2 px-4 rounded mr-2 font-bold hover:bg-blue-800"
                onClick={handleSubmitUpdate}
              >
                Update
              </button>
              <button
                className="bg-red-500 text-white py-2 px-4 rounded font-bold hover:bg-red-800"
                onClick={handleCancelUpdate}
              >
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}

      <div className="grid lg:grid-cols-3 md:grid-cols-2 grid-cols-1 gap-x-4">
        {menuItems.map((menuItem, index) => (
          <div className="col-md-4 mb-4 mx-4 flex" key={menuItem.name || index}>
            <MenuItemCard
              {...menuItem}
              onDelete={async () => {
                await axios
                  .delete(`/api/cafe/menu/delete/${menuItem.id}`)
                  .then(() => {
                    const newMenuItems = menuItems.filter(
                      (item) => item !== menuItem
                    );
                    setMenuItems(newMenuItems);
                    toast.success("Succesfully delete menu!");
                  });
              }}
              onUpdate={() => handleUpdateMenuItem(menuItem.id, menuItem.name)}
            />
          </div>
        ))}
      </div>
    </div>
  );
};

export default CreateMenuItem;
