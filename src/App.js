
import React from "react";
import { Routes, Route } from "react-router-dom";
import ShopPage from "./pages/shop/ShopPage";
import HomePage from "./pages/home/HomePage";
import Login from "./pages/login/Login";
import Register from "./pages/register/Register";
import CheckoutPage from "./pages/checkout/CheckoutPage";
import CheckoutConfirm from "./pages/checkout/CheckoutConfirm";

import InspectionPolicy from "./pages/policy/InspectionPolicy";
import PrivacyPolicy from "./pages/policy/PrivacyPolicy";
import DeliveryPolicy from "./pages/policy/DeliveryPolicy";
import NewsPage from "./pages/news/NewsPage";

import CartPage from "./pages/cart/CartPage";
import ProductDetails from "./pages/ProductDetails/ProductDetails";

import PromotionPage from "./pages/promotion/PromotionPage";
import OrderHistoryPage from "./pages/order/OrderHistoryPage";
import OrderDetailsPage from "./pages/order/OrderDetailsPage";
import ProfilePage from "./pages/profile/ProfilePage";

import AboutUsPage from "pages/aboutUs/AboutUsPage";

import DashBoard from "./pages/shopOwner/DashBoard";
import UserActivityLogPage from "./pages/admin/systemManagement/userLog/UserActivityLogPage";
import PromotionPage from "pages/promotion/PromotionPage";

import UserManagement from "./pages/admin/userMgm/UserManagement";
import ContentPage from "./pages/admin/content/ContentPage";
import UserActivityLogPage from "pages/admin/systemManagement/userLog/UserActivityLogPage";

import ProductManagement from "./pages/shopOwner/product/ProductManagement";
import AddProduct from "pages/shopOwner/product/AddProduct";
import EditProduct from "pages/shopOwner/product/EditProduct";
function App() {
  return (
    <div className="max-w-screen-[1440px] mx-auto">
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        <Route path="/shop" element={<ShopPage />} />

        <Route path="/cart" element={<CartPage />} />

        <Route path="/checkout" element={<CheckoutPage />} />
        <Route path="/checkout/confirm" element={<CheckoutConfirm />} />
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/promotions" element={<PromotionPage />} />

        {/* <Route path="/about" element={<AboutPage />} /> */}
        {/* <Route path="/contact" element={<ContactPage />} /> */}
        <Route path="/inspectionPolicy" element={<InspectionPolicy />} />
        <Route path="/privacyPolicy" element={<PrivacyPolicy />} />
        <Route path="/deliveryPolicy" element={<DeliveryPolicy />} />
        <Route path="/news" element={<NewsPage />} />

        <Route path="/product/:id" element={<ProductDetails />} />

        <Route path="/order-history" element={<OrderHistoryPage />} />
        <Route
          path="/order-details/:customerId"
          element={<OrderDetailsPage />}
        />

        <Route path="/filter" element={<ShopPage />} />

        <Route path="/about-us" element={<AboutUsPage />} />
   

        {/* Admin Routes */}
        <Route path="/admin-page" element={<UserManagement />} />
        <Route path="/admin-page/users" element={<UserManagement />} />
        <Route path="/admin-page/contents" element={<ContentPage />} />
        <Route path="/admin-page/settings" element={<UserManagement />} />
        <Route path="/admin-page/user-activity-log" element={<UserActivityLogPage />} />

        {/* User Routes */}

        <Route path="/shopowner" element={<ProductManagement />} />
        <Route path="/shopowner/dashboard" element={<DashBoard />} />
        <Route path="/shopowner/products" element={<ProductManagement />} />


        <Route path="/shopowner" element={<ProductManagement />} />
        {/* <Route path="/shopowner/products" element={<DashBoard />} /> */}

        {/* Manager Routes */}

        {/* <Route path="/manager-page/product" element={<ProductManagement />} /> */}
        <Route path="/shopowner/add-product" element={<AddProduct />} />
        {/* <Route path="/manager-page/dashboard" element={<ProductManagement />} /> */}

        <Route path="/shopowner/edit-product/:id" element={<EditProduct />} />
      </Routes>


    </div>
  );
}

export default App;
