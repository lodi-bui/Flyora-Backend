import React, { useEffect, useState } from "react";
import { updateAccount } from "api/UpdateAccount";
import toast from "react-hot-toast";

const getRoleNameById = (id) => {
  switch (id) {
    case 1:
      return "Admin";
    case 2:
      return "ShopOwner";
    case 3:
      return "SalesStaff";
    case 4:
      return "Customer";
    default:
      return "Customer";
  }
};

const UpdateAccount = ({ userData, onClose, onUpdateSuccess }) => {
  const requesterId = localStorage.getItem("linkedId");
  const [user, setUser] = useState({ ...userData });

  useEffect(() => {

    setUser({ ...userData });
  }, [userData]);


  const handleUpdate = async () => {
    try {
      const updatedUser = {
        ...user,
        roleName: getRoleNameById(user.roleId),
        approvedBy: requesterId,
      };

      await updateAccount(user.id, requesterId, updatedUser);
      toast.success("Cập nhật tài khoản thành công!");
      onUpdateSuccess();
      onClose();
    } catch (error) {
      toast.error("Cập nhật tài khoản thất bại!");
    }
  };

  const handleInputChange = (key, value) => {
    if (key === "shopOwnerId") {
      const number = parseInt(value);
      if (isNaN(number) || number < 0) return;
    }
    setUser({ ...user, [key]: value });
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-30 flex justify-center items-center z-50">
      <div className="bg-white rounded-lg shadow-lg p-6 w-[500px] max-h-[90vh] overflow-y-auto">
        <h2 className="text-lg font-semibold mb-4">Update User</h2>
        <div className="space-y-4">
          {[
            { label: "Username", key: "username" },
            { label: "Password", key: "password", type: "password" },
            { label: "Email", key: "email", type: "email" },
            { label: "Phone", key: "phone" },
            { label: "Name", key: "name" },
            { label: "Other Info", key: "otherInfo" },
          ].map(({ label, key, type }) => (
            <div key={key}>
              <label className="block text-sm font-medium text-gray-700">
                {label}
              </label>
              <input
                type={type || "text"}
                className="w-full border rounded px-3 py-2"
                value={user[key] || ""}
                onChange={(e) => handleInputChange(key, e.target.value)}
                {...(key === "shopOwnerId" ? { min: 0 } : {})}
              />
            </div>
          ))}

          <div>
            <label className="block text-sm font-medium text-gray-700">
              Role
            </label>
            <select
              className="w-full border rounded px-3 py-2"
              value={user.roleId}
              onChange={(e) =>
                setUser({
                  ...user,
                  roleId: parseInt(e.target.value),
                  roleName: getRoleNameById(parseInt(e.target.value)),
                })
              }
            >
              <option value="1">Admin</option>
              <option value="2">ShopOwner</option>
              <option value="3">SalesStaff</option>
              <option value="4">Customer</option>
            </select>
          </div>

          <div className="flex items-center space-x-4">
            {/* <label className="flex items-center">
              <input
                type="checkbox"
                checked={user.isActive}
                onChange={(e) =>
                  setUser({ ...user, isActive: e.target.checked })
                }
              />
              <span className="ml-2">Active</span>
            </label>
            <label className="flex items-center">
              <input
                type="checkbox"
                checked={user.isApproved}
                onChange={(e) =>
                  setUser({ ...user, isApproved: e.target.checked })
                }
              />
              <span className="ml-2">Approved</span>
            </label> */}
          </div>

          <div className="mt-6 flex justify-end space-x-3">
            <button
              className="px-4 py-2 bg-gray-300 text-gray-800 rounded hover:bg-gray-400"
              onClick={onClose}
            >
              Cancel
            </button>
            <button
              className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
              onClick={handleUpdate}
            >
              Update
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UpdateAccount;
