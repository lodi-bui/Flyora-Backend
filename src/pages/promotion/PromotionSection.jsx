import React, { useEffect, useState } from "react";
import FlashSaleBanner from "./FlashSaleBanner";
import { Percent, Copy, Check, Truck, Clock } from "lucide-react";

const vouchers = [
  {
    id: "SALE10",
    title: "Giảm 10% sản phẩm",
    typeLabel: "Sản phẩm",
    percent: 10,
    color: "orange",
    gradient: "from-orange-500 via-orange-400 to-orange-500",
    ring: "ring-orange-300/60",
    iconBg: "bg-orange-500/10",
    iconColor: "text-orange-500",
    description: "Tối đa 200.000₫ cho đơn từ 300.000₫.",
    minOrder: 300000,
    maxDiscount: 200000,
    expire: "2025-08-31T23:59:59+07:00",
    usesLeft: 120,
    usesTotal: 500,
  },

  {
    id: "SALE20",
    title: "Giảm 15% sản phẩm",
    typeLabel: "Sản phẩm",
    percent: 15,
    color: "orange",
    gradient: "from-orange-500 via-orange-400 to-orange-500",
    ring: "ring-orange-300/60",
    iconBg: "bg-orange-500/10",
    iconColor: "text-orange-500",
    description: "Tối đa 300.000₫ cho đơn từ 500.000₫.",
    minOrder: 500000,
    maxDiscount: 300000,
    expire: "2025-09-15T23:59:59+07:00",
    usesLeft: 80,
    usesTotal: 400,
  },

  {
    id: "SALE50",
    title: "Giảm 15% sản phẩm",
    typeLabel: "Sản phẩm",
    percent: 15,
    color: "orange",
    gradient: "from-orange-500 via-orange-400 to-orange-500",
    ring: "ring-orange-300/60",
    iconBg: "bg-orange-500/10",
    iconColor: "text-orange-500",
    description: "Tối đa 300.000₫ cho đơn từ 500.000₫.",
    minOrder: 500000,
    maxDiscount: 300000,
    expire: "2025-09-15T23:59:59+07:00",
    usesLeft: 80,
    usesTotal: 400,
  },

 {
    id: "SALE50",
    title: "Giảm 15% sản phẩm",
    typeLabel: "Sản phẩm",
    percent: 15,
    color: "orange",
    gradient: "from-orange-500 via-orange-400 to-orange-500",
    ring: "ring-orange-300/60",
    iconBg: "bg-orange-500/10",
    iconColor: "text-orange-500",
    description: "Tối đa 300.000₫ cho đơn từ 500.000₫.",
    minOrder: 500000,
    maxDiscount: 300000,
    expire: "2025-09-15T23:59:59+07:00",
    usesLeft: 80,
    usesTotal: 400,
  },

  {
    id: "SHIP15",
    title: "Giảm 15% phí giao hàng",
    typeLabel: "Vận chuyển",
    percent: 15,
    color: "green",
    gradient: "from-green-500 via-green-400 to-green-500",
    ring: "ring-green-300/60",
    iconBg: "bg-green-500/10",
    iconColor: "text-green-600",
    description: "Giảm tối đa 30.000₫. Cho đơn từ 99.000₫.",
    minOrder: 99000,
    maxDiscount: 30000,
    expire: "2025-09-31T23:59:59+07:00",
    usesLeft: 57,
    usesTotal: 200,
  },

  {
    id: "SHIP25",
    title: "Giảm 25% phí giao hàng",
    typeLabel: "Vận chuyển",
    percent: 25,
    color: "green",
    gradient: "from-green-500 via-green-400 to-green-500",
    ring: "ring-green-300/60",
    iconBg: "bg-green-500/10",
    iconColor: "text-green-600",
    description: "Giảm tối đa 50.000₫. Cho đơn từ 150.000₫.",
    minOrder: 150000,
    maxDiscount: 50000,
    expire: "2025-08-31T23:59:59+07:00",
    usesLeft: 100,
    usesTotal: 300,
  },
];

const Pill = ({ children, className = "" }) => (
  <span
    className={`inline-flex items-center rounded-full px-2.5 py-1 text-xs font-medium tracking-wide uppercase bg-neutral-100 text-neutral-600 ring-1 ring-inset ring-neutral-200 ${className}`}
  >
    {children}
  </span>
);

const FormatCurrency = ({ value }) => <>{value.toLocaleString("vi-VN")}₫</>;

// const ProgressBar = ({ value, total, color }) => {
//   const percent = Math.min(100, Math.round((value / total) * 100));
//   return (
//     <div className="space-y-1">
//       <div className="flex justify-between text-[11px] font-medium text-neutral-500">
//         <span>
//           Đã dùng {total - value}/{total}
//         </span>
//         <span>{percent}%</span>
//       </div>
//       <div className="h-2 w-full rounded-full bg-neutral-200 overflow-hidden">
//         <div
//           className={`h-full rounded-full transition-all duration-500 ${
//             color === "orange"
//               ? "bg-orange-500"
//               : color === "green"
//               ? "bg-green-500"
//               : "bg-neutral-500"
//           }`}
//           style={{ width: `${percent}%` }}
//         />
//       </div>
//     </div>
//   );
// };

