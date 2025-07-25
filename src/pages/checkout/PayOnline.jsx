// src/pages/checkout/PayOnline.jsx
import React, { useEffect } from "react";
import { createPayOSLink } from "../../api/PayOs";
import { createPayment } from "../../api/Payment";

// const PayOnline = ({ total, orderId, onCancel }) => {
//   useEffect(() => {
//     const handleRedirect = async () => {
//       try {
//         const customerId = JSON.parse(localStorage.getItem("user"))?.linkedId;

//         // 1. Tạo payment trên hệ thống của bạn
//         await createPayment({
//           orderId,
//           customerId,
//           paymentMethodId: 1,
//           amount: total,
//         });

//         // 2. Gọi API tạo link PayOS
//         const data = await createPayOSLink(orderId, total);
//         const payUrl =
//           typeof data === "string" ? data : data?.payUrl || data?.url;

//         if (payUrl) {
//           window.location.href = payUrl; // redirect người dùng
//         } else {
//           console.warn("Không tìm thấy link thanh toán:", data);
//           alert("Không tìm thấy link thanh toán.");
//         }
//       } catch (err) {
//         console.error(
//           "Lỗi tạo thanh toán hoặc lấy link PayOS:",
//           err.response?.data || err.message
//         );
//         alert("Không thể tạo link thanh toán. Vui lòng thử lại.");
//       }
//     };

//     handleRedirect();
//   }, [orderId, total]);

const PayOnline = ({ payUrl, onCancel }) => {
  useEffect(() => {
    if (payUrl) {
      window.location.href = payUrl;
    } else {
      console.warn("Không có đường dẫn thanh toán để chuyển hướng.");
    }
  }, [payUrl]);

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-6 bg-white p-6 rounded-lg shadow-lg">
      {/* Left: Thông tin đơn hàng */}
      {/* <div className="bg-green-50 p-4 rounded-lg">
        <h3 className="text-lg font-medium mb-2">Thông tin đơn hàng</h3>
        <div className="bg-white p-6 rounded-lg shadow">
          <p className="text-gray-600">Số tiền thanh toán</p>
          <p className="text-red-500 font-bold text-2xl mb-4">
            {total.toLocaleString()}₫
          </p>
          <p className="text-gray-600">Mã đơn hàng</p>
          <p className="font-semibold">{orderId}</p>
        </div>
      </div> */}

      {/* Cột trái: Thông báo */}
      <div className="bg-green-50 p-4 rounded-lg">
        <h3 className="text-lg font-medium mb-2">Đang xử lý thanh toán</h3>
        <p className="text-gray-700">
          Bạn sẽ được chuyển đến trang thanh toán.
        </p>
      </div>

      {/* Right: Trạng thái */}
      <div className="text-center flex flex-col justify-center items-center">
        <h3 className="text-lg font-medium mb-4">
          Đang chuyển hướng đến PayOS...
        </h3>
        <p className="text-gray-500 mb-4">Vui lòng đợi trong giây lát</p>
        <button
          onClick={onCancel}
          className="px-6 py-2 bg-gray-200 rounded-lg hover:bg-gray-300"
        >
          Hủy thanh toán
        </button>
      </div>
    </div>
  );
};

export default PayOnline;

// // src/pages/checkout/PayOnline.jsx
// import React, { useEffect } from "react";
// import { createPayOSLink } from "../../api/PayOs";
// import { createPayment } from "../../api/Payment";

// const PayOnline = ({ total, orderId, customerId, shipping, onCancel }) => {
//   useEffect(() => {
//     const handleRedirect = async () => {
//       try {
//         // 1. Tạo payment qua API backend
//         await createPayment({
//           orderId,
//           customerId,
//           paymentMethodId: 1, // PayOS
//           amount: total,
//           to_name: shipping?.name,
//           to_phone: shipping?.phone,
//           to_address: shipping?.address,
//           to_ward_code: shipping?.ward,
//           to_district_id: parseInt(shipping?.district),
//         });
//         const paymentPayload = {
//           orderId,
//           customerId,
//           paymentMethodId: 1,
//           amount: total,
//         };

//         console.log("Payload gửi lên createPayment:", paymentPayload);
//         await createPayment(paymentPayload);

//         // 2. Gọi PayOS để tạo link QR
//         const data = await createPayOSLink(orderId, total);
//         const payUrl =
//           typeof data === "string" ? data : data?.payUrl || data?.url;

//         if (payUrl) {
//           window.location.href = payUrl;
//         } else {
//           alert("Không tìm thấy link thanh toán.");
//         }
//       } catch (err) {
//         console.error(
//           "Lỗi tạo thanh toán hoặc lấy link PayOS:",
//           err.response?.data || err.message
//         );
//         alert("Không thể tạo link thanh toán. Vui lòng thử lại.");
//       }
//     };

//     handleRedirect();
//   }, [orderId, total, customerId, shipping]);

//   return (
//     <div className="grid grid-cols-1 md:grid-cols-2 gap-6 bg-white p-6 rounded-lg shadow-lg">
//       {/* Left: Thông tin đơn hàng */}
//       <div className="bg-green-50 p-4 rounded-lg">
//         <h3 className="text-lg font-medium mb-2">Thông tin đơn hàng</h3>
//         <div className="bg-white p-6 rounded-lg shadow">
//           <p className="text-gray-600">Số tiền thanh toán</p>
//           <p className="text-red-500 font-bold text-2xl mb-4">
//             {total.toLocaleString()}₫
//           </p>
//           <p className="text-gray-600">Mã đơn hàng</p>
//           <p className="font-semibold">{orderId}</p>
//         </div>
//       </div>

//       {/* Right: Trạng thái */}
//       <div className="text-center flex flex-col justify-center items-center">
//         <h3 className="text-lg font-medium mb-4">
//           Đang chuyển hướng đến PayOS...
//         </h3>
//         <p className="text-gray-500 mb-4">Vui lòng đợi trong giây lát</p>
//         <button
//           onClick={onCancel}
//           className="px-6 py-2 bg-gray-200 rounded-lg hover:bg-gray-300"
//         >
//           Hủy thanh toán
//         </button>
//       </div>
//     </div>
//   );
// };

// export default PayOnline;
