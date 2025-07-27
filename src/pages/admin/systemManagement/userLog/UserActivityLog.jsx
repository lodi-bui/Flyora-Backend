// src/pages/admin/systemManagement/userLog/UserActivityLog.jsx
import React, { useEffect, useState } from "react";
import { Search } from "lucide-react";

import { getUserActivityLogs } from "api/UserActivityLog";
import toast from "react-hot-toast";
import { useAuthCart } from "context/AuthCartContext"; //  Dùng context để lấy user

const ITEMS_PER_PAGE = 8;

const UserActivityLog = () => {
  const { user } = useAuthCart(); //  Lấy user từ context
  const requesterId = user?.linkedId; //  Lấy linkedId từ user

  const [logs, setLogs] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  useEffect(() => {
    if (!user) return; // đợi user được load xong
    if (!requesterId) {
      //  Đảm bảo requesterId có giá trị
      toast.error("Vui lòng đăng nhập để quản lý hoạt động người dùng");
      return;
    }

    const fetchLogs = async () => {
      try {
        const res = await getUserActivityLogs(requesterId);
        setLogs(Array.isArray(res) ? res : [res]);
      } catch (error) {
        toast.error("Không thể tải log. Bạn không có quyền truy cập.");
        console.error(error);
      }
    };

    fetchLogs();
  }, [requesterId]);

  const filteredLogs = logs.filter(
    (log) =>
      log.username?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      log.action?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const displayedLogs = searchTerm ? filteredLogs : logs;
  const totalPages = Math.ceil(displayedLogs.length / ITEMS_PER_PAGE);
  const paginatedLogs = displayedLogs.slice(
    (currentPage - 1) * ITEMS_PER_PAGE,
    currentPage * ITEMS_PER_PAGE
  );

  const generatePagination = () => {
    const pages = [];
    const maxVisible = 5;

    if (totalPages <= maxVisible) {
      for (let i = 1; i <= totalPages; i++) pages.push(i);
    } else {
      const left = Math.max(1, currentPage - 2);
      const right = Math.min(totalPages, currentPage + 2);

      if (left > 1) pages.push(1, "...");
      for (let i = left; i <= right; i++) pages.push(i);
      if (right < totalPages) pages.push("...", totalPages);
    }

    return pages;
  };

  return (
    <div className="flex-1 flex flex-col bg-gray-50">
      <header className="bg-white shadow-sm border-b border-gray-200">
        <div className="flex items-center justify-between px-6 py-4">
          <div className="flex items-center space-x-4">
            <h1 className="text-xl font-semibold text-gray-900">
              User Activity Log
            </h1>
          </div>
          <div className="flex items-center space-x-4">
            {/* <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
                <input
                  type="text"
                  placeholder="Search here"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-80 pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent"
                />
              </div> */}
            {/* <div className="flex items-center space-x-3">
                <div className="w-10 h-10 bg-gray-300 rounded-full overflow-hidden">
                  <img
                    src="https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-1.2.1&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80"
                    alt="User"
                    className="w-full h-full object-cover"
                  />
                </div>
                <div>
                  <div className="text-sm font-medium text-gray-900">
                    {user?.username || "Admin"}

                  </div>
                  <div className="text-xs text-gray-500">Super Admin</div>
                </div>
              </div> */}
          </div>
        </div>
      </header>
      <main className="flex-1 p-6">
        <div className="bg-white rounded-lg shadow">
          <div className="px-6 py-4 border-b border-gray-200">
            <div className="flex space-x-4">
              <button className="text-red-500 font-medium border-b-2 border-red-500 pb-2">
                All ({logs.length})
              </button>
            </div>
          </div>

          <div className="overflow-x-auto">
            <table className="w-full table-fixed">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider w-[130px]">
                    STT
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider w-[130px]">
                    ID
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider w-[230px]">
                    Tên
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider min-w-[250px]">
                    Chi tiết hoạt động
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider min-w-[200px]">
                    Thời gian hoạt động
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {paginatedLogs.map((log, index) => (
                  <tr key={index} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {(currentPage - 1) * ITEMS_PER_PAGE + index + 1}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {log.accountId}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {log.username}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {log.action}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {new Date(log.timestamp)
                        .toLocaleString("vi-VN", {
                          year: "numeric",
                          month: "2-digit",
                          day: "2-digit",
                          hour: "2-digit",
                          minute: "2-digit",
                          second: "2-digit",
                          hour12: false,
                        })
                        .replace(", ", "")}
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
                onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
                disabled={currentPage === 1}
              >
                ←
              </button>

              {generatePagination().map((page, i) =>
                page === "..." ? (
                  <span
                    key={i}
                    className="w-8 h-8 flex items-center justify-center text-sm text-gray-400"
                  >
                    ...
                  </span>
                ) : (
                  <button
                    key={i}
                    className={`w-8 h-8 rounded flex items-center justify-center text-sm font-medium ${
                      currentPage === page
                        ? "bg-red-500 text-white"
                        : "text-gray-500 hover:bg-gray-100"
                    }`}
                    onClick={() => setCurrentPage(page)}
                  >
                    {page}
                  </button>
                )
              )}

              <button
                className={`w-8 h-8 rounded flex items-center justify-center text-sm font-medium ${
                  currentPage === totalPages
                    ? "text-gray-300 cursor-not-allowed"
                    : "text-gray-500 hover:bg-gray-100"
                }`}
                onClick={() =>
                  setCurrentPage((prev) => Math.min(prev + 1, totalPages))
                }
                disabled={currentPage === totalPages}
              >
                →
              </button>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
};

export default UserActivityLog;