const CopyButton = ({ code }) => {
  const [copied, setCopied] = useState(false);
  const handleCopy = () => {
    navigator.clipboard.writeText(code).then(() => {
      setCopied(true);
      setTimeout(() => setCopied(false), 1600);
    });
  };
  return (
    <button
      type="button"
      onClick={handleCopy}
      className={`group relative inline-flex items-center gap-1 rounded-lg border px-2.5 py-1.5 text-xs font-medium shadow-sm transition focus:outline-none focus:ring-2 focus:ring-offset-2 ${
        copied
          ? "border-emerald-500 bg-emerald-50 text-emerald-700 focus:ring-emerald-400"
          : "border-neutral-200 bg-white text-neutral-700 hover:border-neutral-300 focus:ring-neutral-400"
      }`}
    >
      {copied ? (
        <Check className="h-3.5 w-3.5" />
      ) : (
        <Copy className="h-3.5 w-3.5" />
      )}
      <span>{copied ? "Đã áp dụng" : code}</span>
    </button>
  );
};

const ExpireBadge = ({ expire }) => {
  const date = new Date(expire);
  const isSoon = date.getTime() - Date.now() < 1000 * 60 * 60 * 24 * 7;
  return (
    <div
      className={`flex items-center gap-1 text-[11px] font-medium ${
        isSoon ? "text-red-500" : "text-neutral-500"
      }`}
    >
      <Clock className="h-3.5 w-3.5" />
      <span>HSD: {date.toLocaleDateString("vi-VN")}</span>
    </div>
  );
};

const VoucherCard = ({ v }) => (
  <div
    className={`group relative flex flex-col overflow-hidden rounded-2xl ring-1 backdrop-blur-sm transition shadow-sm hover:shadow-md ${v.ring} bg-white/90`}
  >
    <div
      className={`absolute inset-y-0 left-0 w-1 bg-gradient-to-b ${v.gradient}`}
    />
    <div className="flex flex-col gap-3 p-5 pb-4">
      <div className="flex items-start justify-between gap-4">
        <div className="flex items-center gap-3">
          <div
            className={`h-12 w-12 rounded-xl ${v.iconBg} flex items-center justify-center ring-1 ring-inset ring-white/30`}
          >
            {v.color === "orange" ? (
              <Percent className={`h-6 w-6 ${v.iconColor}`} />
            ) : (
              <Truck className={`h-6 w-6 ${v.iconColor}`} />
            )}
          </div>
          <div>
            <h3 className="text-base font-semibold text-neutral-800 flex items-center gap-2">
              {v.title}
              <Pill
                className={
                  v.color === "orange"
                    ? "bg-orange-100 text-orange-700 ring-orange-300/60"
                    : "bg-green-100 text-green-700 ring-green-300/60"
                }
              >
                {v.percent}% OFF
              </Pill>
            </h3>
            <p className="mt-1 text-[13px] text-neutral-500">{v.description}</p>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-3 gap-3 text-[11px] text-neutral-600">
        <div className="rounded-lg bg-neutral-50 px-2 py-1.5 ring-1 ring-neutral-200">
          <span className="block text-neutral-400">Đơn tối thiểu</span>
          <span className="text-[12px]">
            <FormatCurrency value={v.minOrder} />
          </span>
        </div>
        <div className="rounded-lg bg-neutral-50 px-2 py-1.5 ring-1 ring-neutral-200">
          <span className="block text-neutral-400">Tối đa</span>
          <span className="text-[12px]">
            <FormatCurrency value={v.maxDiscount} />
          </span>
        </div>
        <div className="rounded-lg bg-neutral-50 px-2 py-1.5 ring-1 ring-neutral-200">
          <span className="block text-neutral-400">Loại</span>
          <span className="text-[12px]">{v.typeLabel}</span>
        </div>
      </div>
      {/* <ProgressBar value={v.usesLeft} total={v.usesTotal} color={v.color} /> */}
    </div>
    <div className="mt-auto flex items-center justify-between gap-2 border-t border-dashed border-neutral-200 bg-neutral-50/60 px-5 py-3">
      <ExpireBadge expire={v.expire} />
      <div className="flex gap-2">
        <CopyButton code={v.id} />
        {/* <button
          type="button"
          className={`inline-flex items-center gap-1.5 rounded-lg px-3 py-2 text-sm font-semibold shadow-sm ring-1 ring-inset transition focus:outline-none focus:ring-2 focus:ring-offset-2 ${
            v.color === "orange"
              ? "bg-orange-500 text-white ring-orange-400 hover:bg-orange-600 focus:ring-orange-500"
              : "bg-green-600 text-white ring-green-500 hover:bg-green-700 focus:ring-green-600"
          }`}
        >
          Áp dụng
        </button> */}
      </div>
    </div>
  </div>
);

const PromotionsSection = () => {
  const [secondsLeft, setSecondsLeft] = useState(() =>
    Math.floor(
      (new Date("2025-07-31T23:59:59+07:00").getTime() - Date.now()) / 1000
    )
  );

  useEffect(() => {
    const timer = setInterval(() => {
      setSecondsLeft((prev) => (prev > 0 ? prev - 1 : 0));
    }, 1000);
    return () => clearInterval(timer);
  }, []);

  // const formatTime = (sec) => {
  //   const h = String(Math.floor(sec / 3600)).padStart(2, "0");
  //   const m = String(Math.floor((sec % 3600) / 60)).padStart(2, "0");
  //   const s = String(sec % 60).padStart(2, "0");
  //   return `${h}:${m}:${s}`;
  // };

  return (
    <main className="relative mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 py-10">
      <FlashSaleBanner />

      {/* Danh sách voucher */}
      <div className="mt-10 grid gap-6 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
        {vouchers.map((v) => (
          <VoucherCard key={v.id} v={v} />
        ))}
      </div>
    </main>
  );
};

export default PromotionsSection;
