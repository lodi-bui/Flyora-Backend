// import React from "react";
// import { FaMapMarkerAlt, FaEnvelope, FaPhone, FaClock } from "react-icons/fa";
// import map from "../../icons/map.png"; // Adjust the path as necessary

// const ContactPage = () => {
//   return (
//     <div className="px-4 md:px-16 lg:px-32 py-16 space-y-16 text-gray-800">
//       {/* Contact Section */}
//       <section className="grid grid-cols-1 md:grid-cols-2 gap-10">
//         {/* Message Form */}
//         <div className="bg-gray-50 p-6 rounded-xl shadow-sm">
//           <label htmlFor="message" className="block text-sm font-medium mb-2">
//             Message
//           </label>
//           <textarea
//             id="message"
//             rows="6"
//             placeholder="Your message..."
//             className="w-full p-4 border rounded-lg resize-none focus:outline-none focus:ring-2 focus:ring-orange-300"
//           ></textarea>
//           <button className="mt-4 bg-orange-500 hover:bg-orange-600 text-white px-6 py-2 rounded-lg transition">
//             Send Message
//           </button>
//         </div>

//         {/* Contact Info */}
//         <div>
//           <h2 className="text-2xl font-bold mb-4">Feel free to contact us</h2>
//           <p className="text-gray-600 mb-6">
//             At et vehicula sodales est proin turpis pellentesque sinulla a
//             aliquam amet rhoncus quisque eget sit. Sociis blandit et
//             pellentesque aliquet at quisque tortor lacinia nullam
//           </p>
//           <ul className="space-y-4 text-sm">
//             <li className="flex items-start gap-3">
//               <FaMapMarkerAlt className="text-orange-500 mt-1" />
//               <span className="font-medium">
//                 12 Hoang Hoa Tham, Quan 3, TP.HCM
//               </span>
//             </li>
//             <li className="flex items-start gap-3">
//               <FaEnvelope className="text-orange-500 mt-1" />
//               <span className="font-medium">ntrang21102005@gmail.com</span>
//             </li>
//             <li className="flex items-start gap-3">
//               <FaPhone className="text-orange-500 mt-1" />
//               <span className="font-medium">+84 33678915</span>
//             </li>
//             <li className="flex items-start gap-3">
//               <FaClock className="text-orange-500 mt-1" />
//               <span className="font-medium">
//                 Mon - Sun: 08AM – 11PM (Online)
//               </span>
//             </li>
//           </ul>
//         </div>
//       </section>

//       {/* Map Section */}
//       <section className="text-center">
//         <a
//           href="https://www.google.com/maps/?hl=vi"
//           target="_blank"
//           rel="noopener noreferrer"
//           className="block w-full h-80 rounded-xl overflow-hidden shadow-md"
//         >
//           <img
//             src={map} // phải đảm bảo biến `map` đã import đúng
//             alt="Map"
//             className="w-full h-full object-cover hover:scale-105 transition-transform duration-300"
//           />
//         </a>
//       </section>
//     </div>
//   );
// };
// export default ContactPage;
