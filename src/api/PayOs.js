import axios from "axios";
const BASE = "https://flyora-backend.onrender.com/api/payos";

export const createPayOSLink = async (orderId, amount) => {
  const res = await axios.post(`${BASE}/create-link/${orderId}?amount=${amount}`);
  // const res = await axios.post(`${BASE}/create-link/${orderId}`, { amount });
  return res.data;
};