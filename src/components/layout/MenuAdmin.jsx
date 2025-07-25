import React from "react";
import {
  FaUser,
  FaFileAlt,
  FaCogs,
  FaChevronDown,
  FaSignOutAlt,
} from "react-icons/fa";
import { NavLink } from "react-router-dom";

const MenuAdmin = () => {
  return (
    <aside className="w-64 min-h-screen bg-gradient-to-b from-[#12AB3C] to-[#083622] text-white p-4">
      <nav className="space-y-2">
        <NavLink
          to="/users"
          className="flex items-center gap-2 p-2 rounded hover:bg-green-600"
        >
          <FaUser /> Người dùng
        </NavLink>
        <NavLink
          to="/contents"
          className="flex items-center gap-2 p-2 rounded hover:bg-green-600"
        >
          <FaFileAlt /> Nội dung
        </NavLink>

        <div className="bg-green-600 rounded p-2">
          <div className="flex items-center gap-2">
            <FaCogs /> Quản lý hệ thống <FaChevronDown className="ml-auto" />
          </div>
          <div className="mt-2 ml-6 space-y-1">
            <NavLink
              to="/user-log"
              className="block text-white hover:underline"
            >
              Nhật ký hoạt động người dùng
            </NavLink>
            <NavLink
              to="/system-log"
              className="block text-white hover:underline"
            >
              Nhật ký hệ thống
            </NavLink>
          </div>
        </div>

        <NavLink
          to="/logout"
          className="flex items-center gap-2 p-2 rounded hover:bg-green-600"
        >
          <FaSignOutAlt /> Đăng xuất
        </NavLink>
      </nav>
    </aside>
  );
};

export default MenuAdmin;
