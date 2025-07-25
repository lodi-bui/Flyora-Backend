import React from "react";

const getRoleNameById = (id) => {
  switch (id) {
    case 1:
      return "Admin";
    case 2:
      return "ShopOwner";
    case 3:
      return "SalesStaff";
    case 4:
    default:
      return "Customer";
  }
};

const AddAccount = ({ newUser, setNewUser, onCreate, onClose }) => {
  const requesterId = localStorage.getItem("linkedId");

  return (
    <div className="fixed inset-0 bg-black bg-opacity-30 flex justify-center items-center z-50">
      <div className="bg-white rounded-lg shadow-lg p-6 w-[500px] max-h-[90vh] overflow-y-auto">
        <h2 className="text-lg font-semibold mb-4">Add New User</h2>
        <div className="space-y-4">
          {[
            ["Username", "text", "username"],
            ["Password", "password", "password"],
            ["Email", "email", "email"],
            ["Phone", "text", "phone"],
            ["Name", "text", "name"],
            ["Other Info", "text", "otherInfo"],
            ["Shop Owner ID", "number", "shopOwnerId"],
          ].map(([label, type, field]) => (
            <div key={field}>
              <label className="block text-sm font-medium text-gray-700">
                {label}
              </label>
              <input
                type={type}
                className="w-full border rounded px-3 py-2"
                value={newUser[field]}
                onChange={(e) =>
                  setNewUser({ ...newUser, [field]: e.target.value })
                }
              />
            </div>
          ))}

          <div>
            <label className="block text-sm font-medium text-gray-700">
              Role
            </label>
            <select
              className="w-full border rounded px-3 py-2"
              value={newUser.roleId}
              onChange={(e) => {
                const roleId = parseInt(e.target.value);
                setNewUser({
                  ...newUser,
                  roleId,
                  roleName: getRoleNameById(roleId),
                });
              }}
            >
              <option value="1">Admin</option>
              <option value="2">ShopOwner</option>
              <option value="3">SalesStaff</option>
              <option value="4">Customer</option>
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">
              Approved By (Requester ID)
            </label>
            <input
              type="number"
              className="w-full border rounded px-3 py-2"
              value={newUser.approvedBy}
              onChange={() =>
                setNewUser({ ...newUser, approvedBy: requesterId })
              }
            />
          </div>

          <div className="flex items-center space-x-4">
            <label className="flex items-center">
              <input
                type="checkbox"
                checked={newUser.isActive}
                onChange={(e) =>
                  setNewUser({ ...newUser, isActive: e.target.checked })
                }
              />
              <span className="ml-2">Active</span>
            </label>
            <label className="flex items-center">
              <input
                type="checkbox"
                checked={newUser.isApproved}
                onChange={(e) =>
                  setNewUser({ ...newUser, isApproved: e.target.checked })
                }
              />
              <span className="ml-2">Approved</span>
            </label>
          </div>

          <div className="mt-6 flex justify-end space-x-3">
            <button
              className="px-4 py-2 bg-gray-300 text-gray-800 rounded hover:bg-gray-400"
              onClick={onClose}
            >
              Cancel
            </button>
            <button
              className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700"
              onClick={onCreate}
            >
              Create
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AddAccount;
