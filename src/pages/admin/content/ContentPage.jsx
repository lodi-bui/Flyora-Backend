import React, { useState } from "react";
import { Search, Plus } from "lucide-react";
import Sidebar from "../sidebar/Sidebar";
import { createNewsBlogSalesPost } from "api/NewsBlogSales";
import toast from "react-hot-toast";
const ContentPage = () => {
    const [url, setUrl] = useState("");
    const [title, setTitle] = useState("");
    const requesterId = localStorage.getItem("linkedId");

    const handleCreatePost = async (e) => {
        e.preventDefault();
        if (!title.trim() || !url.trim()) {
            toast.error("Please enter both title and URL.");
            return;
        }
        try {
            const response = await createNewsBlogSalesPost(requesterId, { title, url });
            toast.success("News/Blog post created successfully!");
            setUrl("");
            setTitle("");
        } catch (error) {
            console.error("Error creating post:", error.response?.data || error.message);
            toast.error(`Failed to create news/blog post: ${error.response?.data?.message || error.message}`);
        }
    };

    return (
        <div className="flex min-h-screen">
            {/* Sidebar */}
            <Sidebar />

      {/* Main Content */}
      <div className="flex-1 flex flex-col">
        {/* Header */}
        <header className="bg-white shadow-sm border-b border-gray-200">
          <div className="flex items-center justify-between px-6 py-4">
            <div className="flex items-center space-x-4">
              <h1 className="text-xl font-semibold text-gray-900">
                Cập nhật tin tức
              </h1>
            </div>
          </div>
        </header>
        {/* Content */}
        <main className="flex-1 p-4 flex justify-center items-center">
          <div className="bg-white rounded-[20px] shadow p-6 w-full max-w-lg">
            <form onSubmit={handleCreatePost} className="space-y-8">
              <div className="flex items-center space-x-2">
                <label className="block text-lg font-medium text-gray-700 w-10">
                  URL
                </label>
                <input
                  type="text"
                  value={url}
                  onChange={(e) => setUrl(e.target.value)}
                  placeholder="Enter here"
                  className="flex-1 p-3 border border-gray-300 rounded-lg bg-gray-200 text-gray-500 focus:outline-none"
                  required
                />
              </div>
              <div className="flex items-center space-x-2">
                <label className="block text-lg font-medium text-gray-700 w-10">
                  Tiêu đề
                </label>
                <input
                  type="text"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                  placeholder="Enter here"
                  className="flex-1 p-3 border border-gray-300 rounded-lg bg-gray-200 text-gray-500 focus:outline-none"
                  required
                />
              </div>
              <div className="text-right">
                <button
                  type="submit"
                  className="px-3 py-2 bg-green-500 text-white rounded-full hover:bg-green-600 transition-colors text-sm flex items-center justify-center ml-[380px]   "
                >
                  <Plus className="w-4 h-4 mr-1" />
                  Tạo
                </button>
              </div>
            </form>
          </div>
        </main>
      </div>
    </div>
  );
};
export default ContentPage;
