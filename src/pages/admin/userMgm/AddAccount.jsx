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

  const handleInputChange = (field, value) => {
    if (field === "shopOwnerId") {
      const number = parseInt(value);
      if (isNaN(number) || number < 0) return;
    }
    setNewUser({ ...newUser, [field]: value });
  };

  const handleRoleChange = (e) => {
    const roleId = parseInt(e.target.value);
    setNewUser({
      ...newUser,
      roleId,
      roleName: getRoleNameById(roleId),
    });
  };


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


          ].map(([label, type, field]) => (
            <div key={field}>
              <label className="block text-sm font-medium text-gray-700">
                {label}
              </label>
              <input
                type={type}
                className="w-full border rounded px-3 py-2"
                value={newUser[field] || ""}
                onChange={(e) => handleInputChange(field, e.target.value)}
              />
            </div>
          ))}

          <div>
            <label className="block text-sm font-medium text-gray-700">
              Role
            </label>
            <select
              className="w-full border rounded px-3 py-2"
              value={newUser.roleId || ""}
              onChange={handleRoleChange}
            >
              <option value="">-- Chọn vai trò --</option>
              <option value="1">Admin</option>
              <option value="2">ShopOwner</option>
              <option value="3">SalesStaff</option>
              <option value="4">Customer</option>
            </select>
          </div>


          {newUser.roleId === 2 && (
            <div>
              <label className="block text-sm font-medium text-gray-700">
                Other Info (chỉ dành cho ShopOwner)
              </label>
              <input
                type="text"
                className="w-full border rounded px-3 py-2"
                value={newUser.otherInfo || ""}
                onChange={(e) =>
                  handleInputChange("otherInfo", e.target.value)
                }
              />
            </div>
          )}

          {newUser.roleId === 3 && (
            <div>
              <label className="block text-sm font-medium text-gray-700">
                Shop Owner ID (chỉ dùng nếu là nhân viên)
              </label>
              <input
                type="number"
                className="w-full border rounded px-3 py-2"
                value={newUser.shopOwnerId || ""}
                onChange={(e) =>
                  handleInputChange("shopOwnerId", e.target.value)
                }
                min={0}
              />
            </div>
          )}
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
            onClick={() => {
              const finalData = {
                ...newUser,
                approvedBy: requesterId,
              };
              onCreate(finalData);
            }}
          >
            Create
          </button>

        </div>
      </div>
    </div>
  );
};

export default AddAccount;
