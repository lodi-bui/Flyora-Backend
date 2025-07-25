import React, { useState } from "react";
import { MailIcon, MapPinIcon, PhoneIcon } from "lucide-react";
import { Button } from "../../components/ui/Button";
import { Card, CardContent } from "../../components/ui/Card";
import { Input } from "../../components/ui/Input";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
import { LoginAPI } from "../../api/Login"; // Giả sử bạn đã tạo API đăng nhập
import { useAuthCart } from "../../context/AuthCartContext"; // Import context để sử dụng login

const Login = () => {
  const navigate = useNavigate();
  const [form, setForm] = useState({ username: "", password: "" });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [showSuccess, setShowSuccess] = useState(false); // Thêm state cho popup
  const { login } = useAuthCart();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      // Gọi API đăng nhập
      const res = await LoginAPI(form.username, form.password);

      // Kiểm tra xem response có token hoặc thành công không
      // if (res && res.token) {
      //   // Lưu token nếu cần:
      //   localStorage.setItem("token", res.token);
      //   setShowSuccess(true);
      //   setTimeout(() => {
      //     setShowSuccess(false);
      //     navigate("/");
      //   }, 1500);
      // } else {
      //   // Nếu không có token hoặc trả về không hợp lệ, báo lỗi
      //   setError("Tài khoản hoặc mật khẩu không đúng.");
      // }

      if (res.status === 200) {
        const { userId, name, linkedId, role, token } = res.data;

        // Lưu token vào localStorage
        localStorage.setItem("token", token);

        // Lưu thông tin vào context
        login({ userId, name, linkedId, role, token });
        setShowSuccess(true);

        // Lưu thông tin người dùng vào context
        // login({
        //   userId: res.data.userId,
        //   name: res.data.name,
        //   linkedId: res.data.linkedId,
        //   role: res.data.role,
        // });

        setTimeout(() => {
          setShowSuccess(false);
          console.log("Đăng nhập thành công:", res.data);

          if (role === "Admin") {
            navigate("/admin-page");
          } else if (role === "ShopOwner" || role === "SalesStaff") {
            navigate("/shopowner");
          } else {
            navigate("/");
          }
        }, 500);
      } else {
        setError("Tài khoản hoặc mật khẩu không đúng.");
      }
    } catch (err) {
      setError("Đăng nhập thất bại. Vui lòng thử lại.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="relative min-h-screen bg-gradient-to-br from-[#083622] to-[#12ab3c] px-4 md:px-16 py-8 flex items-center justify-center">
      {/* Pop-up thông báo đăng nhập thành công */}
      {showSuccess && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/30">
          <div className="bg-white rounded-xl shadow-lg px-8 py-6 flex flex-col items-center">
            <svg
              className="w-12 h-12 text-green-500 mb-2"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                d="M5 13l4 4L19 7"
              />
            </svg>
            <span className="text-lg font-semibold text-green-600">
              Đăng nhập thành công!
            </span>
          </div>
        </div>
      )}

      {/* Contact Info - Đưa lên đầu */}
      <div className="absolute top-0 left-0 w-full flex flex-col md:flex-row justify-between items-center px-12 py-4 text-sm z-20">
        <div className="flex gap-6 items-center">
          <div className="flex items-center gap-2 text-white font-bold">
            <PhoneIcon className="w-5 h-5" />
            <span>+84 33678915</span>
          </div>
          <div className="flex items-center gap-2 text-white font-bold">
            <MailIcon className="w-5 h-5" />
            <span>ntrang21102005@gmail.com</span>
          </div>
        </div>
        <div className="flex items-center gap-2 mt-2 md:mt-0 text-white font-bold">
          <MapPinIcon className="w-5 h-5" />
          <span>12 Hoang Hoa Tham, Quan 3, TP.HCM</span>
        </div>
      </div>

      {/* Background images */}
      <div className="absolute inset-0 -z-10 pointer-events-none">
        <img
          className="absolute bottom-0 left-0 w-[600px] opacity-20 blur-sm"
          src="https://c.animaapp.com/mbzzgsmyZQHMQ6/img/group-9.png"
          alt="Bird bg"
        />
        <img
          className="absolute top-32 right-[500px] w-[200px] opacity-30 blur-sm"
          src="https://c.animaapp.com/mbzzgsmyZQHMQ6/img/img.png"
          alt="Bird"
        />
        <img
          className="absolute top-[241px] right-[26px] w-[136px] opacity-30 blur-sm"
          src="https://c.animaapp.com/mbzzgsmyZQHMQ6/img/star-17.svg"
          alt="Star"
        />
        <img
          className="absolute top-[124px] right-[63px] w-[136px] opacity-30 blur-sm"
          src="https://c.animaapp.com/mbzzgsmyZQHMQ6/img/star-20.svg"
          alt="Star"
        />
        <img
          className="absolute top-[0] right-[63px] w-[136px] opacity-30 blur-sm"
          src="https://c.animaapp.com/mbzzgsmyZQHMQ6/img/star-19.svg"
          alt="Star"
        />
        <img
          className="absolute top-[303px] right-[133px] w-[135px] opacity-30 blur-sm"
          src="https://c.animaapp.com/mbzzgsmyZQHMQ6/img/star-16.svg"
          alt="Star"
        />
        <img
          className="absolute top-[50px] right-[450px] w-[246px] h-[215px] opacity-20 blur-sm"
          src="https://c.animaapp.com/mbzzgsmyZQHMQ6/img/ellipse-3.png"
          alt="Ellipse"
        />
      </div>

      {/* Content */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-12 md:gap-24 w-full max-w-6xl z-10 mt-24">
        {/* Left: Welcome Section */}
        <div className="text-white">
          <div className="flex items-center mb-6">
            <img
              className="w-[7vw] h-[7vw] mr-4 rotate-0" // Xoay chim lên trên (không xoay hoặc dùng rotate-0)
              src="https://c.animaapp.com/mbzzgsmyZQHMQ6/img/group-10.png"
              alt="Logo"
            />
            <h1 className="text-[45px] font-bold">
              <span className="text-orange-500">Flyora</span>{" "}
              <span className="text-orange-500">Shop</span>
            </h1>
          </div>
          <h2 className="text-[40px] font-bold mb-6">CHÀO MỪNG ĐẾN VỚI FLYORA SHOP</h2>
          <p className="text-xl mb-8">Cửa hàng chim với mọi thứ bạn cần</p>
          <Button
            className="bg-green-600 hover:bg-green-700 text-white px-6 py-3 text-lg rounded shadow"
            onClick={() => navigate("/")}
          >
            Khám Phá Chúng Tôi
          </Button>
        </div>

        {/* Right: Login Form */}
        <Card className="w-full max-w-md bg-white rounded-2xl shadow-xl">
          <CardContent className="p-10">
            <form className="space-y-6" onSubmit={handleSubmit}>
              <div>
                <label className="block font-medium mb-1 text-gray-700">
                  Tên người dùng
                </label>
                <Input
                  name="username"
                  value={form.username}
                  onChange={handleChange}
                  placeholder="Enter your username..."
                  className="w-full px-4 py-2 rounded shadow text-gray-700 placeholder:text-gray-400 placeholder:font-normal"
                  autoComplete="username"
                  required
                />
              </div>
              <div>
                <label className="block font-medium mb-1 text-gray-700">
                  Mật khẩu
                </label>
                <Input
                  name="password"
                  type="password"
                  value={form.password}
                  onChange={handleChange}
                  placeholder="Enter your password..."
                  className="w-full px-4 py-2 rounded shadow text-gray-700 placeholder:text-gray-400 placeholder:font-normal"
                  autoComplete="current-password"
                  required
                />
              </div>

              {error && (
                <div className="text-red-500 text-sm font-semibold text-center">
                  {error}
                </div>
              )}

              <Button
                type="submit"
                disabled={loading}
                className="w-full bg-orange-500 text-black font-semibold py-2 rounded shadow transition-colors duration-200 hover:bg-orange-600 hover:text-white"
              >
                {loading ? "Đang đăng nhập..." : "Đăng nhập"}
              </Button>

              <div className="text-center text-sm">
                Chưa có tài khoản?{" "}
                <Link to="/register" className="text-blue-600 font-medium">
                  Tạo tài khoản mới
                </Link>
              </div>

              {/* <div className="relative">
                <Separator className="my-6" />
                <div className="absolute -top-3 left-1/2 transform -translate-x-1/2 bg-white px-2 text-gray-600 text-sm">
                  Or
                </div>
              </div> */}

              {/* <Button
                type="button"
                variant="outline"
                className="w-full border rounded-xl py-3 text-gray-700 flex items-center justify-center gap-4 shadow"
              >
                <img
                  src="https://c.animaapp.com/mbzzgsmyZQHMQ6/img/logo.png"
                  alt="Google"
                  className="w-5 h-5"
                />
                Sign in with Google
              </Button> */}
            </form>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default Login;
