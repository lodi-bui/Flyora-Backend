// src/pages/checkout/VnpaySection.jsx
import React from "react";
import qrImage from "../../icons/vnpay-qr.png"; // ảnh QR lấy về từ backend hoặc assets

const VnpaySection = ({ total, orderId, onCancel }) => (
  <div className="grid grid-cols-1 md:grid-cols-2 gap-6 bg-white p-6 rounded-lg shadow-lg">
    {/* Left: Thông tin đơn hàng */}
    <div className="bg-green-50 p-4 rounded-lg">
      <h3 className="text-lg font-medium mb-2">Thông tin đơn hàng</h3>
      <div className="bg-white p-6 rounded-lg shadow">
        <p className="text-gray-600">Số tiền thanh toán</p>
        <p className="text-red-500 font-bold text-2xl mb-4">
          {total.toLocaleString()}₫
        </p>
        <p className="text-gray-600">Mã đơn hàng</p>
        <p className="font-semibold">{orderId}</p>
      </div>
    </div>

    {/* Right: QR Code */}
    <div className="text-center">
      <h3 className="text-lg font-medium mb-4">
        Quét mã qua ứng dụng Ví VNPAY
      </h3>
      <img
        src={qrImage}
        alt="VNPay QR"
        className="mx-auto mb-4 w-64 h-64 object-contain"
      />
      <button
        onClick={onCancel}
        className="px-6 py-2 bg-gray-200 rounded-lg hover:bg-gray-300"
      >
        Hủy thanh toán
      </button>
    </div>
  </div>
);

export default VnpaySection;
