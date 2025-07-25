// src/pages/checkout/CheckoutPage.jsx
import React, { useState, useEffect, useMemo } from "react";
import { Navigate } from "react-router-dom";
import ShippingInfo from "./ShippingInfo";
import PaymentMethod from "./PaymentMethod";
import PayOnline from "./PayOnline";
import Header from "../navfoot/Header";
import Footer from "../navfoot/Footer";
import { createOrder } from "../../api/Order";
import { createPayment } from "../../api/Payment";
import { getCart } from "../../api/Cart"; // Sửa nếu file là cart.js

const CheckoutPage = () => {
  // 1) Thông tin giao hàng
  const [shipping, setShipping] = useState({
    name: "",
    phone: "",
    email: "",
    address: "",
    province: "",
    district: "",
    ward: "",
    notes: "",
  });

  const handleShipChange = (e) =>
    setShipping({ ...shipping, [e.target.name]: e.target.value });

  // 2) Giỏ hàng
  const [items, setItems] = useState([]);

  useEffect(() => {
    const fetchCart = async () => {
      const rawCart = JSON.parse(localStorage.getItem("cart")) || [];

      // Nếu giỏ hàng trống thì không làm gì
      if (rawCart.length === 0) {
        setItems([]);
        return;
      }

      try {
        // Gọi API lấy thông tin chi tiết sản phẩm từ productId và quantity
        const productData = await getCart(rawCart);

        // Gộp lại thông tin qty từ local với dữ liệu chi tiết từ API
        const merged = productData.map((prod) => {
          const match = rawCart.find(
            (c) => c.id === prod.id || c.id === prod.productId
          );
          return {
            id: prod.id || prod.productId,
            name: prod.name,
            img: prod.imageUrl,
            price: prod.price,
            qty: match ? match.qty : 1,
          };
        });

        setItems(merged);
      } catch (err) {
        console.error("Lỗi khi gọi getCart:", err);
      }
    };

    fetchCart();
  }, []);

  const total = useMemo(
    () => items.reduce((sum, it) => sum + it.price * it.qty, 0),
    [items]
  );

  // 3) Phương thức thanh toán
  const [payment, setPayment] = useState("cod");
  const handlePayChange = (e) => setPayment(e.target.value);

  // 4) Show màn QR VNPAY
  const [showPayOnline, setShowPayOnline] = useState(false);

  // 5) Đặt hàng thành công với COD → redirect confirm
  const [success, setSuccess] = useState(false);
  const [orderId, setOrderId] = useState(null);
  const [payUrl, setPayUrl] = useState(null);

  // Mã đơn mô phỏng, về sau lấy từ backend trả về

  // Khi nhấn Đặt Hàng
  const handleSubmit = async () => {
    try {
      const requiredFields = [
        "name",
        "phone",
        "email",
        "address",
        "province",
        "district",
        "ward",
      ];

      const emptyFields = requiredFields.filter(
        (field) => !shipping[field]?.trim()
      );

      if (emptyFields.length > 0) {
        alert("Vui lòng nhập đầy đủ thông tin giao hàng!");
        return;
      }

      // Lấy cart từ localStorage
      const rawCart = JSON.parse(localStorage.getItem("cart")) || [];

      const itemsToSend = rawCart
        .filter((item) => item.qty && item.qty > 0)
        .map((item) => ({
          productId: item.id,
          quantity: item.qty,
        }));

      if (itemsToSend.length === 0) {
        alert("Giỏ hàng trống!");
        return;
      }

      // Lấy customerId từ localStorage (hoặc context nếu có)
      const storedUser = JSON.parse(localStorage.getItem("user"));
      const customerId = storedUser?.linkedId;

      if (!customerId) {
        alert("Bạn chưa đăng nhập!");
        return;
      }

      // 1. Gọi API tạo đơn hàng
      const orderRes = await createOrder(customerId, itemsToSend);
      const newOrderId = orderRes.orderId;
      setOrderId(newOrderId); // Gán vào state
      // 2. Nếu phương thức thanh toán là VNPAY
      const paymentMethodId = payment === "payonline" ? 1 : 2;
      const paymentData = {
        orderId: newOrderId,
        customerId,
        paymentMethodId,
        ...(paymentMethodId === 1
          ? { amount: total }
          : {
              to_name: shipping.name,
              to_phone: shipping.phone,
              to_address: shipping.address,
              to_ward_code: shipping.ward,
              to_district_id: parseInt(shipping.district),
            }),
      };

      // 3. Gọi API thanh toán

      // 4. Xử lý kết quả theo phương thức thanh toán
      // if (paymentMethodId === 1) {
      //   // PayOnline → chỉ hiển thị màn PayOnline
      //   setShowPayOnline(true); // <-- CHỈ hiển thị màn QR code
      // } else {
      //   // COD → tạo đơn vận chuyển
      //   const payRes = await createPayment(paymentData);
      //   setSuccess(true);
      //   localStorage.removeItem("cart");
      // }
      // ...

      if (paymentMethodId === 1) {
        const payRes = await createPayment(paymentData);

        const payUrl = payRes?.paymentUrl || payRes?.payUrl || payRes?.url;

        if (!payUrl) throw new Error("Không nhận được link thanh toán từ API.");

        setShowPayOnline(true);
        setOrderId(newOrderId); // vẫn set để truyền cho PayOnline
        localStorage.removeItem("cart");

        // Truyền thêm payUrl qua state
        setPayUrl(payUrl);
      } else {
        // COD → tạo đơn vận chuyển
        const payRes = await createPayment(paymentData);
        setSuccess(true);
        localStorage.removeItem("cart");
      }
    } catch (err) {
      console.error("Lỗi đặt hàng:", err);
      if (err.response) {
        console.error("Server trả về:", err.response.data); // <-- Quan trọng!
      }
      alert("Đặt hàng thất bại. Vui lòng thử lại.");
    }
  };

  // Nếu COD thành công thì chuyển confirm
  if (success) {
    return <Navigate to="/checkout/confirm" replace state={{ orderId }} />;
  }

  return (
    <>
      <Header />

      <div className="max-w-6xl mx-auto py-12 px-4 lg:px-0">
        {showPayOnline ? (
          <PayOnline payUrl={payUrl} onCancel={() => setShowPayOnline(false)} />
        ) : (
          // Màn form Checkout bình thường
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-y-8 lg:gap-x-[38px]">
            {/* ─── Cột trái: Shipping, Payment, Notes ─── */}
            <div className="space-y-8">
              <ShippingInfo data={shipping} onChange={handleShipChange} />

              <PaymentMethod method={payment} onChange={handlePayChange} />

              {/* Chỉ hiển thị ghi chú khi không phải VNPAY */}
              <div className="bg-white p-6 rounded-lg shadow">
                <h2 className="text-xl font-semibold mb-4">Ghi chú đơn hàng</h2>
                <textarea
                  name="notes"
                  value={shipping.notes}
                  onChange={handleShipChange}
                  placeholder="Ghi chú đơn hàng"
                  rows={4}
                  className="w-full border rounded-lg px-4 py-3 placeholder-gray-400 resize-none"
                />
              </div>
            </div>

            {/* ─── Cột phải: Cart summary & Order summary + nút Đặt ─── */}
            <div className="space-y-6">
              {/* Giỏ hàng */}
              <div className="bg-white p-6 rounded-lg shadow">
                <h2 className="text-lg font-semibold mb-4">Giỏ hàng</h2>
                {items.map((it) => (
                  <div
                    key={it.id}
                    className="flex items-center justify-between mb-4"
                  >
                    <div className="flex items-center space-x-4">
                      <img
                        src={it.img}
                        alt={it.name}
                        className="w-16 h-16 object-cover rounded"
                      />
                      <div>
                        <p className="font-medium">{it.name}</p>
                        <p className="text-red-500">
                          {it.price.toLocaleString()} VND
                        </p>
                      </div>
                    </div>
                    <div className="border px-3 py-1 rounded">{it.qty}</div>
                  </div>
                ))}
              </div>

              {/* Tóm tắt đơn hàng + nút Đặt Hàng */}
              <div className="bg-white p-6 rounded-lg shadow">
                <h2 className="text-lg font-semibold mb-4">Tóm tắt đơn hàng</h2>
                <div className="space-y-2 text-gray-700">
                  <div className="flex justify-between">
                    <span>Tổng tiền hàng</span>
                    <span>{total.toLocaleString()} VND</span>
                  </div>
                  <div className="flex justify-between">
                    <span>Phí vận chuyển</span>
                    <span>30,000 VND</span>
                  </div>
                  <div className="flex justify-between font-semibold">
                    <span>Tổng thanh toán</span>
                    <span>{total.toLocaleString()} VND</span>
                  </div>
                </div>
                <button
                  onClick={handleSubmit}
                  className="mt-6 w-full py-3 bg-green-500 text-white rounded-lg font-medium"
                >
                  Đặt Hàng
                </button>
              </div>
            </div>
          </div>
        )}
      </div>

      <Footer />
    </>
  );
};

export default CheckoutPage;
