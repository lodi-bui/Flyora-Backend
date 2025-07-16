import React, { useState, useEffect } from "react";

import {
  Search,
  Plus,
  Edit2,
  Trash2,
  Settings,
  LogOut,
  Users,
  FileText,
  ChevronDown,
} from "lucide-react";
import { useAuthCart } from "../../../context/AuthCartContext";
import Sidebar from "../sidebar/Sidebar";
import toast from "react-hot-toast";
import { useNavigate } from "react-router-dom";
import { UserAccounts } from "api/UserManagement";
import { addNewAccount } from "api/AddNewAccount";
import AddAccount from "./AddAccount";
import UpdateAccount from "./UpdateAccount";
import { deleteAccount } from "api/DeleteAccount";
import { activateAccount } from "api/ActivateAccount";
import { deactivateAccount } from "api/DeactivateAccount";

const ITEMS_PER_PAGE = 6;

const UserManagement = () => {
  const { logout } = useAuthCart();
  const navigate = useNavigate();

  const requesterId = localStorage.getItem("linkedId");
  const [users, setUsers] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);

  const [isPopupOpen, setIsPopupOpen] = useState(false);
  const [newUser, setNewUser] = useState({
    username: "",
    password: "",
    email: "",
    phone: "",
    isActive: true,
    isApproved: true,
    roleId: 4, // Default to Customer role
    roleName: "Customer", // Default role name
    approvedBy: 0,
    name: "",
    otherInfo: "",
    shopOwnerId: 2, // Default to ShopOwner ID
  });

  const [isEditPopupOpen, setIsEditPopupOpen] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);

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

  const handleCreateUser = async () => {
    try {
      await addNewAccount(requesterId, {
        ...newUser,
        roleName: getRoleNameById(newUser.roleId),
      });
      toast.success("Tạo người dùng thành công!");
      setIsPopupOpen(false);
      setNewUser({
        username: "",
        password: "",
        email: "",
        phone: "",
        isActive: true,
        isApproved: true,
        roleId: 4,
        roleName: "Customer",
        approvedBy: 0,
        name: "",
        otherInfo: "",
        shopOwnerId: 0,
      });

      // Reload lại danh sách
      const updated = await UserAccounts(requesterId);
      setUsers(Array.isArray(updated) ? updated : [updated]);
    } catch (error) {
      toast.error("Tạo người dùng thất bại!");
    }
  };

  // Hàm xử lý khi nhấn nút sửa người dùng
  const handleEditUser = (user) => {
    setSelectedUser(user);
    setIsEditPopupOpen(true);
  };

  // Hàm xử lý khi nhấn nút xóa người dùng
  const handleDeleteUser = async (userId) => {
    const confirmDelete = window.confirm(
      "Bạn có chắc muốn xóa người dùng này?"
    );
    if (!confirmDelete) return;

    try {
      await deleteAccount(userId, requesterId);
      toast.success("Xóa người dùng thành công!");

      // Reload danh sách sau khi xóa
      const updated = await UserAccounts(requesterId);
      setUsers(Array.isArray(updated) ? updated : [updated]);
    } catch (error) {
      toast.error("Xóa người dùng thất bại!");
    }
  };

  const [isConfirmModalOpen, setIsConfirmModalOpen] = useState(false);
  const [targetUser, setTargetUser] = useState(null);

  const openConfirmModal = (user) => {
    setTargetUser(user);
    setIsConfirmModalOpen(true);
  };

  const confirmToggleStatus = async () => {
    if (!targetUser) return;

    try {
      if (targetUser.active) {
        await deactivateAccount(targetUser.id, requesterId);
        toast.success(`Đã vô hiệu hóa ${targetUser.username}`);
      } else {
        await activateAccount(targetUser.id, requesterId);
        toast.success(`Đã kích hoạt ${targetUser.username}`);
      }

      // Refresh danh sách
      const updated = await UserAccounts(requesterId);
      setUsers(Array.isArray(updated) ? updated : [updated]);
    } catch (error) {
      toast.error("Không thể cập nhật trạng thái!");
    } finally {
      setIsConfirmModalOpen(false);
      setTargetUser(null);
    }
  };

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const res = await UserAccounts(requesterId);
        setUsers(Array.isArray(res) ? res : [res]);
      } catch (error) {
        toast.error("Không thể tải danh sách người dùng");
        console.error(error);
      }
    };
    fetchUsers();
  }, [requesterId]);

  const getStatusBadge = (status) => {
    return status ? (
      <span className="inline-full items-center justify-center w-20 px-3 py-1 text-sm font-medium text-green-700 bg-green-50 border border-green-200 rounded-full">
        Activated
      </span>
    ) : (
      <span className="inline-full items-center justify-center w-20 px-3 py-1 text-sm font-medium text-red-700 bg-red-50 border border-red-200 rounded-full">
        Deactivated
      </span>
    );
  };

  const totalPages = Math.ceil(users.length / ITEMS_PER_PAGE);
  const paginatedUsers = users.slice(
    (currentPage - 1) * ITEMS_PER_PAGE,
    currentPage * ITEMS_PER_PAGE
  );

  const handlePrevPage = () => {
    if (currentPage > 1) setCurrentPage(currentPage - 1);
  };

  const handleNextPage = () => {
    if (currentPage < totalPages) setCurrentPage(currentPage + 1);
  };

  return (
    <div className="flex h-screen bg-gray-50">
      {/* Sidebar */}
      <div className="w-64 bg-green-600 text-white">
        <div className="flex items-center p-6 border-b border-green-500">
          <div className="flex items-center space-x-2">
            <div className="w-8 h-8 bg-white rounded flex items-center justify-center">
              <span className="text-green-600 font-bold text-sm">F</span>
            </div>
            <span className="text-xl font-bold">Flyora</span>
          </div>
        </div>

        {/* Sidebar */}
        <Sidebar />
      </div>

      {/* Main Content */}
      <div className="flex-1 flex flex-col">
        <header className="bg-white shadow-sm border-b border-gray-200">
          <div className="flex items-center justify-between px-6 py-4">
            <h1 className="text-xl font-semibold text-gray-900">
              User Management
            </h1>
          </div>
        </header>

        <main className="flex-1 p-6">
          <div className="bg-white rounded-lg shadow">
            <div className="flex items-center justify-between p-6 border-b border-gray-200">
              <h2 className="text-lg font-semibold text-gray-900">Users</h2>
              <button
                onClick={() => setIsPopupOpen(true)}
                className="flex items-center px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 transition-colors"
              >
                <Plus className="w-4 h-4 mr-2" />
                Add New
              </button>
            </div>

            <div className="overflow-x-auto">
              <table className="w-full table-fixed">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-4 py-3 text-xs font-medium text-gray-500 uppercase text-center w-[50px]">
                      ID
                    </th>
                    <th className="px-4 py-3 text-xs font-medium text-gray-500 uppercase text-center w-[120px]">
                      Username
                    </th>
                    <th className="px-4 py-3 text-xs font-medium text-gray-500 uppercase text-center w-[230px]">
                      Email
                    </th>
                    <th className="px-4 py-3 text-xs font-medium text-gray-500 uppercase text-center w-[140px]">
                      Phone
                    </th>
                    <th className="px-4 py-3 text-xs font-medium text-gray-500 uppercase text-center w-[130px]">
                      Role
                    </th>
                    <th className="px-4 py-3 text-xs font-medium text-gray-500 uppercase text-center w-[100px]">
                      Status
                    </th>
                    <th className="px-4 py-3 text-xs font-medium text-gray-500 uppercase text-center w-[80px]">
                      Modify
                    </th>
                  </tr>
                </thead>

                <tbody className="bg-white divide-y divide-gray-200">
                  {paginatedUsers.map((user) => (
                    <tr key={user.id} className="hover:bg-gray-50">
                      <td className="px-4 py-4 text-sm text-gray-900 text-center">
                        {user.id}
                      </td>
                      <td className="px-4 py-4 text-sm text-gray-900 text-center">
                        {user.username}
                      </td>
                      <td className="px-4 py-4 text-sm text-gray-900 text-center truncate">
                        {user.email}
                      </td>
                      <td className="px-4 py-4 text-sm text-gray-900 text-center">
                        {user.phone}
                      </td>
                      <td className="px-4 py-4 text-sm text-gray-900 text-center">
                        {user.role}
                      </td>
                      <td
                        className="px-4 py-4 text-sm text-center cursor-pointer"
                        onClick={() => openConfirmModal(user)}
                      >
                        {getStatusBadge(user.active)}
                      </td>

                      <td className="px-4 py-4 text-sm text-gray-500 text-center">
                        <div className="flex justify-center items-center space-x-2">
                          <button
                            className="p-1 text-blue-600 hover:text-blue-800"
                            onClick={() => handleEditUser(user)}
                          >
                            <Edit2 className="w-4 h-4" />
                          </button>

                          <button
                            className="p-1 text-red-600 hover:text-red-800"
                            onClick={() => handleDeleteUser(user.id)}
                          >
                            <Trash2 className="w-4 h-4" />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            {/* Pagination */}
            <div className="flex flex-col items-center px-6 py-4 border-t border-gray-200 space-y-2">
              <span className="text-sm text-gray-500">
                Page {currentPage} of {totalPages}
              </span>
              <div className="flex items-center space-x-1">
                <button
                  className={`w-8 h-8 rounded flex items-center justify-center text-sm font-medium ${
                    currentPage === 1
                      ? "text-gray-300 cursor-not-allowed"
                      : "text-gray-500 hover:bg-gray-100"
                  }`}
                  onClick={handlePrevPage}
                  disabled={currentPage === 1}
                >
                  ←
                </button>
                {[...Array(totalPages)].map((_, i) => (
                  <button
                    key={i}
                    className={`w-8 h-8 rounded flex items-center justify-center text-sm font-medium ${
                      currentPage === i + 1
                        ? "bg-red-500 text-white"
                        : "text-gray-500 hover:bg-gray-100"
                    }`}
                    onClick={() => setCurrentPage(i + 1)}
                  >
                    {i + 1}
                  </button>
                ))}
                <button
                  className={`w-8 h-8 rounded flex items-center justify-center text-sm font-medium ${
                    currentPage === totalPages
                      ? "text-gray-300 cursor-not-allowed"
                      : "text-gray-500 hover:bg-gray-100"
                  }`}
                  onClick={handleNextPage}
                  disabled={currentPage === totalPages}
                >
                  →
                </button>
              </div>
            </div>
          </div>
        </main>
      </div>

      {/* Add User Popup */}
      {isPopupOpen && (
        <AddAccount
          newUser={newUser}
          setNewUser={setNewUser}
          onCreate={handleCreateUser}
          onClose={() => setIsPopupOpen(false)}
        />
      )}

      {isEditPopupOpen && selectedUser && (
        <UpdateAccount
          userData={selectedUser}
          onClose={() => {
            setIsEditPopupOpen(false);
            setSelectedUser(null);
          }}
          onUpdateSuccess={async () => {
            const updated = await UserAccounts(requesterId);
            setUsers(Array.isArray(updated) ? updated : [updated]);
          }}
        />
      )}

      {isConfirmModalOpen && targetUser && (
        <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-xl p-6 w-[400px]">
            <h2 className="text-lg font-semibold mb-4 text-gray-800">
              Xác nhận thay đổi trạng thái
            </h2>
            <p className="text-sm text-gray-600 mb-6">
              Bạn có chắc chắn muốn{" "}
              <span className="font-medium text-red-600">
                {targetUser.active ? "vô hiệu hóa" : "kích hoạt"}
              </span>{" "}
              tài khoản{" "}
              <span className="font-semibold">{targetUser.username}</span>?
            </p>
            <div className="flex justify-end space-x-3">
              <button
                onClick={() => {
                  setIsConfirmModalOpen(false);
                  setTargetUser(null);
                }}
                className="px-4 py-2 bg-gray-300 text-gray-800 rounded hover:bg-gray-400"
              >
                Hủy
              </button>
              <button
                onClick={confirmToggleStatus}
                className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700"
              >
                Xác nhận
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default UserManagement;
