import React, { useState, useEffect } from "react";
import { LogOut, BarChart3, Package, Edit2, Trash2 } from "lucide-react";
import { useAuthCart } from "../../../context/AuthCartContext";
import { useNavigate } from "react-router-dom";
import { getProductOwners } from "api/ShopOwnerProduct";
import { deleteProduct } from "api/DeleteProduct";

// Utility function to format currency
const formatCurrency = (amount) => {
  if (typeof amount !== "number") return amount;
  return amount.toLocaleString("en-US", {
    style: "currency",
    currency: "VND",
    minimumFractionDigits: 0,
  });
};

const ProductManagement = () => {
  const { logout } = useAuthCart();
  const navigate = useNavigate();
  const [products, setProducts] = useState([]);

  const [currentPage, setCurrentPage] = useState(1);

  const ITEMS_PER_PAGE = 8;
  const totalPages = Math.ceil(products.length / ITEMS_PER_PAGE);
  const paginatedProducts = products.slice(
    (currentPage - 1) * ITEMS_PER_PAGE,
    currentPage * ITEMS_PER_PAGE
  );

  const handlePrevPage = () => {
    if (currentPage > 1) setCurrentPage(currentPage - 1);
  };

  const handleNextPage = () => {
    if (currentPage < totalPages) setCurrentPage(currentPage + 1);
  };

  useEffect(() => {
    const fetchData = async () => {
      const token = localStorage.getItem("token");
      try {
        const res = await getProductOwners(token);
        if (Array.isArray(res)) {
          const mapped = res.map((p) => ({
            id: p.id,
            name: p.name,
            image: p.imageUrl,
            price: p.price,
            stock: p.stock,
            status: p.status, // "Còn hàng" hoặc "Hết hàng"
          }));
          setProducts(mapped);
        }
      } catch (error) {
        console.error("Product fetch error:", error);
      }
    };

    fetchData();
  }, []);

  const handleDelete = async (id) => {
    if (window.confirm("Bạn có chắc muốn xóa sản phẩm này?")) {
      try {
        const token = localStorage.getItem("token");
        await deleteProduct(token, id);
        const newProducts = products.filter((p) => p.id !== id);
        setProducts(newProducts);

        // Nếu trang hiện tại không còn sản phẩm, chuyển về trang trước
        const newTotalPages = Math.ceil(newProducts.length / ITEMS_PER_PAGE);
        if (currentPage > newTotalPages && currentPage > 1) {
          setCurrentPage(currentPage - 1);
        }
      } catch (error) {
        alert("Xóa sản phẩm thất bại!");
      }
    }
  };

  // Sửa lại hàm getStatusBadge cho tiếng Việt
  const getStatusBadge = (status) => {
    if (status === "Còn hàng") {
      return (
        <span className="inline-flex items-center justify-center w-20 px-3 py-1 text-sm font-medium text-green-700 bg-green-50 border border-green-200 rounded-full whitespace-nowrap">
          {status}
        </span>
      );
    } else {
      return (
        <span className="inline-flex items-center justify-center w-20 px-3 py-1 text-sm font-medium text-red-700 bg-red-50 border border-red-200 rounded-full whitespace-nowrap">
          {status}
        </span>
      );
    }
  };

  return (
    <div className="flex min-h-screen bg-gray-50">
      {/* Sidebar */}
      <div className="w-64 bg-green-600 text-white flex flex-col sticky top-0 h-screen overflow-y-auto">
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
        <nav className="mt-8 flex-1">
          <div className="px-4 mb-2">
            <span className="text-sm font-medium text-green-200">Main</span>
          </div>
          <ul className="space-y-2 px-4">
            <li>
              <a
                href="/shopowner/products"
                className={`flex items-center px-4 py-3 rounded-lg transition-colors ${
                  window.location.pathname === "/shopowner/products"
                    ? "bg-green-500 text-white"
                    : "text-green-100 hover:bg-green-500"
                }`}
              >
                <Package className="w-5 h-5 mr-3" />
                <span>Product</span>
              </a>
            </li>
            <li>
              <a
                href="/shopowner/dashboard"
                className={`flex items-center px-4 py-3 rounded-lg transition-colors ${
                  window.location.pathname === "/shopowner/dashboard"
                    ? "bg-green-500 text-white"
                    : "text-green-100 hover:bg-green-500"
                }`}
              >
                <BarChart3 className="w-5 h-5 mr-3" />
                <span className="font-medium">Dashboard</span>
              </a>
            </li>
            <li>
              <button
                onClick={() => {
                  logout();
                  navigate("/");
                }}
                className="w-full text-left flex items-center px-4 py-3 text-red-200 hover:bg-green-500 rounded-lg font-medium transition-colors"
              >
                <LogOut className="w-5 h-5 mr-3" />
                <span>Logout</span>
              </button>
            </li>
          </ul>
        </nav>
      </div>

      {/* Main Content */}
      <div className="flex-1 flex flex-col">
        {/* Header */}
        <header className="bg-white shadow-sm border-b border-gray-200">
          <div className="flex items-center justify-between px-6 py-4">
            <div className="flex items-center space-x-4">
              <h1 className="text-xl font-semibold text-gray-900">
                Quản lý sản phẩm
              </h1>
            </div>
          </div>
        </header>

        {/* Content */}
        <main className="flex-1 p-6">
          <div className="bg-white rounded-lg shadow">
            {/* Products Header */}
            <div className="p-6 border-b border-gray-200 flex items-center justify-between">
              <h2 className="text-lg font-semibold text-gray-900">
                Danh sách sản phẩm
              </h2>
              <button
                className="flex items-center px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 transition-colors"
                onClick={() => {
                  const role = localStorage.getItem("role");
                  if (role !== "ShopOwner") {
                    alert("Vai trò không hợp lệ!");
                    return;
                  }
                  navigate("/shopowner/add-product");
                }}
              >
                <span className="mr-2">+</span>
                Thêm sản phẩm
              </button>
            </div>

            {/* Table */}
            <div className="overflow-x-auto">
              <table className="w-full table-fixed">
                <thead className="bg-gray-50 text-center">
                  <tr>
                    <th className="w-[50px] px-2 py-3 text-sm font-medium text-gray-700">
                      ID
                    </th>
                    <th className="w-[200px] px-2 py-3 text-sm font-medium text-gray-700">
                      Tên sản phẩm
                    </th>
                    <th className="w-[100px] px-2 py-3 text-sm font-medium text-gray-700">
                      Ảnh
                    </th>
                    <th className="w-[120px] px-2 py-3 text-sm font-medium text-gray-700">
                      Giá
                    </th>
                    <th className="w-[120px] px-2 py-3 text-sm font-medium text-gray-700">
                      Tồn kho
                    </th>
                    <th className="w-[120px] px-2 py-3 text-sm font-medium text-gray-700">
                      Trạng thái
                    </th>
                    <th className="w-[120px] px-2 py-3 text-sm font-medium text-gray-700">
                      Thao tác
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200 text-center">
                  {paginatedProducts.map((product) => (
                    <tr key={product.id} className="hover:bg-gray-50">
                      <td className="px-6 py-4">{product.id}</td>
                      <td className="px-6 py-4">{product.name}</td>
                      <td className="px-6 py-4">
                        <img
                          src={product.image}
                          alt={product.name}
                          className="w-12 h-12 object-contain mx-auto rounded"
                        />
                      </td>
                      <td className="px-6 py-4 text-gray-700">
                        {formatCurrency(product.price)}
                      </td>
                      <td className="px-6 py-4">{product.stock}</td>
                      <td className="px-6 py-4">
                        {getStatusBadge(product.status)}
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-500">
                        <div className="flex items-center justify-center space-x-2">
                          <button
                            className="p-2 rounded hover:bg-gray-100"
                            onClick={() =>
                              navigate(`/shopowner/edit-product/${product.id}`)
                            }
                          >
                            <Edit2 className="w-5 h-5 text-blue-600" />
                          </button>
                          <button
                            className="p-2 rounded hover:bg-gray-100"
                            onClick={() => {
                              const role = localStorage.getItem("role");
                              if (role !== "ShopOwner") {
                                alert("Vai trò không hợp lệ!");
                                return;
                              }
                              handleDelete(product.id);
                            }}
                          >
                            <Trash2 className="w-5 h-5 text-red-600" />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
              <div className="flex flex-col items-center px-6 py-4 border-t border-gray-200 space-y-2">
                <span className="text-sm text-gray-500">
                  Trang {currentPage} trên {totalPages}
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
          </div>
        </main>
      </div>
    </div>
  );
};

export default ProductManagement;
