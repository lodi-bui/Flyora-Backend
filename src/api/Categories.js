
const mockCategories = [
  { id: '1', slug: '1', name: 'Đồ ăn', image: "https://i.postimg.cc/g2Dc3Jd5/food.jpg" },
  { id: '2', slug: '2', name: 'Đồ chơi', image: "https://i.postimg.cc/5NJMpVPM/toys.webp" },
  { id: '3', slug: '3', name: 'Đồ nội thất', image: "https://i.postimg.cc/d0fY9ZfW/furniture.jpg" },
];

export async function getCategories() {
  return new Promise(resolve => setTimeout(() => resolve(mockCategories), 300));
}

// Nếu dùng API thật, bỏ comment dưới đây và sửa endpoint cho phù hợp
// export async function getCategories() {
//   const response = await axios.get('/api/categories');
//   return response.data;
// }