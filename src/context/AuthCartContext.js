// src/context/AuthCartContext.js
import { createContext, useContext, useState, useEffect } from "react";

const AuthCartContext = createContext();

export const AuthCartProvider = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [cartCount, setCartCount] = useState(0);
  const [user, setUser] = useState(null);

  // Thêm hàm để đồng bộ cartCount từ localStorage
  const updateCartCountFromLocalStorage = () => {
    const cart = JSON.parse(localStorage.getItem("cart")) || [];
    const count = cart.reduce((sum, item) => sum + (item.qty || 0), 0);
    setCartCount(count);
  };

const addToCart = (id, quantity = 1) => {
  const cart = JSON.parse(localStorage.getItem("cart")) || [];
  const existingItem = cart.find((item) => item.id === id);

  if (existingItem) {
    existingItem.qty += quantity;
  } else {
    cart.push({ id, qty: quantity });
  }

  localStorage.setItem("cart", JSON.stringify(cart));
  updateCartCountFromLocalStorage();
};


   const login = (userData) => {
    setUser(userData);
    setIsLoggedIn(true);
    localStorage.setItem("user", JSON.stringify(userData)); // Lưu user
    localStorage.setItem("linkedId", userData.linkedId); // Lưu linkedId
    localStorage.setItem("token", userData.token); // Lưu token
    localStorage.setItem("role", userData.role); // Lưu role
    updateCartCountFromLocalStorage(); // Cập nhật cartCount từ localStorage
  };
  const logout = () => {
    setUser(null);
    setIsLoggedIn(false);
    setCartCount(0); // Reset cartCount khi logout
    // Xóa thông tin khỏi localStorage
    localStorage.removeItem("cart");
    localStorage.removeItem("token");
    localStorage.removeItem("user"); // ✅ Xóa user
    localStorage.removeItem("linkedId");
  };
  const clearCart = () => {
    localStorage.removeItem("cart");
    setCartCount(0);
  };

 useEffect(() => {
    updateCartCountFromLocalStorage(); // load cartCount từ localStorage khi khởi tạo
    // Kiểm tra nếu có user và token trong localStorage để xác định trạng thái đăng nhập

    const storedUser = localStorage.getItem("user");
    const storedToken = localStorage.getItem("token");

    if (storedUser && storedToken) {
      setUser(JSON.parse(storedUser));
      setIsLoggedIn(true);
    }
  }, []);

  return (
    <AuthCartContext.Provider
      value={{
        isLoggedIn,
        setIsLoggedIn,
        cartCount,
        setCartCount,
        updateCartCountFromLocalStorage, // Export
        user,
        setUser,
        login,
        logout,
        clearCart,
        addToCart, 
      }}
    >
      {children}
    </AuthCartContext.Provider>
  );
};

export const useAuthCart = () => useContext(AuthCartContext);
