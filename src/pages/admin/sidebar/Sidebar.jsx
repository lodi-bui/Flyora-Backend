import React from 'react';
import { Users, FileText, Settings, LogOut, ChevronDown } from 'lucide-react';
import { useAuthCart } from '../../../context/AuthCartContext';
import toast from "react-hot-toast";
import { useNavigate, useLocation } from 'react-router-dom';

const Sidebar = () => {
  const { logout } = useAuthCart();
  const navigate = useNavigate();
  const location = useLocation();

  const role = useAuthCart().user?.role

  return (
    <div className="w-64 min-h-screen bg-green-600 text-white">

      {/* Logo */}
      <div className="flex items-center p-6 border-b border-green-500">
        <div className="flex items-center space-x-2">
          <div className="w-8 h-8 bg-white rounded flex items-center justify-center">
            <span className="text-green-600 font-bold text-sm">F</span>
          </div>
          <span className="text-xl font-bold">Flyora</span>
        </div>
      </div>

      {/* Navigation */}
      <nav className="mt-8">
        <div className="px-4 mb-2">
          <span className="text-sm font-medium text-green-200">Main</span>
        </div>
        <ul className="space-y-2 px-4">

          {role === 'Admin' ? (

            <>
              <li>
                <a href="/admin-page/users" className={`flex items-center px-4 py-3 rounded-lg ${location.pathname === '/admin-page/users' ? 'bg-green-500 text-white' : 'text-green-100 hover:bg-green-500 transition-colors'}`}>
                  <Users className="w-5 h-5 mr-3" />
                  <span className="font-medium">Users</span>
                </a>
              </li>
              <li>
                <a href="/admin-page/contents" className={`flex items-center px-4 py-3 rounded-lg ${location.pathname === '/admin-page/contents' ? 'bg-green-500 text-white' : 'text-green-100 hover:bg-green-500 transition-colors'}`}>
                  <FileText className="w-5 h-5 mr-3" />
                  <span>Contents</span>
                </a>
              </li>
              <li>
                <a href="/admin-page/user-activity-log" className={`flex items-center px-4 py-3 rounded-lg ${location.pathname === '/admin-page/user-activity-log' ? 'bg-green-500 text-white' : 'text-green-100 hover:bg-green-500 transition-colors'}`}>
                  <Settings className="w-5 h-5 mr-3" />
                  <span>User Activity</span>
                  {/* <ChevronDown className="w-4 h-4 ml-auto" /> */}
                </a>
              </li>
            </>

          ) : role === 'ShopOwner' ? (
            <>
              <li>
                <a href="/shopowner/product" className={`flex items-center px-4 py-3 rounded-lg ${location.pathname === '/admin-page/product' ? 'bg-green-500 text-white' : 'text-green-100 hover:bg-green-500 transition-colors'}`}>
                  <FileText className="w-5 h-5 mr-3" />
                  <span>Product</span>
                </a>
              </li>
              <li>

                <a href="/shopowner/dashboard" className={`flex items-center px-4 py-3 rounded-lg ${location.pathname === '/admin-page/dashboard' ? 'bg-green-500 text-white' : 'text-green-100 hover:bg-green-500 transition-colors'}`}>
                  <Settings className="w-5 h-5 mr-3" />
                  <span>Dashboard</span>
                </a>
              </li>
            </>
          ) : null}
          <li>
            <button
              onClick={() => {
                logout();
                toast.success("Đăng xuất thành công!");
                navigate("/");
              }}
              className={`w-full text-left flex items-center px-4 py-3 rounded-lg ${location.pathname === '/' ? 'bg-green-500 text-white' : 'text-red-200 hover:bg-green-500 transition-colors'} font-medium`}
            >
              <LogOut className="w-5 h-5 mr-3" />
              <span>Logout</span>
            </button>
          </li>
        </ul>
      </nav>
    </div>
  );
};

export default Sidebar;